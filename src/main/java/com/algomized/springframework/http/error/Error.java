package com.algomized.springframework.http.error;

/**
 * Abstract error class that contains an error attribute 
 * passed from exception.
 * 
 * @author Poh Kah Kong
 *
 */
public abstract class Error {
	private String error;
	
	public Error() {}
	
	public Error(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
