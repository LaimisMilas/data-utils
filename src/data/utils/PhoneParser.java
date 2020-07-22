package data.utils;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lt.mariadb.CompanyInfoDAO;
import lt.mariadb.PhoneNumberDAO;
import lt.mariadb.RelationLinkDAO;

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

					//parseAbalLTPhone(company);

					continue;

				} else if(companyUrl.contains("info.lt")) {
					
					//counter2 ++ ;
					
					//parseInfoLTPhone(company);
					
					continue;
					
				} else if(companyUrl.contains("rekvizitai.")) {
					
					counter2 ++ ;
					//System.out.println(companyUrl);
					parseRekvizitaiLTPhone(company);
					
					continue;
					
				} else if(companyUrl.contains("visalietuva.lt")) {
					
					//counter2 ++ ;
					
					//parseInfoLTPhone(company);
					//System.out.println(companyUrl);
					continue;
					
				} else if(companyUrl.contains("imones.lt")) {
					
					//counter2 ++ ;
					//System.out.println(companyUrl);
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
	        //System.out.print(matcher.group() + ", ");
	        phoneNumber = matcher.group();
	    }
	    
	    if(phoneNumber.isEmpty()) {
	    	 //System.out.println(rawContacts);
	    	 //saveAsStatus125(companyId);
	    	 return;
	    } else {	
	    	phoneNumber = replaceCharacters(phoneNumber);
	    	//System.out.print(phoneNumber + ", ");
	    	counter ++;
	    }
	    
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
		
		// imones kodui  \s[0-9]{9}\s
		// PVM kodas \sLT[0-9]+\s
		// "(8 345)"   [(][0-9]\s[0-9]*[)]
		// " (8 345) 12345 " \s[(][0-9]\s[0-9]*[)][\s]*[0-9]{5}
		// "834512345" [8][0-9]{8}

		//(\D)?(?(1)\D)
    	System.out.println(log);
	}
	
	private static void parseInfoLTPhone(HashMap<String, Object> company) {

		Long companyId = (Long) company.get("id");

		String rawContacts = (String) company.get("raw_contacts");
		String log = "";
		
		//System.out.println(rawContacts);
		
	    Pattern p = Pattern.compile("[(][0-9]\\s[0-9]*[)][\\s]*[0-9]{5}");

	    Matcher matcher = p.matcher(rawContacts);
	    
	    String phoneNumber = "";
	    
	    while(matcher.find()){
	        //System.out.print(matcher.group() + ", ");
	        phoneNumber = matcher.group();
	    }
	    
	    if(phoneNumber.isEmpty()) {
	    	 System.out.println(rawContacts);
	    	 saveAsStatus125(companyId);
	    } else {
	    	
	    	phoneNumber = replaceCharacters(phoneNumber);
	    	//System.out.print(phoneNumber + ", ");
	    	counter ++;
	    }
	    
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
		
		// imones kodui  \s[0-9]{9}\s
		// PVM kodas \sLT[0-9]+\s
		// "(8 345)"   [(][0-9]\s[0-9]*[)]
		// " (8 345) 12345 " \s[(][0-9]\s[0-9]*[)][\s]*[0-9]{5}
		// "834512345" [8][0-9]{8}

		//(\D)?(?(1)\D)
    	System.out.println(log);
	}

	private static void parseAbalLTPhone(HashMap<String, Object> company) {
		String rawContacts = (String) company.get("raw_contacts");
		Long companyId = (Long) company.get("id");
		String companyCode = (String) company.get("company_code");
		
	    if (!rawContacts.isEmpty() && rawContacts.contains("Tel. nr.")) {
	    	String[] tmp = rawContacts.split("Tel. nr.");
	    	String phoneNumber = "";
	    	if(rawContacts.contains("El.")) {
	            tmp = tmp[1].split("El.");
	            phoneNumber = tmp[0];
	        } else if (rawContacts.contains("Veiklos sritis")) {
	            tmp = tmp[1].split("Veiklos sritis");
	            if(tmp[0].contains("Tinklapis")) {
	                tmp = tmp[0].split("Tinklapis");
	                phoneNumber = tmp[0];
	            } else {
	            	phoneNumber = tmp[0];
	            }
	        } else if (rawContacts.contains("Tinklapis")) {
	            tmp = tmp[1].split("Tinklapis");
	            phoneNumber = tmp[0];
	        } else {
	        	phoneNumber = tmp[1];
	        }
	    	
	    	phoneNumber = phoneNumber.trim();
	    	
	    	String log = phoneNumber ;
	    	
	    	if(phoneNumber.length() != 9) {
	    		
	    		log = log + " != 9, ";
        		phoneNumber = replaceCharacters(phoneNumber);
        		log = log + phoneNumber;
        		
	    		if(phoneNumber.length() < 9) {
	    			log = log + ", leng < 9";
        			if(phoneNumber.length() == 8 && phoneNumber.contains("846")) {
        				log = log + ", leng == 8 && contains(\"846\"), "+ phoneNumber ;
	        		} else {
	        			phoneNumber = "8" + phoneNumber;
	        			log = log + ", " + phoneNumber;
	        			if(phoneNumber.length() < 9) {
	        				log = log + ", length() < 9, 126 ";
	        			}
	        		}
        			
        		} else if(phoneNumber.length() > 9) {
        			log = log + ", leng > 9";
        			if(phoneNumber.contains("370")) {
        				log = log + ", contains(\"370\"), ";
        				phoneNumber = phoneNumber.replace("370", "8");
        				log = log + phoneNumber;
        				if(phoneNumber.length() > 9) {
        					log = log + ", leng > 9";
        					if(phoneNumber.contains(";")) {
        						log = log + ", contains(\";\") ";
        						phoneNumber = phoneNumber.split(";")[0];
        						log = log + phoneNumber; 
        						if(phoneNumber.length() == 9) {
        							log = log + ", leng = 9";
        						} else {
        							log = log + ", leng != 9";
        							log = log + ", 126";
        						}
        					} else {
        						log = log + ", 126";
        					}
        					
            			}
        				
        			} else {
        				if(phoneNumber.contains(";")) {
        					log = log + ", contains(\";\") ";
        					String[] aTem = phoneNumber.split(";");
        					phoneNumber = aTem[0];
        					log = log + phoneNumber;
        				} else {
        					log = log + ", 126";
        				}
        			}
        		}
        	}
	    	
	    	log = log + " = " + phoneNumber.length();
	    	
	    	if(log.contains("126")) {
	    		saveAsStatus126(companyId);
	    		log = log + " saveAsStatus126 ";
	    		return;
	    	}
	    	
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
	    	
	    	System.out.println(log);
	    	
	    } else {
	        saveAsStatus125(companyId);
	    }
	    
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
