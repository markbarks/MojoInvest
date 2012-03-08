package com.mns.mojoinvest.server.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mns.mojoinvest.server.engine.model.Quote;
import com.mns.mojoinvest.server.engine.model.dao.QuoteDao;
import com.mns.mojoinvest.server.servlet.util.ParameterNotFoundException;
import com.mns.mojoinvest.server.servlet.util.ParameterParser;
import org.joda.time.LocalDate;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class QuoteViewerServlet extends HttpServlet {

    private final QuoteDao dao;

    @Inject
    public QuoteViewerServlet(QuoteDao dao) {
        this.dao = dao;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        ParameterParser parser = new ParameterParser(req);

        List<Quote> quotes = new ArrayList<Quote>();
        String symbol = null;
        LocalDate date = null;
        try {
            symbol = parser.getStringParameter("symbol");
            date = parser.getLocalDateParameter("date");
        } catch (ParameterNotFoundException e) {
            //pass
        }
        if (symbol != null && date != null) {
            quotes.add(dao.get(symbol, date));
        } else if (symbol != null) {
            quotes.addAll(dao.query(symbol));
        } else if (date != null) {
            quotes.addAll(dao.query(date));
        } else {
            //don't want to do this
            //quotes = dao.list();
        }
        resp.setContentType("text/html");
        resp.getWriter().println("<ul>");
        for (Quote quote : quotes) {
            resp.getWriter().println("<li>" + quote + ", close=" + quote.getClose() + ", rolled=" + quote.isRolled() + "</li>");
        }

        resp.getWriter().println("</ul>");
    }
}
