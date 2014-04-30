package com.algomized.springframework.messenger;

import java.io.IOException;
import java.util.List;

import com.google.android.gcm.server.Message;

/**
 * Specifies the required methods as a Messenger.
 * 
 * @author Poh Kah Kong
 *
 */
public interface Messenger {	
	/**
	 * Send a message to a single device.
	 * 
	 * @param registrationId the registration id of the device to receive the message
	 * @param message the message to be sent
	 * @return
	 * @throws IOException
	 */
	public String sendMessage(String registrationId, Message message) throws IOException;
	
	/**
	 * Send a message to multiple devices.
	 * 
	 * @param devices the list of registration ids of the devices to receive the message
	 * @param message the message to be sent
	 * @return
	 * @throws IOException
	 */	
	public String sendMulticastMessage(List<String> devices, Message message) throws IOException;
}
