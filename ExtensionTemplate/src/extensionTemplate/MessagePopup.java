package extensionTemplate;

import javax.swing.*;
import java.awt.*;

public class MessagePopup {
	private final String BURP_FRAME_TITLE = "Burp Suite";

	public MessagePopup(String message) {
		// Create frame
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setMinimumSize(new Dimension(500, 300));
		frame.setLocationByPlatform(true);
		Frame burpFrame = getBurpFrame();
		if (burpFrame != null) {
			frame.setLocationRelativeTo(burpFrame);
		}
		frame.setVisible(true);

		// Create panel for frame
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.NORTH;
		panel.add(new JLabel(message), constraints);

		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(e -> {
			frame.dispose();
		});
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.SOUTH;
		constraints.fill = GridBagConstraints.VERTICAL;
		panel.add(closeButton, constraints);
		frame.add(panel);
	}

	public Frame getBurpFrame() {
		for (Frame f : Frame.getFrames()) {
			if (f.isVisible() && f.getTitle().startsWith(BURP_FRAME_TITLE)) {
				return f;
			}
		}
		return null;
	}
}
