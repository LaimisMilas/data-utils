package data.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lt.laimis.mariadb.CompanyInfoDAO;

public class CheckForDublicaiton {

	public static int index = 0;

	public static void deleteDublicate(){
		
		List<HashMap<String, Object>> items = CompanyInfoDAO.selectAll();

		if (items != null) {

			for (HashMap<String, Object> item : items) {
				
				String companyCode = (String) item.get("company_code");
				Integer id = (Integer) item.get("id");
				
				List<HashMap<String, Object>> response = CompanyInfoDAO.getByCompanyCode(companyCode);
				
				if(response.size() > 1) {
					
					for (HashMap<String, Object> company : response) {

						Integer companyId = (Integer) company.get("id");						
						
						if(!id.equals(companyId)) {
							
							System.out.println("Delete " + companyCode + " "+ companyId);
						
							//CompanyInfoDAO.delete(companyId);
							
						}
					}
				}
				
			}			
		}
	}

	public static void chechDublicatedCompanys() {

		List<String> codesForDelete = new ArrayList<String>();

		List<HashMap<String, Object>> items = CompanyInfoDAO.selectAll();

		if (items != null) {

			for (HashMap<String, Object> item : items) {

				List<HashMap<String, Object>> items2 = CompanyInfoDAO.selectAll();

				String compnay = (String) item.get("company_code");

				int counter = 0;

				for (HashMap<String, Object> item2 : items2) {

					String compnay2 = (String) item2.get("company_code");

					if (compnay.equals(compnay2)) {

						System.out.println(compnay + " " + compnay2 + " " + counter);

						if (counter > 0) {

							Integer sId = (Integer) item2.get("id");

							if (!codesForDelete.contains(compnay)) {

								codesForDelete.add(compnay);

							}

							//CompanyInfoDAO.delete(sId);

						}

						counter++;

					}

				}

				if (counter > 1) {

					System.out.println(index + " " + compnay + " " + counter);

					index++;

				}

				if (index > 2000) {

					// System.exit(0);

				}

			}

			for (String code : codesForDelete) {

				System.out.println(code);
			}

		}

	}
	
	public static void main(String[] args) {
		CheckForDublicaiton cfd = new CheckForDublicaiton();
		cfd.chechDublicatedCompanys();
	}

}
