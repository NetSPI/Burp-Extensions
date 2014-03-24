package burp;


public class WcfHttpListener implements IHttpListener {

	@Override
	public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo) {
		if (messageIsRequest && new String(messageInfo.getRequest()).contains("application/soap+msbin1")) {
			if (toolFlag == IBurpExtenderCallbacks.TOOL_INTRUDER || toolFlag == IBurpExtenderCallbacks.TOOL_SCANNER) {
				byte[] currentMsg = messageInfo.getRequest();
				byte[] serializedMsg = null;
				serializedMsg = WCFConverterUtils.encodeDecodeWcfRequest("encode", new String(currentMsg));
				messageInfo.setRequest(serializedMsg);

			}
		}
	}
}