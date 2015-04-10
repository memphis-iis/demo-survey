package edu.memphis.iis.demosurvey;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Our base class for other servlets - we provide some fairly simple
 * helper methods for rendering views and such
 */
public class BaseServlet extends HttpServlet {
    private static final long serialVersionUID = -1468638650859897366L;

    private static final Logger logger = LoggerFactory.getLogger(BaseServlet.class);

    protected void doView(HttpServletRequest req, HttpServletResponse resp, String view) {
        try {
            logger.info("Showing view {} (for {})", currentPath(req), view);
            req.getRequestDispatcher(view).forward(req, resp);
        }
        catch (Exception e) {
            logger.error("Error while processing " + currentPath(req), e);

        }
    }

    protected void doRedirect(HttpServletRequest req, HttpServletResponse resp, String url) {
        logger.info("Redirecting from {} to {}", currentPath(req), url);
        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        resp.setHeader("Location", url);
    }

    protected void doError(HttpServletRequest req, HttpServletResponse resp, String mainText, Exception e) {
        try {
            req.setAttribute("errorMessage", mainText);
            req.setAttribute("errorDetails", e.toString());
            req.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(req, resp);
        }
        catch(Exception esub) {
            logger.error("ERROR rendering error page for previous error", esub);
        }
    }

    protected String currentPath(HttpServletRequest req) {
        return String.format(
            "%s%s%s",
            Utils.defStr(req.getContextPath()),
            Utils.defStr(req.getServletPath()),
            Utils.defStr(req.getPathInfo())
        );
    }

    protected String getStrParm(HttpServletRequest request, String name) {
        String val = request.getParameter(name);
        if (Utils.isBlankString(val))
            val = "";
        return val;
    }
}
