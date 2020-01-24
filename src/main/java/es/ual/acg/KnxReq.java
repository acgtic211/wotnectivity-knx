package es.ual.acg;


import java.net.InetSocketAddress;

import es.ual.acg.utils.KnxProcessListener;
import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.KNXException;
import tuwien.auto.calimero.datapoint.Datapoint;
import tuwien.auto.calimero.datapoint.StateDP;
import tuwien.auto.calimero.dptxlator.DPTXlator8BitSigned;
import tuwien.auto.calimero.link.KNXNetworkLink;
import tuwien.auto.calimero.link.KNXNetworkLinkIP;
import tuwien.auto.calimero.link.medium.TPSettings;
import tuwien.auto.calimero.process.ProcessCommunicator;
import tuwien.auto.calimero.process.ProcessCommunicatorImpl;
import tuwien.auto.calimero.process.ProcessListener;

public class KnxReq {


	InetSocketAddress remote;
	ProcessListener pl;

	
	/** 
	 * @return 
	 */
	public KnxReq(){
		this.pl = new KnxProcessListener();
	}

	public KnxReq(ProcessListener pl){
		this.pl = pl;
	}

	
	/** 
	 * @param address
	 * @param group
	 * @param datatype
	 * @return String
	 * @throws Exception
	 */
	public String getStatus(String address, String group, String datatype) throws Exception {
	
		this.remote = new InetSocketAddress(address, 3671);
		// Create our network link, and pass it to a process communicator
		try (KNXNetworkLink knxLink = KNXNetworkLinkIP.newTunnelingLink(null,this.remote, false, TPSettings.TP1);
				
			ProcessCommunicator pc = new ProcessCommunicatorImpl(knxLink)) {

			System.out.println("read value from datapoint " + group);

			GroupAddress groupAddress = new GroupAddress(group);

			

			// Uncomment the next line, if you want to write back the same value to the KNX network
			// pc.write(group, value);

			final Datapoint dp = new StateDP(groupAddress, "my datapoint "+groupAddress.toString());
			

			System.out.println(DPTXlator8BitSigned.DPT_VALUE_1_UCOUNT.getID());
			//dp.setDPT(0, KnxUtils.getTranslatorDPT(String value));

			dp.setDPT(0, datatype);
			String result = pc.read(dp).toString();

			System.out.println("datapoint " + group + " value = " + result);
			
			//JCR: Review
			pc.close();
			knxLink.close();
			
			return result;
		}
		catch (KNXException | InterruptedException e) {
			throw new Exception("Error accessing KNX datapoint: " + e.getMessage());
		}
	}

	public void subscribeToKNXBuffer(String address, String group, String datatype) throws Exception{

		final InetSocketAddress remote = new InetSocketAddress(address, 3671);
		try (KNXNetworkLink knxLink = KNXNetworkLinkIP.newTunnelingLink(null, remote, false, TPSettings.TP1);
		     ProcessCommunicator pc = new ProcessCommunicatorImpl(knxLink)) {

			// start listening to group notifications using a process listener
			pc.addProcessListener(pl);
			System.out.println("Monitoring KNX network using KNXnet/IP server " + address + " ...");

			while (knxLink.isOpen()) Thread.sleep(1000);
		}
		catch (final KNXException | InterruptedException | RuntimeException e) {
			System.err.println(e);
		}
			

	}
	
	/** 
	 * @param address
	 * @param group
	 * @param datatype
	 * @param value
	 * @throws Exception
	 */
	public void setStatus(String address, String group, String datatype, String value) throws Exception {
	
		this.remote = new InetSocketAddress(address, 3671);
		//final InetSocketAddress local = new InetSocketAddress(localip, 0);
		// Create our network link, and pass it to a process communicator
		try (KNXNetworkLink knxLink = KNXNetworkLinkIP.newTunnelingLink(null, this.remote, false, TPSettings.TP1);
			
			ProcessCommunicator pc = new ProcessCommunicatorImpl(knxLink)) {

			System.out.println("read value from datapoint " + group);

			GroupAddress groupAddress = new GroupAddress(group);

			// Uncomment the next line, if you want to write back the same value to the KNX network
			// pc.write(group, value);

			final Datapoint dp = new StateDP(groupAddress, "my datapoint "+groupAddress.toString());
			
			//dp.setDPT(0, KnxUtils.getTranslatorDPT(String value));

			dp.setDPT(0, datatype);
			pc.write(dp, value);

			//JCR: Review
			pc.close();
			knxLink.close();

		}
		catch (KNXException | InterruptedException e) {
			throw new Exception("Error accessing KNX datapoint: " + e.getMessage());
		}
	}
		
}
	
