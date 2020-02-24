package com.web.service.session;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

public class SessionClient {
	Logger LOG = LoggerFactory.getLogger(SessionClient.class);

	public MemcachedClient client;

	public SessionClient(String address, int port) throws IOException {
		client = new MemcachedClient(new InetSocketAddress(address, port));
	}

	public void destroy() {
		client.shutdown();
	}

	public OperationFuture<Boolean> set(String key, Object data) {
		return client.set(key, 0, data);
	}

	public Object get(String key) {
		return client.get(key);
	}

	public Object gat(String key) {
		return client.getAndTouch(key, 0);
	}
	
	public SessionObject ensureSessionObjectExists(String sessionId) {
		Object sessionObject = get(sessionId);
		if (null == sessionObject) {
			sessionObject = new SessionObject(sessionId);
			set(sessionId, sessionObject);
		}
		return (SessionObject) sessionObject;
	}

}
