package com.mns.mojoinvest.server.engine.model.dao;

import com.google.inject.Inject;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.Query;
import com.mns.mojoinvest.server.engine.model.Fund;
import com.mns.mojoinvest.server.engine.model.Quote;
import com.mns.mojoinvest.server.util.QuoteUtils;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class QuoteDao extends DAOBase {

    private static final Logger log = Logger.getLogger(QuoteDao.class.getName());

    private static boolean objectsRegistered;

    @Inject
    public QuoteDao(final ObjectifyFactory objectifyFactory) {
        super(objectifyFactory);
    }

    @Override
    protected boolean areObjectsRegistered() {
        return objectsRegistered;
    }


    @Override
    public void registerObjects(ObjectifyFactory ofyFactory) {
        objectsRegistered = true;
        ofyFactory.register(Quote.class);
        ofyFactory.getConversions().add(new MyTypeConverters());
    }

    public Key<Quote> put(Quote quote) {
        return ofy().put(quote);
    }

    public Map<Key<Quote>, Quote> put(Iterable<Quote> quotes) {
        return ofy().put(quotes);
    }

    public List<Quote> list() {
        Query<Quote> q = ofy().query(Quote.class);
        return q.list();
    }

    public List<Quote> query(Map<String, Object> filters) {
        Query<Quote> q = ofy().query(Quote.class);
        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            q.filter(entry.getKey(), entry.getValue());
        }
        return q.list();
    }

    public List<Quote> query(Fund fund) {
        return query(fund.getSymbol());
    }

    public List<Quote> query(String symbol) {
        Query<Quote> q = ofy().query(Quote.class);
        q.filter("symbol", symbol);
        return q.list();
    }

    public List<Quote> query(LocalDate date) {
        Query<Quote> q = ofy().query(Quote.class);
        q.filter("date", date.toDateMidnight().toDate());
        return q.list();
    }

    public List<Quote> query(String symbol, LocalDate date) {
        Query<Quote> q = ofy().query(Quote.class);
        q.filter("symbol", symbol);
        q.filter("date", date.toDateMidnight().toDate());
        return q.list();
    }

    public Collection<Quote> get(List<Key<Quote>> keys) {
        return ofy().get(keys).values();
    }

    public Collection<Quote> get(Collection<Fund> funds, Collection<LocalDate> dates) {
        List<Key<Quote>> keys = new ArrayList<Key<Quote>>();
        for (LocalDate date : dates) {
            for (Fund fund : funds) {
                keys.add(new Key<Quote>(Quote.class, QuoteUtils.quoteId(fund.getSymbol(), date)));
            }
        }
        return get(keys);
    }

    public Collection<Quote> get(Fund fund, Collection<LocalDate> dates) {
        List<Key<Quote>> keys = new ArrayList<Key<Quote>>();
        for (LocalDate date : dates) {
            keys.add(new Key<Quote>(Quote.class, QuoteUtils.quoteId(fund.getSymbol(), date)));
        }
        return get(keys);
    }

    public Quote get(String symbol, LocalDate date) {
        Key<Quote> key = new Key<Quote>(Quote.class, QuoteUtils.quoteId(symbol, date));
        return ofy().get(key);
    }

    public Quote get(Fund fund, LocalDate date) {
        return get(fund.getSymbol(), date);
    }

}
