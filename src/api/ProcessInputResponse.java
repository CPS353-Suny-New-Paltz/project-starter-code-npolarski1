package api;

public class ProcessInputResponse {
	
	enum ResponseStatus {
		SUCCESS,
		FAIL
	}
	
	ResponseStatus status;

	boolean success() {
		if (status == ResponseStatus.SUCCESS) {
			return true;
		}
		return false;
	}

	public ComputationResult getCompResults() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
