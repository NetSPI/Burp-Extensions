package extensionTemplate;

import burp.IContextMenuFactory;
import burp.IContextMenuInvocation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/*
Create context menu items.
Functions:
    1. Add a message to our split pane
    2. Show a message popup
 */
public class ExtensionTemplateMenu implements IContextMenuFactory {

	private ExtensionTemplateMainTab mainTab;

	public ExtensionTemplateMenu(ExtensionTemplateMainTab mainTab) {
		this.mainTab = mainTab;
	}

	@Override
	public List<JMenuItem> createMenuItems(IContextMenuInvocation invocation) {
		// Create the menu item list that we're going to return
		List<JMenuItem> menuItems = new ArrayList<>();

		// Create a nested menu, just to show that we can
		JMenu topLevelMenu = new JMenu("Extension Template Menu");

		// Add menu items and listeners
		JMenuItem menuItem1 = new JMenuItem("Add Message Tab");
		menuItem1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainTab.addMessageTab(invocation.getSelectedMessages()[0]);
			}
		});
		topLevelMenu.add(menuItem1);

		JMenuItem menuItem2 = new JMenuItem("Show popup");
		menuItem2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new MessagePopup("Hello World!");
			}
		});
		topLevelMenu.add(menuItem2);

		// Return our list
		menuItems.add(topLevelMenu);
		return menuItems;
	}
}
