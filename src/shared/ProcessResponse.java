package shared;



public enum ProcessResponse {
	
	SUCCESS(true),
	FAIL(false);
	
	private boolean isSuccess;
	
	ProcessResponse(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public boolean isSuccess() {
		return this.isSuccess;
	}
}
