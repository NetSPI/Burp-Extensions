package extensionTemplate;

import burp.IBurpExtenderCallbacks;
import burp.IHttpRequestResponse;
import burp.ITab;
import burp.ITextEditor;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;

/*
This is the component that we use to produce tabs in our tabbed pane
Creates a split pane with the request and response in text editors
 */
public class ExtensionTemplateTab implements ITab {

	private IBurpExtenderCallbacks callbacks;
	private JSplitPane splitPane;
	private JPanel topPane;

	public ExtensionTemplateTab(IHttpRequestResponse message, final IBurpExtenderCallbacks callbacks) {
		this.callbacks = callbacks;

		this.topPane = new JPanel();
		topPane.setLayout(new BoxLayout(topPane, BoxLayout.Y_AXIS));

		// Build split pane
		this.splitPane = new JSplitPane();
		this.splitPane.setResizeWeight(0.5);
		JPanel leftPane = new JPanel();
		leftPane.setLayout(new GridBagLayout());
		leftPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
		this.splitPane.setLeftComponent(leftPane);
		JPanel rightPane = new JPanel();
		rightPane.setLayout(new GridBagLayout());
		rightPane.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
		this.splitPane.setRightComponent(rightPane);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(2, 2, 2, 2);


		// Make our headers
		JLabel requestHeader = new JLabel("Request");
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.NONE;
		Font normalFont = requestHeader.getFont();
		Font boldFont = new Font(normalFont.getFontName(), Font.BOLD, normalFont.getSize());
		requestHeader.setFont(boldFont);
		leftPane.add(requestHeader, constraints);

		JLabel responseHeader = new JLabel("Response");
		responseHeader.setFont(boldFont);
		rightPane.add(responseHeader, constraints);

		topPane.add(splitPane);

		// Create two editor panels. One for requests and one for responses
		JPanel requestPanel = new JPanel();
		requestPanel.setLayout(new BoxLayout(requestPanel, BoxLayout.Y_AXIS));
		Border header = BorderFactory.createMatteBorder(4, 0, 0, 0, Color.LIGHT_GRAY);
		requestPanel.setBorder(header);
		ITextEditor requestEditor = callbacks.createTextEditor();
		requestEditor.setText(message.getRequest());
		constraints.gridy = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1;
		constraints.weighty = 1;
		requestPanel.add(requestEditor.getComponent());
		leftPane.add(requestPanel, constraints);

		JPanel responsePanel = new JPanel();
		responsePanel.setLayout(new BoxLayout(responsePanel, BoxLayout.Y_AXIS));
		responsePanel.setBorder(header);
		ITextEditor responseEditor = callbacks.createTextEditor();
		responseEditor.setText(message.getResponse());
		responsePanel.add(responseEditor.getComponent());
		rightPane.add(responsePanel, constraints);

		callbacks.customizeUiComponent(this.topPane);
	}

	@Override
	public String getTabCaption() {
		return "Extension Template";
	}

	@Override
	public Component getUiComponent() {
		return this.topPane;
	}
}