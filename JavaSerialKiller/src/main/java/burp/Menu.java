package burp;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class Menu implements IContextMenuFactory {
    private IExtensionHelpers helpers;
    private IBurpExtenderCallbacks callbacks;
    private JavaSerialKillerTab tab;
    JavaSerialKiller sk;


    public Menu(IBurpExtenderCallbacks callbacks) {
        helpers = callbacks.getHelpers();
        this.callbacks = callbacks;
    }

    public List<JMenuItem> createMenuItems(final IContextMenuInvocation invocation) {
        ArrayList<JMenuItem> menus = new ArrayList<JMenuItem>();

        JMenuItem sendToSerialKiller = new JMenuItem("Send to Java Serial Killer");

        sendToSerialKiller.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent arg0) {

            }


            public void mouseEntered(MouseEvent arg0) {
            }


            public void mouseExited(MouseEvent arg0) {
            }


            public void mousePressed(MouseEvent arg0) {

            }


            public void mouseReleased(MouseEvent arg0) {
                if (tab == null){
                    tab = new JavaSerialKillerTab(callbacks);
                    sk = new JavaSerialKiller(tab);
                }

                IHttpRequestResponse iReqResp = invocation.getSelectedMessages()[0];
                sk.sendToTab(iReqResp);

            }
        });

        menus.add(sendToSerialKiller);
        return menus;
    }

}