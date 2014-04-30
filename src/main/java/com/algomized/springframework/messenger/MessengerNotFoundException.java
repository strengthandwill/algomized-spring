package com.algomized.springframework.messenger;

import com.algomized.springframework.http.HttpRuntimeException;

@SuppressWarnings("serial")
public class MessengerNotFoundException extends HttpRuntimeException {
	public MessengerNotFoundException(String name) {
		super("Messenger " + name + " is not found");
	}

	@Override
	public String getErrorCode() {
		return "messenger_not_found";
	}	
}
