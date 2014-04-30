package com.algomized.springframework.messenger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

/**
 * Implementation of {@link Messenger} for Google Cloud Messaging (GCM).
 * 
 * @author Poh Kah Kong
 */
public class GCMMessenger implements Messenger {
	private static final int MULTICAST_SIZE = 1000;
	
	private static final Executor threadPool = Executors.newFixedThreadPool(5);
	
	private static final Logger logger = LoggerFactory.getLogger(GCMMessenger.class);
	
	@Autowired
	private Sender sender;
	
	/**
	 * Send a message to a single device through GCM.
	 */
	@Override
	public String sendMessage(String registrationId, Message message) throws IOException {
		Result result = sender.send(message, registrationId, 5);
		String status = "Sent message to one device: " + result;
		return status;
	}
	

	/**
	 * Send a message to multiple devices through GCM.
	 */	
	@Override
	public String sendMulticastMessage(List<String> devices, Message message) throws IOException {	
		// send a multicast message using JSON
		// must split in chunks of 1000 devices (GCM limit)
		int total = devices.size();
		List<String> partialDevices = new ArrayList<String>(total);
		int counter = 0;
		int tasks = 0;
		for (String device : devices) {
			counter++;
			partialDevices.add(device);
			int partialSize = partialDevices.size();
			if (partialSize == MULTICAST_SIZE || counter == total) {
				asyncSend(partialDevices, message);
				partialDevices.clear();
				tasks++;
			}
		}
		String status = "Asynchronously sending " + tasks + " multicast messages to " +
				total + " devices";
		return status;
	}	

	/**
	 * Sends the message to the given devices through GCM.
	 * 
	 * @param partialDevices the devices to receive the message 
	 * @param message the message to be sent
	 */
	private void asyncSend(List<String> partialDevices, final Message message) {		
		// make a copy
		final List<String> devices = new ArrayList<String>(partialDevices);
		threadPool.execute(new Runnable() {

			public void run() {
				MulticastResult multicastResult;
				try {
					multicastResult = sender.send(message, devices, 5);
				} catch (IOException e) {
					//logger.trace(Level.SEVERE, "Error posting messages", e);
					return;
				}
				List<Result> results = multicastResult.getResults();
				// analyze the results
				for (int i = 0; i < devices.size(); i++) {
					String regId = devices.get(i);
					Result result = results.get(i);
					String messageId = result.getMessageId();
					if (messageId != null) {
						//logger.fine("Succesfully sent message to device: " + regId +
						//		"; messageId = " + messageId);
						String canonicalRegId = result.getCanonicalRegistrationId();
						if (canonicalRegId != null) {
							// same device has more than on registration id: update it
							logger.info("canonicalRegId " + canonicalRegId);
							//Datastore.updateRegistration(regId, canonicalRegId);
						}
					} else {
						String error = result.getErrorCodeName();
						if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
							// application has been removed from device - unregister it
							logger.info("Unregistered device: " + regId);
							//Datastore.unregister(regId);
						} else {
							//logger.severe("Error sending message to " + regId + ": " + error);
						}
					}
				}
			}});
	}


	@Override
	public String getName() {
		return "gcm";
	}	
}
