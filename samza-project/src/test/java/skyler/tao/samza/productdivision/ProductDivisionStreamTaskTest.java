package skyler.tao.samza.productdivision;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;

public class ProductDivisionStreamTaskTest {

	@Test
	public void divisionTest() throws Exception {
		
		String input = "{\"is_unread_pool\":\"_\",\"platform\":\"iphone\",\"category_r\":\"bo|ad\",\"service_name\":\"single_page\",\"from\":\"1063093010\",\"loadmore\":\"_\",\"product_r\":\"Bidfeed|TopFans|AddFans|FanstopExtend|Bidfeed|Apploft\",\"version\":\"6.3.0\",\"uid\":2271567324,\"unread_status\":0,\"available_pos\":0,\"feedsnum\":0,\"hc\":\"0\",\"reqtime\":1458110024,\"category_imp\":\"_\"}";
//		System.out.println(input);
		
		JSONObject msgJson;
		try {
			msgJson = new JSONObject(input);
		} catch (JSONException e) {
			return;
		}
		
		if (msgJson.has("product_r")) {
			String products = msgJson.getString("product_r");
			for (String product : products.split("\\|")) {
				JSONObject product_output = msgJson;
				String[] deprecatedKeys = {"product_r", "category_r", "from", "unread_status", "available_pos", "category_imp"};
				for (String deprecatedKey : deprecatedKeys) {
					if (product_output.has(deprecatedKey))
						product_output.remove(deprecatedKey);
				}
				product_output.put("product_r", product);
				System.out.println(product_output.toString());
			}
		}
	}
}
