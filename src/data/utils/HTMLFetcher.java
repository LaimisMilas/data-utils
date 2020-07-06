package data.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;
import java.io.*;
import lt.mariadb.DomainNameDAO;
import lt.mariadb.CompanyInfoDAO;
import utils.Utils;

public class HTMLFetcher {

	public static int index = 0;
	public static boolean parser = true;
	public static boolean storeResult = true;
	public static boolean fetch = true;
	public static int sleep = 2000;
	private static boolean LOGGING = false;
	
	public static String ignorWords = "maerskline.com  csc-crewing.com interactio.io";

	public static void doStuff() {
		// selects all from company_info
		List<HashMap<String, Object>> items = CompanyInfoDAO.selectAll();

		if (items != null) { 

			for (HashMap<String, Object> item : items) {
				
				protocol = "";

				Integer id = (Integer) item.get("id");
				String companyCode = (String) item.get("company_code");
				// String rawContacts = (String) item.get("raw_contacts");
				String webSiteUrl = (String) item.get("web_site_url");
				String fetchResult = "";
				
				if(webSiteUrl != null && ignorWords.contains(webSiteUrl)){
					continue;
				}

				if (DomainNameDAO.findByDoaminName(webSiteUrl)) {
					continue;
				}

				if (fetch && webSiteUrl != null && !webSiteUrl.trim().isEmpty()) {

					Utils.sleepThread(sleep);

					fetchResult = fetchHTML(webSiteUrl);
				}

				if (parser) {

					String parseLog = parseWebSiteHTML(fetchResult, webSiteUrl);

					int status = parseStatus(parseLog);

					if (webSiteUrl != null && storeResult) {

						if (!DomainNameDAO.findByDoaminName(webSiteUrl)) {

							int result = DomainNameDAO.insert(webSiteUrl, status, parseLog, fetchResult, protocol, companyCode);

							// System.out.println("Result after domain insert: " + result);

						} else {

							// int result = DomainNameDAO.update(id, webSiteUrl, status, parseLog,
							// fetchResult);

							// System.out.println("Result after domain update: no updates");
						}
					}
				}
				index++;
				// System.out.println("i:" + index + " from: " + items.size());
			}
		}
	}

	private static int parseStatus(String parseLog) {

		int result = -1;

		if (parseLog.contains("null")) {
			result = 0;
		}
		if (result == -1 && parseLog.contains("empty_context")) {
			result = 1;
		}
		if (parseLog.contains("This_based_on_HTML4")) {
			result = 2;
		}
		if (parseLog.contains("viewport")) {
			result = 3;
		}
		if (result == -1 && parseLog.contains("This_based_on_HTML5")) {
			result = 4;
		}
		if (parseLog.contains("Neegzistuoja_Serveriai.lt")) {
			result = 5;
		}

		System.out.println("i:" + index + " " + result + " " + parseLog);

		return result;
	}

	private static String parseWebSiteHTML(String fetchResult, String webSiteUrl) {

		String sysLog = webSiteUrl + " ";

		if (fetchResult != null && !fetchResult.trim().isEmpty()) {

			if (fetchResult.contains("<!DOCTYPE html>")) {

				sysLog = sysLog + " This_based_on_HTML5";

			} else {

				sysLog = sysLog + " This_based_on_HTML4 ";

			}

			if (fetchResult.contains("name=\"viewport\"")) {

				sysLog = sysLog + " viewport ";
			}

			if (fetchResult.contains("<title>Neegzistuoja - Serveriai.lt</title>")) {

				sysLog = sysLog + " Neegzistuoja_Serveriai.lt";
			}
			if (fetchResult.contains("<title>Neegzistuoja</title>") || 
					fetchResult.contains("hostingas.lt")) {

				sysLog = sysLog + " Neegzistuoja_Serveriai.lt";
			}

		} else {

			sysLog = sysLog + "empty_context";
		}

		// System.out.println(sysLog);

		return sysLog;
	}
	
	private static String protocol = "http";

	private static String fetchHTML(String webSiteUrl) {

		String content = null;
		boolean unknownHostException = false;
		String domainName = "";
		
		if(ignorWords.contains(webSiteUrl)){
			
			System.out.println(webSiteUrl);
			return content;
		}
			
		try {
			
			if (!webSiteUrl.contains("http")) {
				domainName = "http://wwww." + webSiteUrl;
			}

			content = fetchHTTP(domainName);
			
			protocol = "http";

		} catch (Exception e) {
			
			if(LOGGING) {
				e.printStackTrace();
			}
			
			unknownHostException = true;
		}
		
		if (unknownHostException) {

			try {
				
				if (!webSiteUrl.contains("http")) {
					domainName = "http://" + webSiteUrl;
				}
				
				content = fetchHTTP(domainName);
				unknownHostException = false;
				protocol = "http";
				
			} catch (Exception e) {
				
				if(LOGGING) {
					e.printStackTrace();
				}
				
				unknownHostException = true;
			}
		}

		if (unknownHostException) {

			content = fetchHTTPS(webSiteUrl);
		}

		// System.out.println(webSiteUrl);
		// System.out.println(content);

		return content;
	}

	private static String fetchHTTP(String url) throws MalformedURLException, IOException {

		String content = null;
		URLConnection conn = null;

		conn = new URL(url).openConnection();
		//conn.setConnectTimeout(3000);
		Scanner scanner = new Scanner(conn.getInputStream());
		scanner.useDelimiter("\\Z");
		content = scanner.next();
		scanner.close();

		return content.toString();
	}

	private static String fetchHTTPS(String webSiteUrl) {

		StringBuffer content = new StringBuffer();
		String domainName = "";

		try {

			if (!webSiteUrl.contains("http")) {
				domainName = "https://" + webSiteUrl;
			}

			URL myUrl = new URL(domainName);
			HttpsURLConnection conn = (HttpsURLConnection) myUrl.openConnection();
			conn.setConnectTimeout(3000);
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			
			protocol = "https";

		} catch (Exception e) {
			
			if(LOGGING) {
				e.printStackTrace();
			}
			
		}

		// System.out.println(webSiteUrl);
		// System.out.println(content);

		return content.toString();
	}

	// tinka kaip klientai:
	// http://wwww.akswest.lt/
	// makanada.lt empty content
	public static void main(String[] args) {
		
		HTMLFetcher.LOGGING = true;
		HTMLFetcher.doStuff();
		//doSingleFectch("maerskline.com");
		//doSingleFectch("makanada.lt");
		//doSingleFectch("geomodera.lt");
		//doSingleFectch("webmod.lt"); //saitas veikentis, contenas yra serveriai.lt(Neegzistuoja).
		//doSingleFectch("doppioshoes.com"); suteikia 5 statusa
	}
	
	public static void doSingleFectch(String domainName) {
		String fetchResult = HTMLFetcher.fetchHTML(domainName);
		String parseLog = HTMLFetcher.parseWebSiteHTML(fetchResult, domainName);
		int status = HTMLFetcher.parseStatus(parseLog);
		System.out.println(status);
	}

}
