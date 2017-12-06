package org.smap.service;

public class StatusMessageResponse
{
	private String message;
	private String results;
	private boolean success;

	public StatusMessageResponse(String message, boolean success,String results) {
		super();
		this.message = message;
		this.success = success;
		this.setResults(results);
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}



	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}
}
