package burp;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ComponentListener;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Arrays;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.sun.media.sound.UlawCodec;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

class WCFDeserializerTab implements IMessageEditorTab {
	private boolean editable;
	WcfTextEditor txtArea;
	private byte[] currentMessage;
	private IBurpExtenderCallbacks callbacks;
	private IExtensionHelpers helpers;

	public WCFDeserializerTab(IMessageEditorController controller, boolean editable, IBurpExtenderCallbacks callbacks2, IExtensionHelpers helpers2) {
		this.editable = editable;
		callbacks = callbacks2;
		helpers = helpers2;
		// create an instance of Burp's text editor, to display our deserialized data
		
	}

	//
	// implement IMessageEditorTab
	//

	@Override
	public String getTabCaption() {
		return "WCF Deserialized";
	}

	@Override
	public Component getUiComponent() {
		txtArea = new WcfTextEditor();
		callbacks.customizeUiComponent(txtArea.getComponent());
		return txtArea.getComponent();
	}

	@Override
	public boolean isEnabled(byte[] content, boolean isRequest) {

		return true;
	}

	@Override
	public void setMessage(byte[] content, boolean isRequest) {
		if (content == null) {
			// clear our display
//			txtInput.setText(null);
//			txtInput.setEditable(false);
			txtArea.setEditable(false);
		} else {
			int bodyOffset = helpers.analyzeRequest(content).getBodyOffset();
			final String tmpFilePath = WCFConverterUtils.writeToTmpFile(Arrays.copyOfRange(content, bodyOffset, content.length));
			// Deserialize the parameter value
//			txtInput.setText(helpers.base64Decode(WCFConverterUtils.encodeDecodeWcf("decode", tmpFilePath)));
//			txtInput.setEditable(editable);
			txtArea.setEditable(editable);
			txtArea.setText(helpers.base64Decode(WCFConverterUtils.encodeDecodeWcf("decode", tmpFilePath)));
			File file = new File(tmpFilePath);
			 EventQueue.invokeLater(new Runnable() {
			         public void run() {
			        	 JPanel cp = new JPanel(new BorderLayout());
			        	 RSyntaxTextArea txtArea1 = new RSyntaxTextArea(20,60);
			        	 txtArea1.setText(new String(helpers.base64Decode(WCFConverterUtils.encodeDecodeWcf("decode", tmpFilePath))));
			        	 RTextScrollPane rTextScrollPane = new RTextScrollPane(txtArea1);
			        	 cp.add(rTextScrollPane);
			        	 cp.setVisible(true);
			         }
			      });
				file.delete();

		}
			

		currentMessage = content;
	}

	@Override
	public byte[] getMessage() {
		// Determine whether the user modified the deserialized data
		if (isModified()) {
			String tmpFilePath = WCFConverterUtils.writeToTmpFile(txtArea.getText());
			// Re-serialize the data
			byte[] newBody = helpers.base64Decode(WCFConverterUtils.encodeDecodeWcf("encode", tmpFilePath));
			File file = new File(tmpFilePath);
			file.delete();
			return WCFConverterUtils.processRequest(currentMessage, newBody);
		} else
			return currentMessage;
	}

	@Override
	public boolean isModified() {
		return !txtArea.getText().equals(currentMessage);
	}

	@Override
	public byte[] getSelectedData() {
		return txtArea.getSelectedText();
	}
}