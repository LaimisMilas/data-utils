package data.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lt.laimis.selenium.utils.PageUtils;
import lt.laimis.selenium.utils.SeleniumUtils;
import main.java.lt.laimis.java.utils.Utils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import lt.mariadb.DomainNameDAO;
import lt.mariadb.CompanyInfoDAO;

public class VisualWebPageView {
	
	private static WebDriver driver;
	
	// pasiims sarasa kompaniju ir ciklu per visas atidarines ju puslapius.
	// cia yra rekvizitu puslapis kur galima nuskaityti detalesne kompanijos informacija
	public static void multipleVisits() throws Exception  {
		List<HashMap<String, Object>> companies = getCompanies();
		for (int a = 0; a < companies.size(); a++) {
			HashMap<String, Object> company = companies.get(a);
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
			PageUtils.scrollPageDown(1, 300, 1000, driver);
			Utils.sleepThread(4000);
		}
	}

	// Grazina companiju sarasa is BD lenteles company_info
	// tik nera aisku kaip selectina, atrodo yra atskira domenu lentele,
	// pagal tai iesko url'o kas ner 'web_site_url', cia tiktu contains
	private static List<HashMap<String, Object>> getCompanies() {
		List<HashMap<String, Object>>  companies = new ArrayList<HashMap<String, Object>>(50);
		//yra lentele kuri saugo domain_name laukai: domain_name, status, note, protocol, company_code
		List<HashMap<String, Object>>  domains = DomainNameDAO.selectAllByStatus(2);
		for (int a = 0; a < domains.size(); a++) {
			HashMap<String, Object> domain = domains.get(a);
			String domainName = (String) domain.get("domain_name");
			// ten net nera LIKI ar kasnors panasaus yra =. nebent lentoje saugomas visas url'as.
			List<HashMap<String, Object>>  rCompanies = CompanyInfoDAO.getByWebSiteUrl(domainName);
			if(rCompanies != null && rCompanies.size() > 0) {
				HashMap<String, Object> rCompany = rCompanies.get(0);
				rCompany.put("domain_name",(String) domain.get("domain_name"));
				rCompany.put("domain_status",(String) domain.get("status"));
				rCompany.put("domain_protocol",(String) domain.get("protocol"));
				companies.add(rCompany);
			}
		}
		return companies;
	}
	
	public static void main(String[] args) {
		SeleniumUtils.setGeckoDriver();
		driver = SeleniumUtils.initFireFox("");
		try {
			VisualWebPageView.multipleVisits();
			//VisualWebPageView.singleVisit("http://reikalingaaukle.lt");
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	private static void singleVisit(String webSiteUrl) {
		if(!HTMLFetcher.ignorWords.contains(webSiteUrl)) {
			driver.get(webSiteUrl);
			System.out.println(webSiteUrl);
		}
	}
	
}
