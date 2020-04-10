package extensionTemplate;

import burp.IBurpExtenderCallbacks;
import burp.IHttpRequestResponse;
import burp.ITab;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
Serves as the first tab in our suite tab. Just contains a summary of the extension features and a method
for adding tabs.
 */
public class ExtensionTemplateMainTab implements ITab {

	private final String tabCaption = "Extension Template";
	private JTabbedPane tabbedPane;
	private IBurpExtenderCallbacks callbacks;
	private int tabs = 0;

	public ExtensionTemplateMainTab(IBurpExtenderCallbacks callbacks) {
		this.tabbedPane = new JTabbedPane();
		this.callbacks = callbacks;

		// Add our main tab
		this.tabbedPane.addTab("Main", buildMainTab());

		this.callbacks.customizeUiComponent(this.tabbedPane);
		this.callbacks.addSuiteTab(ExtensionTemplateMainTab.this);
	}

	// Add a tab to our tabbed pane
	public void addMessageTab(IHttpRequestResponse message) {
		this.tabs++;
		ExtensionTemplateTab extensionTemplateTab = new ExtensionTemplateTab(message, this.callbacks);
		this.tabbedPane.add(extensionTemplateTab.getUiComponent());
		int tabIndex = this.tabbedPane.getTabCount() - 1;
		this.tabbedPane.setTabComponentAt(tabIndex, new ButtonTabComponent(this.tabs, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPane.remove(extensionTemplateTab.getUiComponent());
			}
		}));
		this.tabbedPane.setSelectedIndex(tabIndex);
	}

	@Override
	public String getTabCaption() {
		return tabCaption;
	}

	@Override
	public Component getUiComponent() {
		return this.tabbedPane;
	}

	// Construct the component for our first tab
	private Component buildMainTab() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JLabel header = new JLabel("Extension Template");
		header.setFont(header.getFont().deriveFont(Font.ITALIC, 32.0f));
		header.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(header);

		JLabel featuresHeader = new JLabel("Current Features:");
		featuresHeader.setFont(featuresHeader.getFont().deriveFont(24f));
		featuresHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(featuresHeader);

		JLabel featuresList = new JLabel("<html><ul>" +
				"<li>Suite tab with tabbed pane</li>" +
				"<li>Tab with split pane</li>" +
				"<li>Use of IMessageEditor/ITextEditor</li>" +
				"<li>Basic proxy listener</li>" +
				"<li>Context menu items</li>" +
				"<li>Basic logging functionality</li>" +
				"<li>Popup window</li>" +
				"<li>Implementation of an IMessageEditorTabFactory</li>");
		featuresList.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(featuresList);
		return panel;
	}
}
