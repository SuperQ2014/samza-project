package skyler.tao.samza.subreq;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.junit.Test;

public class SubreqStreamTaskTest {
	
	@Test
	public void parseSubreqLogTest() {
		
		String msg1 = "10.13.2.39|[23/Dec/2015:10:44:28 +0800] /render/v4/gateway.php 200  0.003 [127.0.0.1:9000] [0.003] [200]";
		String msg2 = "111.13.53.235|[23/Dec/12015:10:44:13 +0800] /uve/uve_strategy 200 0.000 [-] [-] [-]";
		String msg3 = "111.13.53.235|[23/Dec/2015:10:44:13 +0800] --sdfa    c    a     b";
		String msg4 = "111.13.53.235|[abcsdf sdf] /uve/uve_strategy 200 a b";
		
		SubreqStreamTask testedTask = new SubreqStreamTask();
		System.out.println(testedTask.parseSubreqLog(msg1).toString());	//response_time:0.003
		System.out.println(testedTask.parseSubreqLog(msg2));	//response_time:0.000
		System.out.println(testedTask.parseSubreqLog(msg3));	//-1
		System.out.println(testedTask.parseSubreqLog(msg4));	//-1
	}
	
	//@Test
	public void reqtimeTest() {
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MMMMM/yyyy:HH:mm:ss z", Locale.ENGLISH);
		
		String timeString = "[01/Feb/2016:10:55:50 +0800]";
		int start = timeString.indexOf("[");
        int end = timeString.indexOf("]");
		String tmpTime = timeString.substring(start + 1, end);
		Long time = null;
		try {
			time = format.parse(tmpTime).getTime();
			time = time / 1000;
		} catch (ParseException e) {

			System.out.println("Parse error: " + tmpTime);
		}
		
		System.out.println(time);
	}
	
//	@Test
	public void safe_getTest() {
		
//		String msg = "10.13.2.39|[23/Dec/2015:10:44:28 +0800] /render/v4/gateway.php 200 0.003 [127.0.0.1:9000] [0.003] [200]";
		String msg = "10.13.2.39|[23/Dec/2015:10:44:28 +0800] /render/v4/gateway.php 200  0.003 [127.0.0.1:9000] [0.003] [200]";
		
		SubreqStreamTask testedTask = new SubreqStreamTask();
		msg = testedTask.removeScribeIp(msg);
		String[] info = msg.split("\\s+");
		System.out.println(testedTask.safe_get(info, 0));
		System.out.println(testedTask.safe_get(info, 1));
		System.out.println(testedTask.safe_get(info, 2));
		System.out.println(testedTask.safe_get(info, 3));
		System.out.println(testedTask.safe_get(info, 4));
		
		System.out.println("=========safe_get_numbers============");
		System.out.println(testedTask.safe_get_numbers(info, 0));
		System.out.println(testedTask.safe_get_numbers(info, 1));
		System.out.println(testedTask.safe_get_numbers(info, 2));
		System.out.println(testedTask.safe_get_numbers(info, 3));
		System.out.println(testedTask.safe_get_numbers(info, 4));
		
		System.out.println("=========results[index]============");
		String[] results = msg.split("\\s+");
		System.out.println("Index 0: " + results[0]);
		System.out.println("Index 1: " + results[1]);
		System.out.println("Index 2: " + results[2]);
		System.out.println("Index 3: " + results[3]);
		System.out.println("Index 4: " + results[4]);
	}
	
//	@Test
	public void safe_get_numbersTest() {
		String[] info = new String[3];
		info[0] = "a";
		info[1] = "...";
		info[2] = "";
		SubreqStreamTask testedTask = new SubreqStreamTask();
		System.out.println(testedTask.safe_get_numbers(info, 0));
		System.out.println(testedTask.safe_get_numbers(info, 1));
		System.out.println(testedTask.safe_get_numbers(info, 2));
	}
	
//	@Test 
	public void isNumberTest() {
		String num1 = ".33";
		SubreqStreamTask testedTask = new SubreqStreamTask();
		System.out.println(testedTask.isInteger(num1));
		
		String response_time = null;
		double response_time_double = 0;
		try {
			response_time_double = Double.parseDouble(response_time);
		} catch (Exception e) {
			System.out.println("failed!");
		}
		System.out.println(response_time_double);
	}
}