package es.udc.pojo.servjsptutorial.web.util;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebUtil {

	public final static void forwardTo(HttpServletRequest request,
			HttpServletResponse response, String url) throws IOException,
			ServletException {

		RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);

		requestDispatcher.forward(request, response);

	}

}
