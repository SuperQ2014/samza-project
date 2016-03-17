package skyler.tao.samza.productdivision;

public class ProductLogTarget {

	private int reqtime;
	
	private String platform;
	private String service_name;
	private String version;
	private String is_unread_pool;
	private String loadmore;
	private String uid;
	private String product;
	
	private int feedsnum;
	private int hc;
	
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
	public String getService_name() {
		return service_name;
	}
	public void setService_name(String service_name) {
		this.service_name = service_name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getIs_unread_pool() {
		return is_unread_pool;
	}
	public void setIs_unread_pool(String is_unread_pool) {
		this.is_unread_pool = is_unread_pool;
	}
	public String getLoadmore() {
		return loadmore;
	}
	public void setLoadmore(String loadmore) {
		this.loadmore = loadmore;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public int getFeedsnum() {
		return feedsnum;
	}
	public void setFeedsnum(int feedsnum) {
		this.feedsnum = feedsnum;
	}
	public int getHc() {
		return hc;
	}
	public void setHc(int hc) {
		this.hc = hc;
	}
}
