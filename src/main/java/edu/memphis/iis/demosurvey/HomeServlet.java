package edu.memphis.iis.demosurvey;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//TODO: error page for actual errors
//TODO: readme documentation

@WebServlet(value="/home")
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 8127525026229258742L;

	private static final Logger logger = LoggerFactory.getLogger(HomeServlet.class);

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
		//TODO: handle post data
		doRedirect(req, resp, "/home");
	}

	protected void doView(HttpServletRequest req, HttpServletResponse resp, String view) {
		try {
			logger.info("Showing view {} (for {})", currentPath(req), view);
			req.getRequestDispatcher(view).forward(req, resp);
		}
		catch (IOException e) {
			logger.error("IO Error while processing " + currentPath(req), e);
		}
		catch (ServletException e) {
			logger.error("Servlet Error while processing " + currentPath(req), e);
		}
	}

	protected void doRedirect(HttpServletRequest req, HttpServletResponse resp, String url) {
		logger.info("Redirecting from %s to %s", currentPath(req), url);
		resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        resp.setHeader("Location", url);
	}

	protected String currentPath(HttpServletRequest req) {
		return String.format(
			"%s%s%s",
			Utils.defStr(req.getContextPath()),
			Utils.defStr(req.getServletPath()),
			Utils.defStr(req.getPathInfo())
		);
	}
}
