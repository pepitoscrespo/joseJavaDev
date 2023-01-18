package org.cryptocrespo.cryptofisher;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.cryptocrespo.communicator.CommunicatorLinkerIN;
import org.cryptocrespo.cryptofisher.general.CFCONST;
import org.cryptocrespo.marketor.Marketor;

public class CommunicatorLinkerTimerThread implements Runnable {
	
	
	public static ConcurrentHashMap<String, Double> exchange_data = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String, Double> assets_amt_map = new ConcurrentHashMap<>();
	
	
	public static ConcurrentHashMap<String, Double> market_prices_map = new ConcurrentHashMap<>();
	
	
	public static AtomicBoolean update_balance =  new AtomicBoolean(false);
	
	
	//private static double current_market_price;
	//private static double primary_curr_amt_sold;
	//private static double primary_curr_amt_bought;
	private static AtomicInteger count_marcas_positivas = new AtomicInteger();
			//0;
	
	
    private AtomicBoolean unlock_after_exception =  new AtomicBoolean(false);
   

	public CommunicatorLinkerTimerThread() {

	}

	@Override
	public void run() {
		

		for (;;) {

			try {
				Thread.sleep(1);
				
				

				if (cryptofisher.ready_to_start_communicator.get() 
						// || unlock_after_exception.get()
						) {
					System.out.println("en thread de communicatorLinker");
					 if (CommunicatorLinkerIN.getCommunicatorLinkerIN_instance().back_from_communicator_link_db.get()
							//  || unlock_after_exception.get()
							 ) {
						System.out.println("calling again pumper in communicator linker");
						  System.out.println ("value of  count_marcas_positivas !!" +  count_marcas_positivas.get() );
						 double l_last_price_from_exchange = exchange_data.get("last_price");
						 double l_last_volume_from_exchange = exchange_data.get("last_volume");
					     CommunicatorLinkerIN.getCommunicatorLinkerIN_instance().pumpDataWithHearBeat(l_last_price_from_exchange,l_last_volume_from_exchange  );
					     cryptofisher.ready_to_start_communicator.set(false);
					    
					     unlock_after_exception.set(false);
					 }
					 
				
				}

			} catch (InterruptedException e) {
				CommunicatorLinkerIN.getCommunicatorLinkerIN_instance().back_from_communicator_link_db.set(true);
				System.out.println("Interrupted Exception en CommunicatorLinkerTimerThread");
				
				e.printStackTrace();
				
			} catch (NullPointerException e) {
				
				CommunicatorLinkerIN.getCommunicatorLinkerIN_instance().back_from_communicator_link_db.set(true);
				System.out.println("NullPointerException en CommunicatorLinkerTimerThread");
				
				e.printStackTrace();
			}
			
			finally {
				 unlock_after_exception.set(true);
			}

		}
				
	
					
				
				 
				
}}
