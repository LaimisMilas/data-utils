package data.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import lt.mariadb.CompanyInfoDAO;

public class WebSiteParser {

	public static int index = 0;
	public static boolean parser = true;
	public static boolean storeResult = true;

	public static void del() {
		// selects all from company_info
		List<HashMap<String, Object>> items = CompanyInfoDAO.selectAll();

		if (items != null) {

			for (HashMap<String, Object> item : items) {

				Integer id = (Integer) item.get("id");
				// String companyCode = (String) item.get("company_code");
				String rawContacts = (String) item.get("raw_contacts");
				
				if (parser) {

					String webSiteUrl = parseWebSiteUrl(item);
					
					if (storeResult) {
						
						if (webSiteUrl != null 
								&& !(webSiteUrl.trim().length() == 0)
								&& !CompanyInfoDAO.findByWebSiteUrl(webSiteUrl)) {

							int result = CompanyInfoDAO.updateWebSiteUrl(id, webSiteUrl);

							System.out.println("Result: " + result);

						}
					}	
					

				}
			}
		}
	}
	
	public static List<String> ignorDomains = new ArrayList<String>();
	
	static {
		ignorDomains.add("gmail.com");
		ignorDomains.add("yahoo.com");
		ignorDomains.add("mail.lt");
		ignorDomains.add("mail.ru");
		ignorDomains.add("one.lt");
		ignorDomains.add("takas.lt");
		ignorDomains.add("inbox.lt");
		ignorDomains.add("yandex.com");
		ignorDomains.add("delfi.lt");
		ignorDomains.add("gmai.com");
		ignorDomains.add("hotmail.com");
		ignorDomains.add("email.lt");
		ignorDomains.add("ONE.lt");
		ignorDomains.add("info.lt");
		ignorDomains.add("zebra.lt");
		ignorDomains.add("balticum-tv.lt");
		ignorDomains.add("list.ru");
		ignorDomains.add("yahoo.de");
		ignorDomains.add("hotmail.lt");
		ignorDomains.add("omni.lt");
	}
	
	private static boolean isInBlackList(String domain) {
		return ignorDomains.contains(domain);
	}
	
	private static int countWebSites = 0;

	private static String parseWebSiteUrl(HashMap<String, Object> company) {
		
		boolean www = true;
		boolean el = false;
		boolean tokenize = false;
		
		Integer id = (Integer) company.get("id");
		String companyCode = (String) company.get("company_code");
		String rawContacts = (String) company.get("raw_contacts");
		String company_url = (String) company.get("company_url");
		company_url = "";
		String title = (String) company.get("title");
		String email = (String) company.get("email");
		
		
		String wwwURl = "";
		
		if(www && rawContacts.contains("www.")) {
			
			String[] tmp = rawContacts.split("www."); 

			//System.out.println(tmp[1]);
			
			tmp = tmp[1].split("\n");
			
			System.out.println(countWebSites + " " + tmp[0]);
			
			wwwURl = tmp[0];
			
			countWebSites ++;

		} else {
			
			if(tokenize) {			
				wwwURl = parseByToken(rawContacts);	
			}
		}
		
		if(el && rawContacts.contains("@")) {
			
			String[] tmp = rawContacts.split("@"); 
			
			tmp = tmp[1].split("\n");
			
			if(!isInBlackList(tmp[0])) {
				
				System.out.println(
						"url: " + tmp[0] + 
						" title: " + title + 
						", URL: " + company_url +
						", wwwURl: " + wwwURl
						);
			}
		}

		return wwwURl;
	}

	private static String parseByToken(String rawContacts) {
		
		String result = "";
		
		StringTokenizer st = new StringTokenizer(rawContacts);  
		 while (st.hasMoreTokens()) {
			 
			 String text = st.nextToken();
			 
			 if(!ignorWords.contains(text)) {
				// System.out.println(text); 
				 if(!text.contains("@")) {
					 if(text.contains("http")) {
						 //System.out.println(text); 
					 }
					 if(text.contains(".")) {
						 System.out.println(text); 
					 }
					 if(text.contains("//")) {
						 //System.out.println(text);
						 // apkmetalrecycling.lt
						 // regitros.
					 }
				 }
			 }
		 }
		 
		 return result;
	}
	
	public static String ignorWords = "Įmonės kontaktai Telefonas (8 652) 82260"
			+ "El. paštas Direktorius Oleg Sokolov Įmonės kodas automagistralių"
			+ "al. mstl., SAV. SEN., M. I. J. H. SEN. pl. kab., a. pr. nr. El. p."
			+ "Z. g. g., S. Tel. nr. g. m. m., sav. R. K. K., k., http://iranga-slaugai.business.site"
			+ "http://- http://facebook:muzgost "
			+ "http://interactio.io"
			+ "http://facebook:muzgost"
			+ "";

	public static void main(String[] args) {
		
		WebSiteParser.parser = true;
		WebSiteParser.del();
		
	}

}
