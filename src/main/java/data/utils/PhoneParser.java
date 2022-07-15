package data.utils;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lt.laimis.mariadb.CompanyInfoDAO;
import lt.laimis.mariadb.PhoneNumberDAO;
import lt.laimis.mariadb.RelationLinkDAO;

public class PhoneParser {
	
	static int counter = 0;
	static int counter1 = 0;
	static int counter2 = 0;
	
	public static void main(String[] args) {
		
		PhoneParser parser = new PhoneParser();
		parser.parsePhoneNumber();
	}

	public void parsePhoneNumber() {

		try {

			List<HashMap<String, Object>> companys = CompanyInfoDAO.selectAll();

			for (int a = 0; a < companys.size(); a++) {
				
				counter1 ++;

				HashMap<String, Object> company = companys.get(a);

				String companyUrl = (String) company.get("company_url");
				Integer status = (Integer) company.get("status");
				
				if(status == 125 || status == 126 || companyUrl == null) {
					
					continue;
					
				}

				if (companyUrl.contains("abalt.lt")) {

					//counter2 ++ ;
					//parseAbalLTPhone(company);
					continue;

				} else if(companyUrl.contains("info.lt")) {
					
					counter2 ++ ;
					parseInfoLTPhone(company);
					continue;
					
				} else if(companyUrl.contains("rekvizitai.")) {
					
					//counter2 ++ ;
					//parseRekvizitaiLTPhone(company);
					continue;
					
				} else if(companyUrl.contains("visalietuva.lt")) {
					
					//counter2 ++ ;
					//parseInfoLTPhone(company);
					continue;
					
				} else if(companyUrl.contains("imones.lt")) {
					
					//counter2 ++ ;
					//parseInfoLTPhone(company);
					continue;
					
				} else if(companyUrl.contains("medicina.lt")) {
					
					//counter2 ++ ;
					//parseInfoLTPhone(company);
					continue;
					
				}    	
				
				else {
					
					// System.out.println(companyUrl);
				}

			}
			
			 System.out.println("counter: " + counter);
			 System.out.println("counter1: " + counter1);
			 System.out.println("counter2: " + counter2);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	private static void parseRekvizitaiLTPhone(HashMap<String, Object> company) {

		Long companyId = (Long) company.get("id");

		String rawContacts = (String) company.get("raw_contacts");
		String log = "";
		
		System.out.println(rawContacts);
		
	    Pattern p = Pattern.compile("[(][0-9]\\s[0-9]*[)][\\s]*[0-9]{5}");

	    Matcher matcher = p.matcher(rawContacts);
	    
	    String phoneNumber = "";
	    
	    while(matcher.find()){
	        phoneNumber = matcher.group();
	    }
	    
	    if(phoneNumber.length() == 0) {
	    	 saveAsStatus125(companyId);
	    	 return;
	    } else {	
	    	phoneNumber = replaceCharacters(phoneNumber);
	    	counter ++;
	    }
	    
		log = saveData(phoneNumber, companyId, log);
    	System.out.println(log);
	}
	
	private static void parseInfoLTPhone(HashMap<String, Object> company) {

		Long companyId = (Long) company.get("id");

		String rawContacts = (String) company.get("raw_contacts");
		String log = "";
		
	    Pattern p = Pattern.compile("[(][0-9]\\s[0-9]*[)][\\s]*[0-9]{5}");

	    Matcher matcher = p.matcher(rawContacts);
	    
	    String phoneNumber = "";
	    
	    while(matcher.find()){
	        phoneNumber = matcher.group();
	    }
	    
	    if(phoneNumber.length() == 0) {
	    	 saveAsStatus125(companyId);
	    } else {
	    	
	    	phoneNumber = replaceCharacters(phoneNumber);
	    	counter ++;
	    }
	    
	    log = saveData(phoneNumber, companyId, log);
	    System.out.println(log);
	}

	private static void parseAbalLTPhone(HashMap<String, Object> company) {

		Long companyId = (Long) company.get("id");

		String rawContacts = (String) company.get("raw_contacts");
		String log = "";
		
	    Pattern p = Pattern.compile("[(][0-9]\\s[0-9]*[)][\\s]*[0-9]{5}");

	    Matcher matcher = p.matcher(rawContacts);
	    
	    String phoneNumber = "";
	    
	    while(matcher.find()){
	        phoneNumber = matcher.group();
	    }
	    
	    if(phoneNumber.length() == 0) {
	    	 saveAsStatus125(companyId);
	    } else {
	    	
	    	phoneNumber = replaceCharacters(phoneNumber);
	    	counter ++;
	    }
	    
	    log = saveData(phoneNumber, companyId, log);
	    System.out.println(log);

	}
	
	private static String saveData(String phoneNumber, Long companyId, String log) {
		if(!PhoneNumberDAO.isPhoneNumberExist(phoneNumber)){
			log = log + " , PN not found in db";
			Long phoneId = PhoneNumberDAO.insert(phoneNumber, "", false);
			log = log + " , insert PN, phoneId=" + phoneId;
		 	RelationLinkDAO.insert(companyId, 0L, 0L, phoneId, 0L);
		 	log = log + " , insert RL";	 	
		} else {
			log = log + " , PN found in db";
			List<HashMap<String, Object>> per = PhoneNumberDAO.seachInPhoneNumbers(phoneNumber);
			if(per != null && per.size() > 0) {
				log = log + " , per.size() > 0";
				HashMap<String, Object> phone = per.get(0);
				Long phoneId = (Long) phone.get("id");
				log = log + " , phoneId = " + phoneId;
	    		if(!RelationLinkDAO.isExist(companyId, null, null, phoneId, null)) {
	    			log = log + " , RL not found in db";
	    			RelationLinkDAO.insert(companyId, 0L, 0L, phoneId, 0L);
	    			log = log + " , insert RL";
	    		} else {
	    			log = log + " , RL found in db";
	    		}	
			}
		};
		return log;
	}

	// Phone not found
	private static void saveAsStatus125(Long companyId) {
		CompanyInfoDAO.updateStatus(companyId.intValue(), 125);
	}
	// Phone data incorrect
	private static void saveAsStatus126(Long companyId) {
		CompanyInfoDAO.updateStatus(companyId.intValue(), 126);
	}
	
	public static String replaceCharacters(String phoneNumber) {
		phoneNumber = phoneNumber.replace(" ", "");
		phoneNumber = phoneNumber.replace("(", "");
		phoneNumber = phoneNumber.replace(")", "");
		phoneNumber = phoneNumber.replace("+", "");
		phoneNumber = phoneNumber.trim();
		return phoneNumber;
	}
}
