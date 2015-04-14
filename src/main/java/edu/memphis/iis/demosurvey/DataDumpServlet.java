package edu.memphis.iis.demosurvey;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is our extremely simple data display servlet. Note that since we aren't
 * implementing any kind of login functionality, we just require a hard-coded
 * "display code". This is a really bad idea for any kind of serious application
 */
@WebServlet(value="/datadump", loadOnStartup=2)
public class DataDumpServlet extends BaseServlet {
    private static final long serialVersionUID = -9170614145639941319L;

    private static final Logger logger = LoggerFactory.getLogger(DataDumpServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        logger.info("Data Dump screen being shown");
        doView(req, resp, "/WEB-INF/view/datadump.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        logger.info("Data Dump request made");

        //They must type the correct code
        String code = getStrParm(req, "datacode");
        if (!code.equals("show me the data")) {
            logger.error("Invalid attempt to display data");
            doError(req, resp, "Sorry, no data for you.");
            return;
        }

        req.setAttribute("surveys", new DataStoreClient().findSurveys());
        doView(req, resp, "/WEB-INF/view/datadump.jsp");
    }
}
