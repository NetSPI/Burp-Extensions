package burp;

import extensionTemplate.*;

public class BurpExtender implements IBurpExtender {
	private final String extensionName = "Extension Template";

	@Override
	public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
		// Set extension name
		callbacks.setExtensionName(extensionName);

		// Initialize our Logger
		Logger.init(callbacks);

		// Create our main extension tab
		ExtensionTemplateMainTab mainTab = new ExtensionTemplateMainTab(callbacks);

		// Create context menu item
		callbacks.registerContextMenuFactory(new ExtensionTemplateMenu(mainTab));

		// Register our message editor
		callbacks.registerMessageEditorTabFactory(new ExtensionTemplateEditorTabFactory(callbacks));

		// Register our proxy listener
		callbacks.registerProxyListener(new ProxyListener());
	}
}
