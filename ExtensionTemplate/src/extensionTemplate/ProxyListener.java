package extensionTemplate;

import burp.IInterceptedProxyMessage;
import burp.IProxyListener;

import java.util.ArrayList;

/*
Very simple proxy listener that records all of the hosts which have passed through the proxy, and
logs a message every time a request to a new host is observed
 */
public class ProxyListener implements IProxyListener {
	ArrayList<String> hosts;

	public ProxyListener() {
		hosts = new ArrayList<>();
	}

	@Override
	public void processProxyMessage(boolean messageIsRequest, IInterceptedProxyMessage message) {
		String host = message.getMessageInfo().getHttpService().getHost();
		if (!hosts.contains(host)) {
			Logger.log("Request to new host: " + host);
			hosts.add(host);
		}
	}
}
