package burp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Menu implements IContextMenuFactory {
    private IExtensionHelpers helpers;
    private IBurpExtenderCallbacks callbacks;
    private JSWSParserTab tab;
    public static Timer timer;

    public Menu(IBurpExtenderCallbacks callbacks) {
        helpers = callbacks.getHelpers();
        tab = new JSWSParserTab(callbacks);
        this.callbacks = callbacks;
        timer = new Timer();
    }

    public List<JMenuItem> createMenuItems(
            final IContextMenuInvocation invocation) {
        List<JMenuItem> list;
        list = new ArrayList<JMenuItem>();

        JMenuItem item = new JMenuItem("Parse JSWS");

        item.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {

            }


            public void mousePressed(MouseEvent e) {

            }


            public void mouseReleased(MouseEvent e) {
                JSWSParser parser = new JSWSParser(helpers, tab, callbacks);
                try {
                    new Worker(parser,invocation, tab, callbacks).execute();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }


            public void mouseEntered(MouseEvent e) {

            }


            public void mouseExited(MouseEvent e) {

            }
        });
        list.add(item);

        return list;
    }

}

class Worker extends SwingWorker<Void,Void> {

    private JDialog dialog = new JDialog();
    private JSWSParser parser;
    private IContextMenuInvocation invocation;
    private JSWSParserTab tab;
    private IBurpExtenderCallbacks callbacks;
    private int status;

    public Worker(JSWSParser parser, IContextMenuInvocation invocation, JSWSParserTab tab, IBurpExtenderCallbacks callbacks) {
        JProgressBar progressBar = new JProgressBar();
        progressBar.setString("Parsing JSWS");
        progressBar.setStringPainted(true);
        progressBar.setIndeterminate(true);
        dialog.getContentPane().add(progressBar);
        dialog.pack();
        dialog.setLocationRelativeTo(tab.getUiComponent().getParent());
        dialog.setModal(false);
        dialog.setVisible(true);
        this.parser = parser;
        this.invocation = invocation;
        this.tab = tab;
        this.callbacks = callbacks;
    }

    @Override
    protected Void doInBackground() throws Exception {
        status = parser.parseJSWS(invocation.getSelectedMessages()[0],callbacks);
        return null;
    }

    @Override
    protected void done() {
        dialog.dispose();
        if (status == -1) {
            JOptionPane.showMessageDialog(tab.getUiComponent().getParent(), "Error: Can't Read Response");
        } else if(status == -2){
            JOptionPane.showMessageDialog(tab.getUiComponent().getParent(), "Error: Not a JS file");
        } else if(status == -3){
            JOptionPane.showMessageDialog(tab.getUiComponent().getParent(), "Error: Can't Parse JS");
        }
        else {
            final JTabbedPane parent = (JTabbedPane) tab.getUiComponent().getParent();
            final int index = parent.indexOfComponent(tab.getUiComponent());
            parent.setBackgroundAt(index, new Color(229, 137, 1));

            Menu.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    parent.setBackgroundAt(index, new Color(0, 0, 0));
                }
            }, 5000);

        }
    }

}