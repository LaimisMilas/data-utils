package data.utils;

import java.util.HashMap;
import java.util.List;

import lt.mariadb.CompanyInfoDAO;

public class EmailParser {
	
	public static void main(String[] args) {
		
		EmailParser parser = new EmailParser();
		parser.parseEmail();
	}

	public void parseEmail() {

		try {

			List<HashMap<String, Object>> companys = CompanyInfoDAO.selectAll();

			for (int a = 0; a < companys.size(); a++) {

				HashMap<String, Object> company = companys.get(a);

				String companyUrl = (String) company.get("company_url");

				if (companyUrl != null && companyUrl.contains("abalt.lt")) {

					parseAbalLTEmail(company);

					continue;

				}

				if (companyUrl != null && companyUrl.contains("info.lt")) {

					parseInfoLTEmail(company);

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

	private static void parseAbalLTEmail(HashMap<String, Object> company) {

		String rawContacts = (String) company.get("raw_contacts");

		String email = (String) company.get("email");

		Integer id = (Integer) company.get("id");

		if (email.trim().length() == 0) {

			String[] tmp = rawContacts.split("@");

			if (tmp.length > 1) {

				try {

					String userl = tmp[0].split("El. p.")[1].trim();

					String domen = tmp[1].split("\n")[0].trim();

					CompanyInfoDAO.updateEmail(id, userl + "@" + domen);

					System.out.println(userl + "@" + domen + " id=" + id);
					
				} catch (Exception e) {

					e.printStackTrace();

				}

			}

		}

	}
	
}
