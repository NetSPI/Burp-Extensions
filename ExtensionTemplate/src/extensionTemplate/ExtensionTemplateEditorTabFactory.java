package extensionTemplate;

import burp.*;

import java.awt.*;

/*
Adds a new tab to message editors that simply shows the base64 encoded version of the message
 */
public class ExtensionTemplateEditorTabFactory implements IMessageEditorTabFactory {

	IBurpExtenderCallbacks callbacks;

	public ExtensionTemplateEditorTabFactory(IBurpExtenderCallbacks callbacks) {
		this.callbacks = callbacks;
	}

	@Override
	public IMessageEditorTab createNewInstance(IMessageEditorController controller, boolean editable) {
		return new ExtensionEditorTab(controller, editable);
	}

	class ExtensionEditorTab implements IMessageEditorTab {

		private ITextEditor editor;
		private IExtensionHelpers helpers;

		public ExtensionEditorTab(IMessageEditorController controller, boolean editable) {
			this.editor = callbacks.createTextEditor();
			this.editor.setEditable(editable);
			this.helpers = callbacks.getHelpers();
		}

		@Override
		public String getTabCaption() {
			return "Base64";
		}

		@Override
		public Component getUiComponent() {
			return editor.getComponent();
		}

		@Override
		public boolean isEnabled(byte[] content, boolean isRequest) {
			return true;
		}

		@Override
		public void setMessage(byte[] content, boolean isRequest) {
			editor.setText(helpers.base64Encode(content).getBytes());
		}

		@Override
		public byte[] getMessage() {
			if (editor.isTextModified()) {
				return helpers.base64Decode(editor.getText());
			}
			return null;
		}

		@Override
		public boolean isModified() {
			return editor.isTextModified();
		}

		@Override
		public byte[] getSelectedData() {
			return editor.getSelectedText();
		}
	}
}
