package skyler.tao.samza.readtime;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import org.apache.samza.system.IncomingMessageEnvelope;
import org.apache.samza.system.OutgoingMessageEnvelope;
import org.apache.samza.system.SystemStream;
import org.apache.samza.task.MessageCollector;
import org.apache.samza.task.StreamTask;
import org.apache.samza.task.TaskCoordinator;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ReadTimeStreamTask implements StreamTask {

	public final SystemStream readtimeStream = new SystemStream("kafka",
			"uve_readtime_druid");

	@Override
	public void process(IncomingMessageEnvelope envelope,
			MessageCollector collector, TaskCoordinator coordinator) {

		String message = (String) envelope.getMessage();
		if (message == null) {
			return;
		}
		String resultMsg = parseReadtimeLog(message);

		if (resultMsg == null) {
			return;
		}
		try {
			collector.send(new OutgoingMessageEnvelope(readtimeStream,
					resultMsg));
		} catch (Exception e) {
		}
	}

	public String parseReadtimeLog(String msg) {

		ReadTimeTarget resultTarget = new ReadTimeTarget();

		String shortMsg = removeScribeIp(msg);

		JSONObject msgJson;

		try {
			msgJson = new JSONObject(shortMsg);
		} catch (JSONException e) {
			return null;
		}

		// necessary keys
		String reqtime_string_origin;
		String ua = "_";
		String mark = "_";
		String uid;
		String item_id = "_";
		String from = "_";
		int readtimeValue = -1;

		try {
			reqtime_string_origin = msgJson.getString("addtime");
			uid = msgJson.getString("uid");
			readtimeValue = msgJson.getInt("readtime");
		} catch (JSONException e) {
			return null;
		}
		
		try {
			ua = msgJson.getString("ua");
			mark = msgJson.getString("mark");
			item_id = msgJson.getString("item_id");
			from = msgJson.getString("from");
		} catch (JSONException e) {
			
		}

		int reqtime = filterReqtime(reqtime_string_origin);

		// reqtime does not satify our demand!
		if (reqtime == -1) {
			return null;
		}

		// uid must be int for hyperUnique in druid
		Long uid_value;
		if (!isInteger(uid)) {
			return null;
		} else {
			uid_value = Long.parseLong(uid);
		}

		String platform = "_";
		String version = "_";

		//filter log like: weibo__weibo__6.0.1__iphone__iphone5.1.1
		int weiboIndex = -1;
		String ua_seg = ua;
		while (true) {
			weiboIndex = ua_seg.indexOf("weibo");
			if (weiboIndex < 0) {
				break;
			}
			ua_seg = ua_seg.substring(weiboIndex + 5);	//the 'o' index in 'weibo'
		}
		
		if (weiboIndex == -1) {
			String[] ua_split = ua_seg.split("_(_)+");
			if (ua_split.length >=3) {
				platform = ua_split[2];
				version = ua_split[1];
			}
		}

		String pos_string = getPosFromMark(mark);
		String readtimeCategory = formReadtimeCategory(readtimeValue);

		// set timestamp
		resultTarget.setReqtime(reqtime);

		// set dimensions
		resultTarget.setPlatform(platform);
		resultTarget.setVersion(version);
		resultTarget.setPos(pos_string);
		resultTarget.setUid(uid_value);
		resultTarget.setFrom(from);
		resultTarget.setItem_id(item_id);
		resultTarget.setReadtimeValue(String.valueOf(readtimeValue));
		resultTarget.setReadtimeCategory(readtimeCategory);

		// set metrics
		int requestCount = 1;
		resultTarget.setRequestCount(requestCount);

		// convert to json string
		Gson gson = new GsonBuilder().create();
		String resultString = gson.toJson(resultTarget);
		return resultString;
	}

	public boolean isInteger(String uid) {

		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(uid).matches();
	}

	private String formReadtimeCategory(int readtimeValue) {
		String readtimeCategory = "-1";
		if (readtimeValue == 0)
			readtimeCategory = "0";
		else if (readtimeValue <= 50)
			readtimeCategory = "(0,50ms]";
		else if (readtimeValue <= 300)
			readtimeCategory = "(50ms,300ms]";
		else if (readtimeValue <= 1000)
			readtimeCategory = "(300ms,1000ms]";
		else if (readtimeValue <= 5000)
			readtimeCategory = "(1000ms,5000ms]";
		else if (readtimeValue > 5000)
			readtimeCategory = "(5000ms,INF)";
		return readtimeCategory;
	}

	private String getPosFromMark(String mark) {

		Integer pos = -1; // default position
		try {
			pos = Integer.parseInt(mark.split("(_)+")[0]);
		} catch (Exception e) {
		}
		String pos_string = pos.toString();
		return pos_string;
	}

	private int filterReqtime(String reqtime_string_origin) {

		int reqtime = -1;
		try {
			String reqtime_string_format = new BigDecimal(reqtime_string_origin)
					.toString().substring(0, 10);
			reqtime = Integer.parseInt(reqtime_string_format);
		} catch (Exception e) {
			return -1;
		}

		// ensure timestamp is posix format, which is 10 digits and must be
		// in range.
		Long timestamp = System.currentTimeMillis();
		int now_timestamp = Integer.parseInt(timestamp.toString().substring(0,
				10));
		int windowPre = 60 * 60 * 24 * 3; // 3 days
		int windowPost = 60 * 60 * 24; // 1 day
		if (reqtime > (now_timestamp - windowPre) && reqtime < now_timestamp) { // 过去3天到现在
		} else if (reqtime >= now_timestamp
				&& reqtime < (now_timestamp + windowPost)) { // 现在到未来1天
			reqtime = now_timestamp;
		} else {
			reqtime = -1;
		}
		return reqtime;
	}

	public String removeScribeIp(String msg) {
		int index = msg.indexOf('|');
		return msg.substring(index + 1);
	}

}
