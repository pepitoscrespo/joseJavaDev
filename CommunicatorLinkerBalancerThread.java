package org.cryptocrespo.cryptofisher;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.cryptocrespo.communicator.CommunicatorLinkerIN;
import org.cryptocrespo.marketor.Marketor;

public class CommunicatorLinkerBalancerThread implements Runnable {

	private static AtomicBoolean importBalanceFromExchange = new AtomicBoolean(false);

	public static void setImportBalanceFromExchange(boolean value) {

		importBalanceFromExchange.set(value);

	}

	public CommunicatorLinkerBalancerThread() {

	}

	@Override
	public void run() {

		for (;;) {

			try {
				Thread.sleep(1);

				if (cryptofisher.ready_to_start_communicator.get()) {
					if (importBalanceFromExchange.get()) {

						System.out.println("updating balances with real data from Exchange... ");
						ConcurrentHashMap<String, Double> balance_map = Marketor.getMarketor()
								.getAllAvailableBalances();

						cryptofisher.user_account_stream_data.put("BAL_BTC", balance_map.get("BTC"));
						cryptofisher.user_account_stream_data.put("BAL_USDT", balance_map.get("USDT"));
						
						System.out.println("updating balances with real data from Exchange... OK UPDATED !!");
						

						System.out.println("en CommunicatorLinkerBalancerThread updated cryptofisher.user_account_stream_data BAL_BTC  "
								+ cryptofisher.user_account_stream_data.get("BAL_BTC")
								+ " updated cryptofisher.user_account_stream_data BAL_USDT  "
								+ cryptofisher.user_account_stream_data.get("BAL_USDT"));

						importBalanceFromExchange.set(false);

					}
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
