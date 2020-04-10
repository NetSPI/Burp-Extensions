package extensionTemplate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
Tab component to be used for the tabs on our split pane.
Numbers tabs and adds a close button.
 */
public class ButtonTabComponent extends JPanel {

	private ActionListener listener;

	public ButtonTabComponent(int tabNum, ActionListener listener) {
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.listener = listener;

		setOpaque(false);
		//make JLabel read titles from JTabbedPane
		JLabel label = new JLabel(Integer.toString(tabNum));
		add(label);
		//add more space between the label and the button
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		//tab button
		JButton button = new TabButton(listener);
		add(button);
	}

	private class TabButton extends JButton {

		public TabButton(ActionListener listener) {
			int size = 15;
			setPreferredSize(new Dimension(size, size));
			setToolTipText("Close this tab");
			setText("x");
			//Make it transparent
			setContentAreaFilled(false);
			//No need to be focusable
			setFocusable(false);
			setBorder(BorderFactory.createEtchedBorder());
			setBorderPainted(false);
			//Making nice rollover effect
			//we use the same listener for all buttons
			addMouseListener(buttonMouseListener);
			setRolloverEnabled(true);
			addActionListener(listener);

		}
	}

	private final static MouseListener buttonMouseListener = new MouseAdapter() {
		public void mouseEntered(MouseEvent e) {
			Component component = e.getComponent();
			if (component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(true);
			}
		}

		public void mouseExited(MouseEvent e) {
			Component component = e.getComponent();
			if (component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(false);
			}
		}
	};
}
