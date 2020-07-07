package data.utils;

import java.util.HashMap;
import java.util.List;

import lt.mariadb.CompanyInfoDAO;
import lt.mariadb.PhoneNumberDAO;
import lt.mariadb.RelationLinkDAO;

public class PhoneParser {
	
	public static void main(String[] args) {
		
		PhoneParser parser = new PhoneParser();
		parser.parsePhoneNumber();
	}

	public void parsePhoneNumber() {

		try {

			List<HashMap<String, Object>> companys = CompanyInfoDAO.selectAll();

			for (int a = 0; a < companys.size(); a++) {

				HashMap<String, Object> company = companys.get(a);

				String companyUrl = (String) company.get("company_url");
				Integer status = (Integer) company.get("status");
				

				if (status != 125 && status != 126 &&  companyUrl != null && companyUrl.contains("abalt.lt")) {

					parseAbalLTPhone(company);

					continue;

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void parseInfoLTEmail(HashMap<String, Object> company) {

		Integer id = (Integer) company.get("id");

		String rawContacts = (String) company.get("raw_contacts");

		String email = (String) company.get("email");

		if (email.trim().length() == 0) {

			String[] tmp = rawContacts.split("@");

			if (tmp.length > 1) {

				String userl = tmp[0].split("El. paštas")[1].trim();

				String domen = tmp[1].split("\n")[0].trim();

				CompanyInfoDAO.updateEmail(id, userl + "@" + domen);

				System.out.println(userl + "@" + domen + " id=" + id);

			}

		}

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
	    		
	    		phoneNumber = phoneNumber.replace(" ", "");
	    		phoneNumber = phoneNumber.replace("(", "");
        		phoneNumber = phoneNumber.replace(")", "");
        		phoneNumber = phoneNumber.replace("+", "");
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
}
