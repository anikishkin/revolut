package com.revolut.rest;

import org.glassfish.jersey.client.ClientConfig;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class OLTPApiTest {

	private Client client = ClientBuilder.newClient(new ClientConfig());
	private Logger log = Logger.getLogger("JUNIT");

	@Test
	public void heartbeat() {
		log.info("API HEARTBEAT");
		WebTarget target = client.target(UriBuilder.fromUri("http://localhost:8000/oltpapi/heartbeat").build());
		String resp = target.request().accept(MediaType.TEXT_PLAIN).get(String.class);
		log.info("RESP: " + resp);
	}

	@Test
	public void doP2p() {
		log.info("P2P SUCCESS: src:12125650001 dest:12125650002 amount:100 currency:840");
		WebTarget target = client.target(UriBuilder.fromUri("http://localhost:8000/oltpapi/p2p?src=12125650001&dest=12125650002&amount=100&currency=840").build());
		String resp = target.request().accept(MediaType.TEXT_PLAIN).post(Entity.entity(null, MediaType.WILDCARD_TYPE), String.class);
		log.info("RESP: " + resp);
		Assert.assertEquals("200", resp);
	}

	@Test
	public void doP2pF() {
		log.info("P2P FAIL: src:123 dest:321 amount:100 currency:840");
		WebTarget target = client.target(UriBuilder.fromUri("http://localhost:8000/oltpapi/p2p?src=123&dest=321&amount=100&currency=840").build());
		String resp = target.request().accept(MediaType.TEXT_PLAIN).post(Entity.entity(null, MediaType.WILDCARD_TYPE), String.class);
		log.info("RESP: " + resp);
		Assert.assertEquals("500", resp);
	}

	@Test
	public void doPayment() {
		log.info("PAYMENT SUCCESS: acct:12125650001 amount:100 currency:840");
		WebTarget target = client.target(UriBuilder.fromUri("http://localhost:8000/oltpapi/payment?msisdn=12125650001&amount=100&currency=840").build());
		String resp = target.request().accept(MediaType.TEXT_PLAIN).post(Entity.entity(null, MediaType.WILDCARD_TYPE), String.class);
		log.info("RESP: " + resp);
		Assert.assertEquals("200", resp);
	}

	@Test
	public void parallel() {
		log.info("PARALLEL");
		getBalances();
		ExecutorService es = Executors.newFixedThreadPool(5);
		es.submit(() -> doP2p());
		es.submit(() -> doP2p());
		es.submit(() -> doP2p());
		es.submit(() -> doP2p());
		es.submit(() -> doP2p());
		try {
			es.awaitTermination(1, TimeUnit.SECONDS);
			getBalances();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void getTranslog() {
		log.info("SHOW TRASNLOG...");
		WebTarget target = client.target(UriBuilder.fromUri("http://localhost:8000/oltpapi/translog").build());
		String resp = target.request().accept(MediaType.TEXT_PLAIN).get(String.class);
		log.info("RESP: " + resp);
		Assert.assertNotNull(resp);
	}

	@Test
	public void getBalances() {
		log.info("SHOW BALANCES...");
		WebTarget target = client.target(UriBuilder.fromUri("http://localhost:8000/oltpapi/balances").build());
		String resp = target.request().accept(MediaType.TEXT_PLAIN).get(String.class);
		log.info("RESP: " + resp);
		Assert.assertNotNull(resp);
	}
}
