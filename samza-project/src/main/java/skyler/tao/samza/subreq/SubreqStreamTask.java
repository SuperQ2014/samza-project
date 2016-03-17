package skyler.tao.samza.subreq;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.samza.system.IncomingMessageEnvelope;
import org.apache.samza.system.OutgoingMessageEnvelope;
import org.apache.samza.system.SystemStream;
import org.apache.samza.task.MessageCollector;
import org.apache.samza.task.StreamTask;
import org.apache.samza.task.TaskCoordinator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SubreqStreamTask implements StreamTask {

	private final Logger logger = Logger.getLogger(SubreqStreamTask.class);

	private final SimpleDateFormat format = new SimpleDateFormat(
			"dd/MMMMM/yyyy:HH:mm:ss z", Locale.ENGLISH);
	public final SystemStream subreqStream = new SystemStream("kafka1",
			"uve_subreq_source");

	@Override
	public void process(IncomingMessageEnvelope envelope,
			MessageCollector collector, TaskCoordinator coordinator)
			throws Exception {

		String message = (String) envelope.getMessage();
		if (message == null) {
			logger.info("Input message empty!");
			logger.info(message);
			return;
		}
		String result = parseSubreqLog(message);
		if (result == null) {
			logger.info("Parse log message failed!");
			logger.info(message);
			return;
		}
		collector.send(new OutgoingMessageEnvelope(subreqStream, result));
	}

	public String parseSubreqLog(String msg) {
		
		SubreqTarget resultTarget = new SubreqTarget();

		try {

			msg = removeScribeIp(msg);
			String[] info = msg.split("\\s+");
			Long reqtime = getReqtime(safe_get(info, 0) + " "
					+ safe_get(info, 1));
			String subreq_name = safe_get(info, 2);
			String status_code = safe_get_numbers(info, 3);
			String response_time = safe_get_numbers(info, 4);
			
			if (! isInteger(status_code)) {
				return null;
			}

			double response_time_double = 0;
			try {
				response_time_double = Double.parseDouble(response_time);
			} catch (NumberFormatException e) {
				return null;
			}

			if (reqtime != null && reqtime.toString().length() == 10) {
				resultTarget.setReqtime(reqtime);
				resultTarget.setSubreq_name(subreq_name);
				resultTarget.setStatus_code(status_code);
				resultTarget.setResponse_time(response_time_double);
				
				Gson gson = new GsonBuilder().create();
				String resultString = gson.toJson(resultTarget);
				return resultString;
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.info("ERROR msg:" + msg);
			return null;
		}
	}

	public Long getReqtime(String timeString) {

		int start = timeString.indexOf("[");
		int end = timeString.indexOf("]");

		if (start > end) {
			logger.info("Time in message is error!");
			return null;
		}

		String tmpTime = timeString.substring(start + 1, end);
		Long time = null;
		try {
			time = format.parse(tmpTime).getTime();
			time = time / 1000;
		} catch (ParseException e) {
			logger.info("Parse error: " + tmpTime);
			time = null;
		}
		return time;
	}

	public String removeScribeIp(String msg) {
		int index = msg.indexOf('|');
		return msg.substring(index + 1);
	}

	public String safe_get(String[] info, int index) {

		if (info[index] != null && index < info.length) {
			String safe_result = info[index];
			return safe_result;
		}
		return "-1";
	}

	public String safe_get_numbers(String[] info, int index) {

		if (index < info.length && info[index] != "" && info[index] != null) {
			String safe_result = info[index];
			Pattern pattern = Pattern.compile("[0-9|.]*");
			if (!pattern.matcher(safe_result).matches()) {
				return "0";
			}
			return safe_result;
		}
		return "0";
	}

	public boolean isInteger(String str) {
		try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
	}
}