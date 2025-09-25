package shared;

public class Status {
	public enum StatusCode {
		SUCCESS,
		FAIL
	}
	
	private StatusCode statusCode;
	
	public StatusCode getStatus() {
		return statusCode;
	}
}
