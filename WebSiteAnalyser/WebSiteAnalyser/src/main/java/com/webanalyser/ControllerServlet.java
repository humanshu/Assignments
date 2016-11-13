package com.webanalyser;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author Humanshu
 * Act as controller class, retrived the url entered in the form, send and delegate the retreived parameter and 
 * request to UrlAnalyser class.
 *
 */
public class ControllerServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1031422249396784970L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		resp.setContentType("text/html");		
		PrintWriter out = resp.getWriter();
		out.print("Called Servlet GET method!");
		out.flush();
		out.close();
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			
   	    // read form fields
        String enteredText = req.getParameter("searchAnalyser");
        // extract html page data using UrlAnalyser
        UrlAnalyser test = new UrlAnalyser();        
        String htmlRespone =test.startUrlAnalyser(enteredText);         
        // get response writer
        PrintWriter writer = resp.getWriter();         
        // return response
        writer.println(htmlRespone);
	}
}
	
	