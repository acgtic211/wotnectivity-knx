package es.ual.acg.utils;

import java.net.InetSocketAddress;
import java.time.LocalTime;

import tuwien.auto.calimero.DataUnitBuilder;
import tuwien.auto.calimero.DetachEvent;
import tuwien.auto.calimero.KNXException;
import tuwien.auto.calimero.link.KNXNetworkLink;
import tuwien.auto.calimero.link.KNXNetworkLinkIP;
import tuwien.auto.calimero.link.medium.TPSettings;
import tuwien.auto.calimero.process.ProcessCommunicator;
import tuwien.auto.calimero.process.ProcessCommunicatorImpl;
import tuwien.auto.calimero.process.ProcessEvent;
import tuwien.auto.calimero.process.ProcessListener;

public class KnxProcessListener implements ProcessListener {


    
	/** 
	 * @param address
	 */
	public void monitorGroups(String address){
		final InetSocketAddress remote = new InetSocketAddress(address, 3671);
		try (KNXNetworkLink knxLink = KNXNetworkLinkIP.newTunnelingLink(null, remote, false, TPSettings.TP1);
		     ProcessCommunicator pc = new ProcessCommunicatorImpl(knxLink)) {

			// start listening to group notifications using a process listener
			pc.addProcessListener(this);
			System.out.println("Monitoring KNX network using KNXnet/IP server " + address + " ...");

			while (knxLink.isOpen()) Thread.sleep(1000);
		}
		catch (final KNXException | InterruptedException | RuntimeException e) {
			System.err.println(e);
		}
    }
    
    
	/** 
	 * @param svc
	 * @param e
	 */
	@Override
	public void groupWrite(final ProcessEvent e) { print("write.ind", e); }
	
	/** 
	 * @param svc
	 * @param e
	 */
	@Override
	public void groupReadRequest(final ProcessEvent e) { print("read.req", e); }
	
	/** 
	 * @param svc
	 * @param e
	 */
	@Override
	public void groupReadResponse(final ProcessEvent e) { print("read.res", e); }
	
	/** 
	 * @param svc
	 * @param e
	 */
	@Override
	public void detached(final DetachEvent e) {}

	
	/** 
	 * @param svc
	 * @param e
	 */
	// Called on every group notification issued by a datapoint on the KNX network. It prints the service primitive,
	// KNX source and destination address, and Application Service Data Unit (ASDU) to System.out.
	private static void print(final String svc, final ProcessEvent e)
	{
		try {
			System.out.println(LocalTime.now() + " " + e.getSourceAddr() + "->" + e.getDestination() + " " + svc
					+ ": " + DataUnitBuilder.toHex(e.getASDU(), ""));
		}
		catch (final RuntimeException ex) {
			System.err.println(ex);
		}
	}

}