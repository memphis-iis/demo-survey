package edu.memphis.iis.demosurvey;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//TODO: logging and error page for actual errors
//TODO: model
//TODO: unit testing
//TODO: readme documentation

@WebServlet(value="/home")
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 8127525026229258742L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException 
	{
		doView(req, resp, "/WEB-INF/view/home.jsp");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException 
	{
		//TODO: handle post data
		doRedirect(req, resp, "/home");
	}
	
	protected void doView(HttpServletRequest req, HttpServletResponse resp, String view) {
		try {
			req.getRequestDispatcher(view).forward(req, resp);
		}
		catch (IOException e) {
			//TODO: real error handling
			e.printStackTrace();
		}
		catch (ServletException e) {
			//TODO: real error handling
			e.printStackTrace();
		}
	}
	
	protected void doRedirect(HttpServletRequest req, HttpServletResponse resp, String url) {
		resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        resp.setHeader("Location", url);
	}
}
