package burp;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;

public class WcfTextEditor implements ITextEditor, ActionListener {
	RSyntaxTextArea textArea;
	private JTextField searchField;
	private JCheckBox regexCB;
	private JCheckBox matchCaseCB;
	private JCheckBox autoScrollCB;

	private JPopupMenu menu;

	@Override
	public Component getComponent() {

		JPanel cp = new JPanel(new BorderLayout());

		textArea = new RSyntaxTextArea();
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
		textArea.setCodeFoldingEnabled(true);
		textArea.setAntiAliasingEnabled(true);
		textArea.setAutoscrolls(true);
		textArea.setLineWrap(true);

		RTextScrollPane sp = new RTextScrollPane(textArea);
		sp.setFoldIndicatorEnabled(true);
		sp.setAutoscrolls(true);
		cp.add(sp);

		JButton prevButton = new JButton("<");
		prevButton.setActionCommand("FindPrev");
		prevButton.addActionListener(this);

		final JButton nextButton = new JButton(">");
		nextButton.setActionCommand("FindNext");
		nextButton.addActionListener((ActionListener) this);

		regexCB = new JCheckBox("Regex");
		matchCaseCB = new JCheckBox("Match Case");
		autoScrollCB = new JCheckBox("Auto-scroll to match when text changes");

		JButton moreOptions = new JButton("+");
		moreOptions.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				menu.show(e.getComponent(), e.getX(), e.getY());
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		menu = new JPopupMenu(" + ");

		menu.add(regexCB);
		menu.add(matchCaseCB);
		menu.add(autoScrollCB);

		// Create a toolbar with searching options.
		// final JToolBar toolBar = new JToolBar();
		final JMenuBar jmenuBar = new JMenuBar();
		searchField = new JTextField(300);

		GridBagLayout gb = new GridBagLayout();
		jmenuBar.setLayout(gb);

		GridBagConstraints c = new GridBagConstraints();
		// subpanel.setLayout(new GridBagLayout());
		c.fill = GridBagConstraints.HORIZONTAL;
		jmenuBar.add(prevButton, c);
		jmenuBar.add(moreOptions, c);
		// toolBar.add(matchCaseCB, c);
		jmenuBar.add(nextButton, c);
		// c.fill = GridBagConstraints.REMAINDER;
		c.weightx = 1;
		jmenuBar.add(searchField, c);

		cp.add(jmenuBar, BorderLayout.SOUTH);

		searchField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nextButton.doClick(0);
			}
		});
		searchField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				search(true);
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		return cp;
	}

	@Override
	public void setEditable(boolean editable) {
		textArea.setEditable(editable);
	}

	@Override
	public void setText(byte[] text) {
		String textStr = new String(text);
		textArea.setText(textStr);
		textArea.setCaretPosition(0);
		if (autoScrollCB.isSelected()) {
			textArea.setCaretPosition(0);
			search(true);
		}
	}

	@Override
	public byte[] getText() {
		return textArea.getText().getBytes();
	}

	@Override
	public boolean isTextModified() {
		return false;
	}

	@Override
	public byte[] getSelectedText() {
		return textArea.getSelectedText().getBytes();
	}

	@Override
	public int[] getSelectionBounds() {
		return new int[] { textArea.getSelectionStart(), textArea.getSelectionEnd() };
	}

	@Override
	public void setSearchExpression(String expression) {
		String text = textArea.getText();
		SearchContext context = new SearchContext();
		context.setSearchFor(text);
		context.setMatchCase(matchCaseCB.isSelected());
		context.setRegularExpression(regexCB.isSelected());
		textArea.select(text.indexOf(expression), expression.length());
	}

	public void search(boolean forward) {
		String text = searchField.getText();
		if (text.length() == 0) {
			return;
		}
		SearchContext context = new SearchContext();
		context.setMatchCase(matchCaseCB.isSelected());
		context.setRegularExpression(regexCB.isSelected());
		context.setSearchFor(text);
		context.setWholeWord(false);
		context.setMarkAll(true);
		context.setSearchForward(forward);
		SearchEngine.find(textArea, context);

	}

	public void actionPerformed(ActionEvent e) {
		// "FindNext" => search forward, "FindPrev" => search backward
		String command = e.getActionCommand();
		boolean forward = "FindNext".equals(command);
		search(forward);

	}

}
