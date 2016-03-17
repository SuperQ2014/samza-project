package skyler.tao.samza.readtime;

import org.junit.Test;

public class ReadTimeStreamTaskTest {

	@Test
	public void parseReadTimeLogTest() {
		
//		String msg = "111.13.87.70|{\"item_id\":\"3928267411621521\",\"ua\":\"vivo-vivo X5Pro V__weibo__5.7.0__android__android5.0.2\",\"mark\":\"ab_29801875EC575506DF8FD8C411982C1FA940FF48388CC719CB6F0890729C06F22919F00F6B165617D15F679E00F1F0F34C30C4A4BC2CCA424EA97A6CF0F866F09F1A55530D2FF7FA91BBD2FF832BBF50932B8897228DC6FBF963520C87EEF8AD4B8CF41FCD7226E74A2D7DDE7533BB78C985DD2EAD48D6682DAB07D6D6C034B00AB1EAAB13A155DE82BE075B3E412D70\",\"uid\":\"5658979630\",\"readtime\":70,\"addtime\":1453352315.4215,\"from\":\"1057095010\",\"ip\":\"180.212.79.164\",\"lang\":\"zh_CN\"}";
//		String msg1 = "{\"item_id\":\"3931667423363803\",\"ua\":\"iPhone7,2__weibo__5.7.1__iphone__os9.0\",\"mark\":\"3_2A48F99F38072F997159A8262B47D9F2FAE96062475368D69434AB7FCEB28EFE6DDBE0382F5A5C37FE03CC5AE8F7ACA72D7F66E034F1E8B088C45AC35EF0EC882B8D10DD5DAA2067627BE9F9048DF5B2FAED4D4D5502C728E67097B463E8DBE216C8E3580257D5F53C15C9E7E5C99F50934AB049D0C12ADB8D6FEDC1D842FDD5\",\"uid\":\"3847021831\",\"readtime\":302008,\"addtime\":1453087479.7185,\"from\":\"1057193010\",\"ip\":\"117.132.29.114\",\"lang\":\"zh_CN\"}";
//		String msg2 = "adf|agefs";
		String msg = "{\"item_ida\":\"3933369098636007\",\"uab\":\"PULID__F19-PULID F19__weibo__weibo__5.7.0___\",\"mark\":\"11_8BD0199165BC55567D9B42D8A7DA2CB072DED92D798DEEAA2C219493967445CF663345ABC2798FD1D889FF9F7A8FDC7D44CF3E59203FE0B0EE4A69726BA4B0B828E5E26B5DA68A13C3ABA200392E5AFEEBA7451FDA579E483D6053A7A5CDCFCC12569CC79B635808A41B6376F94C00944962C36053020C09359A224D33EBE934\",\"uid\":\"5696634348\",\"readtime\":948,\"addtime\":\"1457404859\",\"from\":\"1057095010\",\"ip\":\"223.86.183.174\",\"lang\":\"zh_CN\"}";
		ReadTimeStreamTask testedTask = new ReadTimeStreamTask();
		String msg_short = testedTask.removeScribeIp(msg);
		System.out.println(msg_short);
		String resultString = testedTask.parseReadtimeLog(msg);
		System.out.println(resultString);
	}
	
//	@Test
	public void removeScribeIpTest() {
		
		ReadTimeStreamTask testedTask = new ReadTimeStreamTask();
		String msg2 = "111.13.87.70&{\"item_id\":\"3928267411621521\"";
		String restMsg2 = testedTask.removeScribeIp(msg2);
		System.out.println(restMsg2);
	}
	
//	@Test 
	public void isIntegerTest() {
		String uid = "false234";
		ReadTimeStreamTask testedTask = new ReadTimeStreamTask();
		System.out.println(testedTask.isInteger(uid));
	}
	
//	@Test
	public void uaTest() {
		String ua = "vi__vo-vivo X5Pro V___weibo__5.7.0__android__android5.0.2";
		String platform = null;
		String version = null;

		int weiboIndex = ua.indexOf("weibo");
		if (weiboIndex != -1) {
			String ua_seg = ua.substring(weiboIndex);
			String[] ua_split = ua_seg.split("__");
			platform = ua_split[2];
			version = ua_split[1];
		} else {
			platform = null;
			version = null;
		}
		System.out.println(platform + version);
	}
	
//	@Test
	public void splitTest() {
		String ua = "weibo__5.7.0_android__android4.2.2";
		String[] ua_split = ua.split("_(_)+");
		
		for (String ua_split_simple : ua_split) {
			System.out.println(ua_split_simple);
		}
		
		System.out.println("Platform: " + ua_split[2]);
		System.out.println("Version: " + ua_split[1]);
	}
	
//	@Test
	public void substringTest() {
		String ua = "aaaaaa_weibo__weibo__weibo__adfasdfa__PULID__F19-PULID F19__weibo__5.7.0__android__android4.2.2";
		int weiboIndex = -1;
		String ua_seg = ua;
		System.out.println(ua_seg.indexOf("weibo"));
		while (true) {
			weiboIndex = ua_seg.indexOf("weibo");
			System.out.println(weiboIndex);
			if (weiboIndex < 0) {
				break;
			}
			ua_seg = ua_seg.substring(weiboIndex + 5);
		}
		System.out.println(ua_seg);
		System.out.println("=========");
		String[] ua_split = ua_seg.split("(_)+");
		for (String ua_split_simple : ua_split) {
			System.out.println(ua_split_simple);
		}
		System.out.println("Platform: " + ua_split[2]);
		System.out.println("Version: " + ua_split[1]);
	}
}
