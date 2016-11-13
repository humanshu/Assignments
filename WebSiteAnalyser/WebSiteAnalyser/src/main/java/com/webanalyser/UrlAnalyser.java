package com.webanalyser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

/**
 * @author Humanshu
 * Class is used to extract below mentioned fields using jsoup library and return the html table
 * filled with all the retreived values in the form of html string.
 * Following fields are retreived from html document:
 * 1. Html version
 * 2. Page title
 * 3. No of headings in html document
 * 4. internal, external and inaccesible links
 * 5. Check if html doc page contain loginForm or not.
 *
 */
public class UrlAnalyser {
	
	private StringBuffer htmlErrorMessage;
	/**
	 * Extract HtmlVersion from Html document using it's Doctype in header.
	 * @param doc
	 * @return HtmlVersion using Doctype string
	 */
	public String getHtmlVersion(Document doc) {
		String htmlVersionNotFound = "Doctype does not contain Html version.";
		String result = null;
		DocumentType documentType = null;
		List<Node> nods = doc.childNodes();
		for (Node node : nods) {
			if (node instanceof DocumentType) {
				documentType = (DocumentType) node;
			}
		}
		if (documentType != null)
			result = documentType.toString().replace("<", "").replace(">", "");
		if (documentType == null || result.isEmpty())
			return htmlVersionNotFound;
		else
			return result;
	}

	/**
	 * @param doc
	 * @return Html document page title
	 */
	public static String getPageTitle(Document doc) {
		return doc.title();
	}
	
	/**
	 * @param doc
	 * @return no of external links in html document
	 * @throws IOException
	 */
	public int getExternalLinks(Document doc) throws IOException {
		
		Elements externalLinks = doc.select("a[href]");
		int noOfExternalLinks = (externalLinks != null) ? externalLinks.size() : 0;
		return noOfExternalLinks;
	}
	
	/**
	 * @param doc
	 * @return no of internal links in html documen
	 * @throws IOException
	 */
	public int getInternalLinks(Document doc) throws IOException {
		
		Elements internalLinks = doc.select("link[href]");
		int noOfInternalLinks = (internalLinks != null) ? internalLinks.size() : 0;;
		return noOfInternalLinks;
	}
	
    /**
     * @param doc
     * @return no of inAccesibleLink links in html documen
     * @throws IOException
     */
    public int getInacessibleLinks(Document doc) throws IOException {
		int inAccesibleLink = 0;
    	Elements externalLinks = doc.select("a[href]");
		for (Element link : externalLinks) {
			if (link.ownText().isEmpty())
				inAccesibleLink++;
		}
		return inAccesibleLink;
	}
	
	/**
	 * Count headerId's for each level
	 * @param siteTest
	 * @param doc
	 * @throws IOException
	 * @return HashMap key:headerId, Value:no of times headerId occurs
	 */
	private Map<String, Integer> getHtagIds(Document doc)
			throws IOException {
		Map<String, Integer> hTagIdValuePair = new HashMap<String, Integer>();
		// Group of all h-Tags
		Elements hTags = doc.select("h1, h2, h3, h4, h5, h6");
		// create map for htag header ids as key and occurances as value
		for (Element tag : hTags) {
			if (hTagIdValuePair.containsKey(tag.tagName()))
				hTagIdValuePair.put(tag.tagName(),hTagIdValuePair.get(tag.tagName()) + 1);
			else
				hTagIdValuePair.put(tag.tagName(), tag.getAllElements().size());
		}
		
		return hTagIdValuePair;
	}
	
	/**
	 * Check whether html document contain loginpage or not
	 * @param doc
	 * @return Boolean (True if loginform exist otherwise false)
	 */
	public boolean isLoginFormExist(Document doc) {
		boolean checkLoginFormExist = false;
		Elements forms = doc.getElementsByTag("form");
		for (Element form : forms) {
			Attributes attributes = form.attributes();
			for (Attribute attribute : attributes) {
				if (attribute.toString().toLowerCase().contains("login")) {
					checkLoginFormExist = true;
					break;
				}
			}
			if (checkLoginFormExist == true)
				break;
		}
		return checkLoginFormExist;
	}
	
	/**
	 * Check if any error exist while retrieving html document
	 * @param siteUrl
	 * @return Document as null on error and add customized error message with Http error status code.
	 * OnSucess returns html document
	 * @throws IOException
	 */
	public Document validateSiteUrl(String siteUrl) throws IOException {
		Document doc = null;
		htmlErrorMessage = new StringBuffer();
		Connection.Response response = null;
		try {
			response = Jsoup
					.connect(siteUrl)
					.userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
					.timeout(10000).ignoreHttpErrors(false).execute();

			int statusCode = response.statusCode();
			if (statusCode == 200) {
				try {
					doc = Jsoup.connect(siteUrl).get();
				} catch (HttpStatusException e) {
					htmlErrorMessage.append("Error status:" + e.getStatusCode()
							+ "</h2><br>" + e.getMessage());
				}
			} else {
				htmlErrorMessage.append("Error status:" + statusCode);
			}
		} catch (IOException e) {
			htmlErrorMessage
					.append("Error status:500</h2>"
							+ "<br>"
							+ "The server encountered an internal error or misconfiguration and was unable to complete your request.");
		} catch (IllegalArgumentException e) {
			htmlErrorMessage.append("Error status:500</h2>" + "<br>"
					+ e.getMessage());
		} catch (Exception e) {
			htmlErrorMessage.append("Error:</h2>" + "<br>" + e.getMessage());
		}
		return doc;
	}

	/**
	 * Start UrlAnalyser and extract required values one by one from given html document
	 * @param siteTest
	 * @return html string which contains all the html document parameters in html table form
	 * @throws IOException
	 */
	public String startUrlAnalyser(String siteTest) throws IOException {
		boolean flagLoginFormExist = false;
		Document doc = validateSiteUrl(siteTest);
		String htmlVersion = "";
		String pageTite = "";
		Map<String, Integer> hTagIdCounterPair = null;
		int noOfExternalLinks = 0, noOfInternalLinks = 0,inAccesibleLink = 0;
		String htmlTableData = "";

		if (doc == null) {
			htmlTableData = "<html><body><h2>" //
					+ htmlErrorMessage //
					+ "</body></html>";
			return htmlTableData;
		} else {
			htmlVersion = getHtmlVersion(doc);
			pageTite = getPageTitle(doc);
			try {
				hTagIdCounterPair = getHtagIds(doc);
			} catch (IOException e) {
				hTagIdCounterPair = Collections.EMPTY_MAP;
				System.out.println("Error while retreiving htag header values from html document");
			}
			try {
				 noOfExternalLinks = getExternalLinks(doc);
				 noOfInternalLinks = getInternalLinks(doc);
				 inAccesibleLink = getInacessibleLinks(doc);
				
			} catch (IOException e) {
				System.out.println("Error while retreiving links from html document");
			}

			flagLoginFormExist = isLoginFormExist(doc);
		}

		//generate html string from the retreived values
		htmlTableData = fillHtmlDocFieldsTable(htmlVersion, pageTite, hTagIdCounterPair,
				noOfInternalLinks, noOfExternalLinks, inAccesibleLink,
				flagLoginFormExist);

		return htmlTableData;
	}

	/**
	 * Create and fill html table using values which are retrieved from html document.
	 * @param htmlVersion
	 * @param pageTite
	 * @param hTagIdValuePair
	 * @param noOfInternalLinks
	 * @param noOfExternalLinks
	 * @param inAccesibleLink
	 * @param flagLoginFormExist
	 * @return html string which contains all the html document parameters in html table form.
	 */
	private static String fillHtmlDocFieldsTable(String htmlVersion, String pageTite,
			Map<String, Integer> hTagIdValuePair, int noOfInternalLinks,
			int noOfExternalLinks, int inAccesibleLink,
			boolean flagLoginFormExist) {
		StringBuffer htmlTableData = new StringBuffer();
		
		// dynamically generate html string for headings depending on their level and occurrence in the html document
		Iterator<String> hTagkeysIterator = hTagIdValuePair.keySet().iterator();
		StringBuffer hTagFields = new StringBuffer();
		StringBuffer noOfTimesHTagFiedOccur = new StringBuffer();
		while (hTagkeysIterator.hasNext()) {
			String key = hTagkeysIterator.next();
			hTagFields.append("<td>" + key + "</td>");
			noOfTimesHTagFiedOccur
					.append("<td>" + hTagIdValuePair.get(key) + "</td>");
		}
		
		String checkLoginFormExist = flagLoginFormExist? "Yes":"No";

		htmlTableData
				.append("<html><link rel=\"stylesheet\" type=\"text/css\" href=\"table.css\"><head></head> <body>" //
						+ "<h2>Url Properties</h2>" //
						+ "<p>Below table contains all the fields extracted by UrlAnalyser:</p>");

		// fill table header row
		htmlTableData.append("<table id=" + "\"urlDataTable\" + border=\"1\" rules=\"all\" >" //
				+ "<tr style=\"font-weight:bold\">" //
				+ "<td>HtmlVersion</td>" //
				+ "<td>PageTitle</td>" //
				+ hTagFields //
				+ "<td>No Of InternalLinks</td>" //
				+ "<td>No Of ExternalLinks</td>" //
				+ "<td>InAccesible Links</td>" //
				+ "<td>LoginForm Exist</td>" //
				+ "</tr>");

		// fill header row fields values
		htmlTableData.append("<tr>" //
				+ "<td>" + htmlVersion + "</td>" //
				+ "<td>" + pageTite + "</td>" //
				+ noOfTimesHTagFiedOccur //
				+ "<td>" + noOfInternalLinks + "</td>" //
				+ "<td>" + noOfExternalLinks + "</td>" //
				+ "<td>" + inAccesibleLink + "</td>" //
				+ "<td>" + checkLoginFormExist + "</td>" //
				+ "</tr></table>");

		htmlTableData.append("</body></html>");

		return htmlTableData.toString();
	}
}
