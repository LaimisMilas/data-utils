package data.utils;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import lt.mariadb.CompanyInfoDAO;
import lt.mariadb.Mariadb;
import lt.mariadb.RekvizitaiDAO;

public class UpdateEmail {
	
	// paimti is info email ir ideti i rekvizite.
	
	public static void setCampanyEmail() {
		
		try {
			
			List<HashMap<String, Object>> items =  CompanyInfoDAO.selectAll();

			for (int a = 0; a < items.size(); a++) {

				HashMap<String, Object> item = items.get(a);

				String companyCode = (String) item.get("company_code");
				
				String email = (String) item.get("email");
				
				List<HashMap<String, Object>> list = RekvizitaiDAO.getByCompanyCode(companyCode);
				
				if(list != null && list.size() > 0) {
					
					HashMap<String, Object> dItem = list.get(0);
					
					Integer dId = (Integer) dItem.get("id");
					
					if(list.size() > 1) {
						
						System.out.println(dItem.get("company_code") + " id " + dItem.get("id") + " " + list.size());
					
						//RekvizitaiDAO.deleteCompany(dId);
						
					}
					
					RekvizitaiDAO.updateEmail(dId, email);
					
				}
				
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}

	public static void cleanCompanyCode() {

		String companyCode = "";
		
		try {

			List<HashMap<String, Object>> companys = RekvizitaiDAO.selectAllCompanysDate();

			for (int a = 0; a < companys.size(); a++) {

				HashMap<String, Object> company = companys.get(a);

				Integer id = (Integer) company.get("id");

				companyCode = (String) company.get("company_code");

				if (companyCode.trim().length() > 0) {
					
					boolean changed = false;

					if (companyCode.contains("senas į.k.")) {
						
						System.out.println(companyCode);

						String[] tmp = companyCode.split(" ");

						companyCode = tmp[0].trim();

						changed = true;
						
					}

					try {

						BigInteger bi = new BigInteger(companyCode);

						if (bi.longValue() <= 0) {

							companyCode = "";
							
							changed = true;
							
						}

					} catch (Exception e) {
						
						System.out.println(companyCode);

						companyCode = "";
						
						changed = true;
					}
					
					
					if(changed) {
						
						RekvizitaiDAO.updateCompanyCode(id, companyCode);
						
					}
					

				}

			}

		} catch (Exception e) {

			companyCode = "";

		}
	}

}