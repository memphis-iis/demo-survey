package edu.memphis.iis.demosurvey;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Our simple thank-you controller
 */
@WebServlet(value="/thanks", loadOnStartup=2)
public class ThanksServlet extends BaseServlet {
    private static final long serialVersionUID = -9027837087270482689L;

    private static final Logger logger = LoggerFactory.getLogger(ThanksServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        logger.info("Thanking our participant!");
        doView(req, resp, "/WEB-INF/view/thanks.jsp");
    }
}
