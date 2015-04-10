package edu.memphis.iis.demosurvey;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO: need thank-you page
//TODO: need data view page
//TODO: readme documentation


@WebServlet(value="/home", loadOnStartup=1)
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 8127525026229258742L;

    private static final Logger logger = LoggerFactory.getLogger(HomeServlet.class);

    @Override
    public void init() throws ServletException {
        super.init();
        new DataStoreClient().ensureSchema();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        logger.info("GET {}", currentPath(req));

        //Set any data our view will need to render
        Survey survey = new Survey(); //Just use default values
        req.setAttribute("survey", survey);

        //Render the view we want
        doView(req, resp, "/WEB-INF/view/home.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        logger.info("POST {}", currentPath(req));

        try {
            Survey survey = new Survey();
            survey.participantCode = getStrParm(req, "participantCode");
            survey.favoriteDogBreed = getStrParm(req, "favoriteDogBreed");
            survey.catLover = getStrParm(req, "catLover").length() > 0;
            survey.favoriteNumber = Integer.parseInt(getStrParm(req, "favoriteNumber"));

            if (!survey.isValid()) {
                throw new IllegalAccessException("The survey is not valid");
            }

            new DataStoreClient().saveSurvey(survey);
        }
        catch(Exception e) {
            doError(req, resp, "There was an issue with your survey responses", e);
        }

        //TODO: send to thanks page
        doRedirect(req, resp, req.getContextPath() + "/home");
    }

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

    private String getStrParm(HttpServletRequest request, String name) {
        String val = request.getParameter(name);
        if (Utils.isBlankString(val))
            val = "";
        return val;
    }
}
