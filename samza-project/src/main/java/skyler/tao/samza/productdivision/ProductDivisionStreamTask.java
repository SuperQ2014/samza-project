package skyler.tao.samza.productdivision;

import org.apache.samza.system.IncomingMessageEnvelope;
import org.apache.samza.system.OutgoingMessageEnvelope;
import org.apache.samza.system.SystemStream;
import org.apache.samza.task.MessageCollector;
import org.apache.samza.task.StreamTask;
import org.apache.samza.task.TaskCoordinator;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ProductDivisionStreamTask implements StreamTask {

	private final SystemStream productStream = new SystemStream("kafka", "uve_product_division");
	@Override
	public void process(IncomingMessageEnvelope envelope,
			MessageCollector collector, TaskCoordinator coordinator)
			throws Exception {
		
		String message = (String) envelope.getMessage();
		JSONObject msgJson;
		try {
			msgJson = new JSONObject(message);
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
				collector.send(new OutgoingMessageEnvelope(productStream, product_output.toString()));
			}
		}
	}

}
