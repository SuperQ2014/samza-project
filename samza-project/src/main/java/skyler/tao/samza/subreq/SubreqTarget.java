package skyler.tao.samza.subreq;

public class SubreqTarget {

	// timestamp, posix format: 10 digits
	private long reqtime;

	//dimensions
	private String subreq_name;
	private String status_code;
	
	//metric
	private double response_time;

	public long getReqtime() {
		return reqtime;
	}

	public void setReqtime(long reqtime) {
		this.reqtime = reqtime;
	}

	public String getSubreq_name() {
		return subreq_name;
	}

	public void setSubreq_name(String subreq_name) {
		this.subreq_name = subreq_name;
	}

	public String getStatus_code() {
		return status_code;
	}

	public void setStatus_code(String status_code) {
		this.status_code = status_code;
	}

	public double getResponse_time() {
		return response_time;
	}

	public void setResponse_time(double response_time) {
		this.response_time = response_time;
	}
}
