package data.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import mariadb.DomainNameDAO;
import mariadb.InfoDAO;
import utils.Utils;

public class VisualWebPageView {
	
	private static WebDriver driver;
	
	public static void initFireFox() {

		driver = new FirefoxDriver();

	}
	
	
	public static void doJob() {
		
		List<HashMap<String, Object>> companys = getCompanys();

		for (int a = 0; a < companys.size(); a++) {

			HashMap<String, Object> company = companys.get(a);

			Integer id = (Integer) company.get("id");
			String companyCode = (String) company.get("company_code");
			String email = (String) company.get("email");
			Integer status = (Integer) company.get("status");
			String webSiteUrl = (String) company.get("web_site_url");
			Integer domainStatus = (Integer) company.get("domain_status");
			String domainProtocol = (String) company.get("domain_protocol");
			
			if(HTMLFetcher.ignorWords.contains(webSiteUrl)) {
				continue;
			}
				
			driver.get(domainProtocol + "://" + webSiteUrl);
			
			System.out.println(a + " domainStatus " + domainStatus + " " + domainProtocol + "://" + webSiteUrl);
			
			Utils.sleepThread(5000);
		}
	}


	private static List<HashMap<String, Object>> getCompanys() {
		
		List<HashMap<String, Object>>  companys = new ArrayList<HashMap<String, Object>>(50);
		
		List<HashMap<String, Object>>  domains = DomainNameDAO.selectAllByStatus(2);

		for (int a = 0; a < domains.size(); a++) {
			
			HashMap<String, Object> domain = domains.get(a);
			
			String domainName = (String) domain.get("domain_name");
			
			List<HashMap<String, Object>>  rCompanys = InfoDAO.getByWebSiteUrl(domainName);
			
			if(rCompanys != null && rCompanys.size() > 0) {
				
				HashMap<String, Object> rCompany = rCompanys.get(0);
				
				rCompany.put("domain_name",(String) domain.get("domain_name"));
				rCompany.put("domain_status",(int) domain.get("status"));
				rCompany.put("domain_protocol",(String) domain.get("protocol"));
				
				companys.add(rCompany);
				
			}
		}
		
		return companys;
	}
	
	public static void main(String[] args) {
		
		String geckoDriver = "/home/laimonas/java/geckodriver-v0.24.0-linux64/geckodriver";
		System.setProperty("webdriver.gecko.driver", geckoDriver);
		
	    VisualWebPageView.initFireFox();
		VisualWebPageView.doJob();
	}
	
}
