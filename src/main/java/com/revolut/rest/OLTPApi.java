package com.revolut.rest;

import com.revolut.db.AccountDao;
import com.revolut.db.TranslogDao;
import com.revolut.db.WalletDao;
import com.revolut.entity.Account;
import com.revolut.entity.Translog;
import com.revolut.entity.Wallet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.ws.rs.*;
import java.util.UUID;

@Path("/oltpapi")
public class OLTPApi {

	private static WalletDao wDao = new WalletDao();
	private static TranslogDao tDao = new TranslogDao();
	private static AccountDao aDao = new AccountDao();

	@GET
	@Path("heartbeat")
	@Produces("text/plain")
	public String heartbeat() {
		return "200";
	}

	// phone2phone transactional
	@POST
	@Path("p2p")
	@Produces("text/plain")
	public String p2p(@QueryParam("src") String srcMsisdn, @QueryParam("dest") String dstMsisdn,
					  @QueryParam("amount") long amount, @QueryParam("currency") String currency) {
		try {
			Wallet srcWallet = wDao.getWalletByMsisdn(srcMsisdn);
			Wallet dstWallet = wDao.getWalletByMsisdn(dstMsisdn);

			tDao.addRow(new Translog(UUID.randomUUID().toString(), srcWallet.getAcctNum(), dstWallet.getAcctNum(), "P2P", amount, currency));

			aDao.debitCredit(srcWallet.getAcctNum(), dstWallet.getAcctNum(), amount);
			return "200";
		} catch (Exception e) {
			e.printStackTrace();
			return "500";
		}
	}

	@POST
	@Path("payment")
	@Produces("text/plain")
	public String payment(@QueryParam("msisdn") String msisdn, @QueryParam("amount") long amount, @QueryParam("currency") String currency) {
		try {
			Wallet wallet = wDao.getWalletByMsisdn(msisdn);

			tDao.addRow(new Translog(UUID.randomUUID().toString(), wallet.getAcctNum(), null, "PAYMENT", amount, currency));

			aDao.debit(wallet.getAcctNum(), amount);
			return "200";
		} catch (Exception e) {
			e.printStackTrace();
			return "500";
		}
	}

	@GET
	@Path("translog")
	@Produces("text/plain")
	public String translog() {
		return tDao.getAllRows().toString();
	}

	@GET
	@Path("balances")
	@Produces("text/plain")
	public String balances() {
		return aDao.getAllRows().toString();
	}

	public static void main(String[] args) throws Exception {
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");

		Server jettyServer = new Server(8000);
		jettyServer.setHandler(context);

		ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
		jerseyServlet.setInitOrder(0);

		jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", OLTPApi.class.getCanonicalName());

		try {
			jettyServer.start();
			jettyServer.join();
		} finally {
			jettyServer.destroy();
		}

	}

}
