package shared;



public class ProcessResponse {
	
	enum ResponseStatus {
		SUCCESS,
		FAIL
	}
	
	ResponseStatus status;
	
	String error;

	public boolean success() {
		if (status == ResponseStatus.SUCCESS) {
			return true;
		}
		return false;
	}

	public String getError() {
		return error;
	}
}
