package data.utils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import lt.laimis.selenium.utils.SeleniumUtils;
import mariadb.RekvizitaiDAO;
import utils.Utils;
import mariadb.CompanyUrlDAO;

public class UpdateCompanyUrlStatus {
	
	/**
	 * Ten kur company_url.status = 0, url'as nebuvo atidarytas.
	 * Ten kur company_url.status = 1, url'as buvo atidarytas ir duomenys issaugoti company_2 lentelele.
	 *
	 */

	public static void checkCampanyUrl() {
		
		try {
			
			List<HashMap<String, Object>> items =  CompanyUrlDAO.selectAll();

			for (int a = 0; a < items.size(); a++) {

				HashMap<String, Object> item = items.get(a);
				
				int id = (int) item.get("id");
				String companyUrl = (String) item.get("company_url");
				
				List<HashMap<String, Object>> list = RekvizitaiDAO.getByCompanyUrl(companyUrl);
				//List<HashMap<String, Object>> list = RekvizitaiDAO.getByCompanyUrlId(id);				
				
				if(list != null && list.size() > 0) {
					System.out.println(a + " Found " + companyUrl);
					CompanyUrlDAO.updateStatus(id, 1);
					
				} else {
					//System.out.println(a + " Not found " + companyUrl);
					//CompanyUrlDAO.updateStatus(id, 0);
				}
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	private static WebDriver driver;
	
	public static void initFireFox() {

		driver = new FirefoxDriver();

	}
	
	
	public static void doVisitProfile() {
		
		List<HashMap<String, Object>> companys =  CompanyUrlDAO.selectAllByStatus(0);

		for (int a = 0; a < companys.size(); a++) {

			HashMap<String, Object> company = companys.get(a);

			Integer id = (Integer) company.get("id");
			String companyUrl = (String) company.get("company_url");
				
			driver.get(companyUrl);
			
			System.out.println(a + " " + companyUrl);
			
			//SeleniumUtils.scrollPageDown(1, 300, 1000, driver);
			
			Utils.sleepThread(4000);
		}
	}
	
	public static void main(String[] args) {
		
		String geckoDriver = "/home/laimonas/java/geckodriver-v0.24.0-linux64/geckodriver";
		System.setProperty("webdriver.gecko.driver", geckoDriver);
		
		UpdateCompanyUrlStatus.checkCampanyUrl();
		//UpdateCompanyUrlStatus.initFireFox();
		//UpdateCompanyUrlStatus.doVisitProfile();
	}

}