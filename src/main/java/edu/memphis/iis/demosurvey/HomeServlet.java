package edu.memphis.iis.demosurvey;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO: need data view page
//TODO: readme documentation


@WebServlet(value="/home", loadOnStartup=1)
public class HomeServlet extends BaseServlet {
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

            //TODO: check and don't overwrite previous survey

            new DataStoreClient().saveSurvey(survey);
        }
        catch(Exception e) {
            //Nothing else we can do - show an error page and quit
            doError(req, resp, "There was an issue with your survey responses", e);
            return;
        }

        //All done - if we're here they should go to the thanks page
        doRedirect(req, resp, req.getContextPath() + "/thanks");
    }
}
