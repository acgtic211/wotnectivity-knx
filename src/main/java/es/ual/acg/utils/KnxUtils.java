package es.ual.acg.utils;

import java.net.InetAddress;

import tuwien.auto.calimero.KNXException;
import tuwien.auto.calimero.knxnetip.Discoverer;

public class KnxUtils {



    
	/** 
	 * @return 
	 */
	public KnxUtils(){
    }

    public void discoverServersPrint(){

        System.out.println("This example discovers all active KNXnet/IP servers in your IP network");

		try {
			// set true to be aware of Network Address Translation (NAT) during discovery
			final boolean useNAT = false;
			// request multicast responses (as opposed to unicast responses) from discovered servers
			final boolean requestMulticastResponse = false;

			final Discoverer discoverer = new Discoverer(null, 0, useNAT, requestMulticastResponse);
			discoverer.startSearch(3, true);

			// print out responding servers
			discoverer.getSearchResponses().forEach(r -> System.out.format("%s %s <=> %s%n",
					r.getNetworkInterface().getName(),
					r.getAddress(),
					r.getResponse().toString().replace(", ", "\n\t")));
			
		}
		catch (KNXException | InterruptedException e) {
			// KNXException: all Calimero-specific checked exceptions are subtypes of KNXException
			// InterruptedException: longer tasks that might block are interruptible, e.g., server search.
			System.out.println("Error during KNXnet/IP discovery: " + e);
		}

	}
	

	
	/** 
	 * @return InetAddress
	 */
	// This method return the InetAdrress of the first server found in the network 
	public InetAddress discoverServer(){

        System.out.println("This example discovers all active KNXnet/IP servers in your IP network");

		try {
			// set true to be aware of Network Address Translation (NAT) during discovery
			final boolean useNAT = false;
			// request multicast responses (as opposed to unicast responses) from discovered servers
			final boolean requestMulticastResponse = false;

			final Discoverer discoverer = new Discoverer(null, 0, useNAT, requestMulticastResponse);
			discoverer.startSearch(3, true);


			if(discoverer.getSearchResponses().size() >= 0){
				System.out.println(discoverer.getSearchResponses().get(0).getResponse().getControlEndpoint().getAddress());
				return discoverer.getSearchResponses().get(0).getResponse().getControlEndpoint().getAddress();
			}else{
				return null;
			}
			
		}
		catch (KNXException | InterruptedException e) {
			// KNXException: all Calimero-specific checked exceptions are subtypes of KNXException
			// InterruptedException: longer tasks that might block are interruptible, e.g., server search.
			System.out.println("Error during KNXnet/IP discovery: " + e);
			return null;
		}

    }

}