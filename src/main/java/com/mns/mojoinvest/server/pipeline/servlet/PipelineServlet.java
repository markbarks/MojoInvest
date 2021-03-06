package com.mns.mojoinvest.server.pipeline.servlet;

import com.google.appengine.tools.pipeline.PipelineService;
import com.google.appengine.tools.pipeline.PipelineServiceFactory;
import com.google.inject.Singleton;
import com.mns.mojoinvest.server.pipeline.DailyPipeline;
import com.mns.mojoinvest.server.servlet.params.ParameterParser;
import org.joda.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@Singleton
public class PipelineServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(PipelineServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        ParameterParser parser = new ParameterParser(req);
        LocalDate date = parser.getLocalDateParameter("date", new LocalDate());
        String sessionId = parser.getStringParameter("sessionid", null);
        boolean funds = parser.getBooleanParameter("funds", true);
        boolean quotes = parser.getBooleanParameter("quotes", true);

        PipelineService service = PipelineServiceFactory.newPipelineService();
        String pipelineId = service.startNewPipeline(new DailyPipeline(), date, sessionId, funds, quotes);

        String msg = "Daily pipeline '" + pipelineId + "' started for date " + date + "<br/>" +
                "<a href=\"/_ah/pipeline/status.html?root=" + pipelineId + "\" target=\"_blank\">Pipeline Console</a>";
        log.info(msg);

        resp.setContentType("text/html");
        resp.getWriter().println(msg);
    }
}
