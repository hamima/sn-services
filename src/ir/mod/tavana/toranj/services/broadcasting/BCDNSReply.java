package ir.mod.tavana.toranj.services.broadcasting;

import jade.content.Predicate;
import jade.util.leap.ArrayList;
import jade.util.leap.List;

public class BCDNSReply implements Predicate {
	
	List dnsReceivers;

	public List getDnsReceivers() {
		if (dnsReceivers == null)
			dnsReceivers = new ArrayList();
		return dnsReceivers;
	}

	public void setDnsReceivers(List dnsReceivers) {
		this.dnsReceivers = dnsReceivers;
	}

}
