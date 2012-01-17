package com.mns.mojoinvest.server.engine.execution;

import com.google.inject.Inject;
import com.mns.mojoinvest.server.engine.model.Fund;
import com.mns.mojoinvest.server.engine.model.Quote;
import com.mns.mojoinvest.server.engine.model.dao.QuoteDaoImpl;
import com.mns.mojoinvest.server.engine.portfolio.Portfolio;
import com.mns.mojoinvest.server.engine.portfolio.PortfolioException;
import com.mns.mojoinvest.server.engine.portfolio.Position;
import com.mns.mojoinvest.server.engine.transaction.BuyTransaction;
import com.mns.mojoinvest.server.engine.transaction.SellTransaction;
import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.util.logging.Logger;

public class NextTradingDayExecutor implements Executor {

    private static final Logger log = Logger.getLogger(NextTradingDayExecutor.class.getName());

    private QuoteDaoImpl quoteDao;

    @Inject
    public NextTradingDayExecutor(QuoteDaoImpl quoteDao) {
        this.quoteDao = quoteDao;
    }

    @Override
    public void buy(Portfolio portfolio, Fund fund, LocalDate date, BigDecimal allocation)
            throws PortfolioException {
        //TODO: getRanking execution price should be mid between open and close
        Quote executionQuote = quoteDao.get(fund, date);
        BigDecimal shares = allocation.divide(executionQuote.getClose(), 0, BigDecimal.ROUND_DOWN);
        BuyTransaction tx = new BuyTransaction(fund, date, shares,
                executionQuote.getClose(), portfolio.getTransactionCost());
        portfolio.add(tx);
    }

    @Override
    public void sellAll(Portfolio portfolio, Fund fund, LocalDate date)
            throws PortfolioException {
        //TODO: getRanking execution price should be mid between open and close
        Quote executionQuote = quoteDao.get(fund, date);
        Position position = portfolio.getPosition(fund);
        SellTransaction tx = new SellTransaction(fund, date, position.shares(date),
                executionQuote.getClose(), portfolio.getTransactionCost());
        portfolio.add(tx);
    }
}
