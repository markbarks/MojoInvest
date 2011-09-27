package com.mns.alphaposition.server.mapper;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.mapreduce.AppEngineMapper;
import com.google.appengine.tools.mapreduce.BlobstoreRecordKey;
import com.google.appengine.tools.mapreduce.DatastoreMutationPool;
import com.google.common.base.Functions;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Ordering;
import com.googlecode.objectify.ObjectifyService;
import com.mns.alphaposition.server.engine.model.Quote;
import com.mns.alphaposition.server.engine.model.QuoteDao;
import org.apache.hadoop.io.NullWritable;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.logging.Logger;

public class RankingCalculatorMapper extends
        AppEngineMapper<BlobstoreRecordKey, byte[], NullWritable, NullWritable> {

    public static final DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");

    private static final Logger log = Logger.getLogger(RankingCalculatorMapper.class.getName());

    private final QuoteDao dao = new QuoteDao(ObjectifyService.factory());

    @Override
    public void map(BlobstoreRecordKey key, byte[] segment, Context context) {

        String line = new String(segment);

        log.info("At offset: " + key.getOffset());
        log.info("Got value: " + line);

        LocalDate date = fmt.parseDateTime(line).toLocalDate();

        List<Quote> toQuotes = dao.query(date);
        List<Quote> fromQuotes = dao.query(date.minusMonths(9));

        Map<String, Quote> fromQuoteMap = new HashMap<String, Quote>(fromQuotes.size());
        for (Quote quote : fromQuoteMap.values()) {
            fromQuoteMap.put(quote.getSymbol(), quote);
        }

        Map<String, BigDecimal> ranker = new HashMap<String, BigDecimal>();
        for (Quote toQuote : toQuotes) {
            if (fromQuoteMap.containsKey(toQuote.getSymbol())) {
                ranker.put(toQuote.getSymbol(), percentageChange(fromQuoteMap.get(toQuote.getSymbol()), toQuote));
            }
        }

        Ordering<String> valueComparator = Ordering.natural().onResultOf(Functions.forMap(ranker));
        SortedSet<String> rank = ImmutableSortedMap.copyOf(ranker, valueComparator).keySet();

        Joiner joiner = Joiner.on(",");
        String m9 = joiner.join(rank);

        Entity ranking = new Entity("Ranking", fmt.print(date));

        ranking.setUnindexedProperty("m9", m9);

        DatastoreMutationPool mutationPool = this.getAppEngineContext(context)
                .getMutationPool();
        mutationPool.put(ranking);

    }

    private static BigDecimal percentageChange(Quote fromQuote, Quote toQuote) {
        return percentageChange(fromQuote.getClose(), toQuote.getClose());
    }

    private static BigDecimal percentageChange(BigDecimal from, BigDecimal to) {
        BigDecimal change = to.subtract(from);
        return change.divide(from, 5, RoundingMode.HALF_EVEN);
    }

}