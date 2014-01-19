package ir.mod.tavana.toranj.services.communication;

import jade.Boot;

import java.net.*;
import java.util.*;

import org.apache.soap.*;
import org.apache.soap.encoding.SOAPMappingRegistry;
import org.apache.soap.encoding.soapenc.StringDeserializer;
import org.apache.soap.rpc.*;
import org.apache.soap.util.xml.QName;

public class snClient {

	public void send(String source, String target, int mtype, String msg) {
		try {
			URL url = new URL("http://localhost:7070/sn-services/ws/");
			Call call = new Call();
			call.setTargetObjectURI("urn:BroadcastingFunctions");
			call.setMethodName("send");
			call.setEncodingStyleURI(Constants.NS_URI_SOAP_ENC);

			SOAPMappingRegistry smr = new SOAPMappingRegistry();
			StringDeserializer sd = new StringDeserializer();
			smr.mapTypes(Constants.NS_URI_SOAP_ENC, new QName("", "sendReturn"), null, null, sd);
			call.setSOAPMappingRegistry(smr);

			// call.setEncodingStyleURI(Constants.NS_URI_LITERAL_XML);
			Vector params = new Vector();
			params.addElement(new Parameter("msg", String.class, msg, Constants.NS_URI_SOAP_ENC));
			params.addElement(new Parameter("mtype", String.class, mtype, Constants.NS_URI_SOAP_ENC));
			params.addElement(new Parameter("priority", String.class, 1, Constants.NS_URI_SOAP_ENC));
			params.addElement(new Parameter("lifetime", String.class, 100, Constants.NS_URI_SOAP_ENC));
			params.addElement(new Parameter("forwardList", String.class, "", Constants.NS_URI_SOAP_ENC));
			params.addElement(new Parameter("source", String.class, source, Constants.NS_URI_SOAP_ENC));
			params.addElement(new Parameter("target", String.class, target, Constants.NS_URI_SOAP_ENC));

			call.setParams(params);

			System.out.print("The SOAP Server says: ");

			Response resp = call.invoke(url, "");
			if (resp.generatedFault()) {
				Fault fault = resp.getFault();
				System.out.println("\nOuch, the call failed: ");
				System.out.println("  Fault Code   = " + fault.getFaultCode());
				System.out.println("  Fault String = " + fault.getFaultString());
			} else {
				Parameter result = resp.getReturnValue();
				System.out.print(result.getValue());
				System.out.println();
			}
		} catch (Exception e) {
			System.out.println("ERROR: ");
			e.printStackTrace();
		}
	}

	private void callJade() throws Exception {
		// run jade platform
		String[] local_args1 = { "-gui"};
		Boot jade_agent = new Boot(local_args1);
		
		// run CommunicationServiceAgent
/*		String[] local_args2 = { "-gui", "-container",
				"CommunicationSAgent1:ir.mod.tavana.toranj.services.communication.CommunicationServiceAgent(CommunicationFunctions false)",
				"-name", "WSIGPlatform" };
		Boot communication_agent = new Boot(local_args2);
*/		
		// run MathServiceAgent
/*		String[] local_args3 = { "-gui", "-container",
				"MathSAgent1:ir.mod.tavana.toranj.wsig.examples.MathAgent(MathFunctions false)",
				"-name", "WSIGPlatform" };
		Boot math_agent = new Boot(local_args2);
*/
		// run WSIGServiceAgent
/*		String[] local_args4 = { "-gui", "-container",
				"WSIGSAgent1:ir.mod.tavana.toranj.wsig.agent.WSIGAgent(WSIGFunctions false)",
				"-name", "WSIGPlatform" };
		Boot wsig_agent = new Boot(local_args2);
*/
		// run PollingServiceAgent
		String[] polling_args = { "-gui",
				"-services", "jade.core.event.NotificationService;jade.core.mobility.AgentMobilityService",
//				"-meta-db",	"JADE_persistence.properties",
				"-container", "PollingSAgent:ir.mod.tavana.toranj.services.polling.PollingServiceAgent(PollingFunctions false)",
				"-name", "WSIGPlatform" };
		Boot polling_agent = new Boot(polling_args);

		String[] mediator_args = { "-gui",
				"-services", "jade.core.event.NotificationService;jade.core.mobility.AgentMobilityService",
//				"-meta-db",	"JADE_persistence.properties",
				"-container", "MediatorSAgent:ir.mod.tavana.toranj.services.mediator.MediatorAgent(MediatorFunctions false)",
				"-name", "WSIGPlatform" };
		Boot mediator_agent = new Boot(mediator_args);

		String[] avatr_args = {"-gui",
				"-services", "jade.core.event.NotificationService;jade.core.mobility.AgentMobilityService",
//				"-meta-db",	"JADE_persistence.properties",
				"-container", "AvatarAgent:ir.mod.tavana.toranj.services.avatar.AvatarAgent(AvatarFunctions true)",
				"-name", "WSIGTestPlatform"};
		Boot avatar_agent = new Boot(avatr_args);

		//run BroadCastinAgent
/*		String[] local_args7 = { "-gui", "-container",
				"BroadcastingSAgent1:ir.mod.tavana.toranj.services.broadcasting.BroadcastingServiceAgent(BroadcastingFunctions false)",
				"-name", "WSIGPlatform" };
		Boot broadcasting_agent = new Boot(local_args7);
*/
		//run Dummy DNSAgent
/*		String[] local_args8 = { "-gui", "-container",
				"DNSSAgent1:ir.mod.tavana.toranj.services.dns.DNSServiceAgent(DNSFunctions false)",
				"-name", "WSIGPlatform" };
		Boot dns_agent = new Boot(local_args8);
*/
		// run Box Agent
/*		String[] local_args3 = { "-gui", "-container",
				"BoxAgent3:ir.mod.tavana.toranj.services.box.BoxAgent(BoxFunctions false)",
				"-name", "WSIGPlatform" };
		Boot box_agent3 = new Boot(local_args3);
*/
		// run Box Agent
/*		String[] local_args4 = { "-gui", "-container",
				"BoxAgent4:ir.mod.tavana.toranj.services.box.BoxAgent(BoxFunctions false)",
				"-name", "WSIGPlatform" };
		Boot box_agent4 = new Boot(local_args4);
*/
		// run Box Agent
/*		String[] local_args5 = { "-gui", "-container",
				"BoxAgent5:ir.mod.tavana.toranj.services.box.BoxAgent(BoxFunctions false)",
				"-name", "WSIGPlatform" };
		Boot box_agent5 = new Boot(local_args5);
*/
		// run Box Agent
/*		String[] local_args6 = { "-gui", "-container",
				"BoxAgent6:ir.mod.tavana.toranj.services.box.BoxAgent(BoxFunctions false)",
				"-name", "WSIGPlatform" };
		Boot box_agent6 = new Boot(local_args6);
*/
		// run Box Agent
/*		String[] local_args9 = { "-gui", "-container",
				"BoxAgent1:ir.mod.tavana.toranj.services.box.BoxAgent(BoxFunctions false)",
				"-name", "WSIGPlatform" };
		Boot box_agent = new Boot(local_args9);
*/
		// run Box Agent
/*		String[] local_args11 = { "-gui", "-container",
				"BoxAgent2:ir.mod.tavana.toranj.services.box.BoxAgent(BoxFunctions false)",
				"-name", "WSIGPlatform" };
		Boot box_agent2 = new Boot(local_args11);
*/
		// run Box Agent
/*		String[] local_args12 = { "-gui", "-container",
				"BoxAgent3:ir.mod.tavana.toranj.services.box.BoxAgent(BoxFunctions false)",
				"-name", "WSIGPlatform" };
		Boot box_agent3 = new Boot(local_args12);
*/
		// run Box Agent
/*		String[] local_args13 = { "-gui", "-container",
				"BoxAgent4:ir.mod.tavana.toranj.services.box.BoxAgent(BoxFunctions false)",
				"-name", "WSIGPlatform" };
		Boot box_agent4 = new Boot(local_args13);
*/
		// run Task Allocation Agent
/*		String[] local_args10 = { "-gui", "-container",
				"TaskAllocationAgent1:ir.mod.tavana.toranj.services.task_allocation.TaskAllocationAgent(TaskAllocationFunctions false)",
				"-name", "WSIGPlatform" };
		Boot task_allocation_agent = new Boot(local_args10);
*/
		// run Dummy TaskAlloc Agent
/*		String[] local_args11 = { "-gui", "-container",
				"TADummyAgent1:ir.mod.tavana.toranj.services.task_allocation.TADummyAgent(TADummyAgentFunctions false)",
				"-name", "WSIGPlatform" };
		Boot dummy_agent_agent = new Boot(local_args11);
*/
		//run Dummy Agent
/*		String[] local_args9 = { "-gui", "-container",
				"DummyAgent1:ir.mod.tavana.toranj.services.broadcasting.BCDummyAgent(DummyFunctions false)",
				"-name", "WSIGPlatform" };
		Boot bc_dummy_agent = new Boot(local_args9);
*/
/*
 * 		//run BuyingAgent
		String[] local_args8 = { "-gui", "-container",
				"BookBuyerAgentSAgent1:ir.mod.tavana.toranj.sample.buycars.BookBuyerAgent(BookBuyerFunctions false)",
				"-name", "WSIGPlatform" };
		Boot buying_agent = new Boot(local_args8);
				
		//run SellingAgent
		String[] local_args9 = { "-gui", "-container",
				"BookSellerSAgent1:ir.mod.tavana.toranj.sample.buycars.BookSellerAgent(BookSellerFunctions false)",
				"-name", "WSIGPlatform" };
		Boot selling_agent = new Boot(local_args9);
*
*/

		// run UserAgents
/*		BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
		String line = "";
		int id = 1;
		while ((line = reader.readLine()) != null) {
			String[] line_array = line.split("\\s*\\t\\s*");
			String[] local_args = { "-gui", "-container", "" };
			StringBuffer params = new StringBuffer("UserAgent" + id
					+ ":ir.mod.tavana.toranj.services.communication.UserAgent(");

			for (int i = 0; i < line_array.length; i++) {
				String weight = line_array[i];
				if (line_array[i].equals("0")) {
					if (i != 0)
						params.delete(params.length() - 1, params.length());
					params.append(")");
					local_args[2] = params.toString();
					Boot user_agent = new Boot(local_args);
				break;
				} else if (!weight.equals("-1")) {
					params.append("UserAgent" + (i + 1) + ":" + weight + " ");
				}
			}
			id++;
		}
		reader.close();
*/		
		// run Physical agents
/*		String[] local_args4 = {
				"-gui",
				"-container",
				"SearchRadar1:ir.mod.tavana.toranj.agents.physical.Search_radar()"};
		Boot search_agent1 = new Boot(local_args4);

		String[] local_args5 = {
				"-gui",
				"-container",
				"SearchRadar2:ir.mod.tavana.toranj.agents.physical.Search_radar()"};
		Boot search_agent2 = new Boot(local_args5);
*/		
/*		String[] local_args6 = {
				"-gui",
				"-container",
				"SP1:ir.mod.tavana.toranj.agents.post.SensorPost()"};
		Boot sp = new Boot(local_args6);
*/	}

	public void runClient() throws Exception {
		callJade();
		// show the menu
		System.out.println("Use the following command line to test the Communication Service Agent:");
		System.out.println("SEND(<source>,<target>,<mtype>,<msg>)");
		System.out.println("-------------------------------------");
		System.out.println("<source>: source agent");
		System.out.println("<target>: target agent");
		System.out.println("<mtype> : 1 for direct communication and 2 for multi-hop communication");
		System.out.println("<msg>   : message content");
		while (true) {
			Scanner sc = new Scanner(System.in);
			String cmd = sc.nextLine();
			// String cmd = "SEND(UserAgent1, UserAgent5, 1, test)";
			cmd = cmd.replaceAll("SEND|\\(|\\)", "");
			String[] cmd_array = cmd.split("\\s*,\\s*");
			send(cmd_array[0], cmd_array[1], Integer.parseInt(cmd_array[2]), cmd_array[3]);
		}

	}
	
	public static void main(String[] args) throws Exception {
		snClient client = new snClient();
		client.runClient();
	}
}
