package data.utils;

import java.sql.SQLException;
import java.util.List;

public class CheckForDublicaitonInfo {

	public static int index = 0;
	
	public static void main(String[] args) {

		try {
			CheckForDublicaitonInfo.chechDublicatedCompanys();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void chechDublicatedCompanys() throws SQLException {

		List items = null;

		List items2 = null;

		if (items != null) {

			for (Object item : items) {

				String[] tmp = item.toString().split(",");

				String compnay = tmp[0].replace("{", "");

				int counter = 0;

				for (Object item2 : items2) {

					String[] tmp2 = item2.toString().split(",");

					String compnay2 = tmp2[0].replace("{", "");

					if (compnay.equals(compnay2)) {

						if (counter > 0) {

							String sId = tmp2[1].replace("}", "").replace("id=", "");

							// Mariadb.deleteCompanyUrl(sId);

						}

						counter++;

					}

				}

				if (counter > 1) {

					System.out.println(index + " " + compnay);

					index++;

				}

				if (index > 2000) {

					System.exit(0);

				}
			}

		}

	}

}
