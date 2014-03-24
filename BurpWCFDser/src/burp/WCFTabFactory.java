package burp;

public class WCFTabFactory implements IMessageEditorTabFactory {
	private IBurpExtenderCallbacks m_callbacks;
	private IExtensionHelpers m_helpers;

	public WCFTabFactory(IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers) {
		m_callbacks = callbacks;
		m_helpers = helpers;
	}

	@Override
	public IMessageEditorTab createNewInstance(IMessageEditorController controller, boolean editable) {
		WCFDeserializerTab wcfDeserializerTab = new WCFDeserializerTab(controller, editable, m_callbacks, m_helpers);
		return wcfDeserializerTab;
	}

}
