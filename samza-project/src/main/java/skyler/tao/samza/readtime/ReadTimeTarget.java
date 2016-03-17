package skyler.tao.samza.readtime;

public class ReadTimeTarget {

		//timestamp, posix format: 10 digits
		private int reqtime;
		
		//dimensions
		private String platform;
		private String version;
		private String pos;
		private String from;
		private String item_id;
		private String readtimeValue;
		
		public String getFrom() {
			return from;
		}
		public void setFrom(String from) {
			this.from = from;
		}
		public String getItem_id() {
			return item_id;
		}
		public void setItem_id(String item_id) {
			this.item_id = item_id;
		}
		public String getReadtimeValue() {
			return readtimeValue;
		}
		public void setReadtimeValue(String readtimeValue) {
			this.readtimeValue = readtimeValue;
		}
		private String readtimeCategory;
		
		//metric
		private int requestCount;
		private Long uid;
		
		public int getReqtime() {
			return reqtime;
		}
		public void setReqtime(int reqtime) {
			this.reqtime = reqtime;
		}

		public String getPlatform() {
			return platform;
		}
		public void setPlatform(String platform) {
			this.platform = platform;
		}

		public String getVersion() {
			return version;
		}
		public void setVersion(String version) {
			this.version = version;
		}

		public String getPos() {
			return pos;
		}
		public void setPos(String pos) {
			this.pos = pos;
		}

		public Long getUid() {
			return uid;
		}
		public void setUid(Long uid) {
			this.uid = uid;
		}

		public String getReadtimeCategory() {
			return readtimeCategory;
		}
		public void setReadtimeCategory(String readtimeCategory) {
			this.readtimeCategory = readtimeCategory;
		}

		public int getRequestCount() {
			return requestCount;
		}
		public void setRequestCount(int requestCount) {
			this.requestCount = requestCount;
		}
}
