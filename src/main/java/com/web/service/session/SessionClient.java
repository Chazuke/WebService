package com.web.service.session;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.spy.memcached.MemcachedClient;

public class SessionClient {
	Logger LOG = LoggerFactory.getLogger(SessionClient.class);

	public MemcachedClient client;

	public SessionClient() throws IOException {
		client = new MemcachedClient(new InetSocketAddress("192.168.0.107", 11211));
	}

	public void destroy() {
		client.shutdown();
	}

	public void set(String key, Object data) {
		client.set(key, 0, data);
	}

	public Object get(String key) {
		return client.get(key);
	}

	public Object gat(String key) {
		return client.getAndTouch(key, 0);
	}

}
