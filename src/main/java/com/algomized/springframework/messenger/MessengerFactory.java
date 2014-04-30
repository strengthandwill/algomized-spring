package com.algomized.springframework.messenger;

import java.util.List;

public class MessengerFactory {
	private final List<Messenger> messengers;
	
	public MessengerFactory(List<Messenger> messengers) {
		this.messengers = messengers;
	}
	
	public Messenger createMessenger(String name) throws MessengerNotFoundException {
		for (Messenger messenger : messengers) {
			if (messenger.getName().equals(name)) {
				return messenger;
			}
		}
		throw new MessengerNotFoundException(name);
	}	
}
