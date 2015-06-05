package burp;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class Menu implements IContextMenuFactory {
    private final IExtensionHelpers m_helpers;

    public Menu(IExtensionHelpers helpers) {
        m_helpers = helpers;
    }

    public List<JMenuItem> createMenuItems(final IContextMenuInvocation invocation) {
        List<JMenuItem> menus = new ArrayList();

        if (invocation.getToolFlag() != IBurpExtenderCallbacks.TOOL_INTRUDER && invocation.getInvocationContext() != IContextMenuInvocation.CONTEXT_MESSAGE_EDITOR_REQUEST){
            return menus;
        }

        JMenuItem sendXMLToRepeater = new JMenuItem("Convert to XML");
        JMenuItem sendJSONToRepeater = new JMenuItem("Convert to JSON");
        sendXMLToRepeater.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent arg0) {

            }


            public void mouseEntered(MouseEvent arg0) {
            }


            public void mouseExited(MouseEvent arg0) {
            }


            public void mousePressed(MouseEvent arg0) {

            }


            public void mouseReleased(MouseEvent arg0) {
                IHttpRequestResponse iReqResp = invocation.getSelectedMessages()[0];
                try {
                    byte[] request = Utilities.convertToXML(m_helpers, iReqResp);
                    if (request != null) {

                        iReqResp.setRequest(request);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        sendJSONToRepeater.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent arg0) {

            }


            public void mouseEntered(MouseEvent arg0) {
            }


            public void mouseExited(MouseEvent arg0) {
            }


            public void mousePressed(MouseEvent arg0) {

            }


            public void mouseReleased(MouseEvent arg0) {
                IHttpRequestResponse iReqResp = invocation.getSelectedMessages()[0];
                try {
                    byte[] request = Utilities.convertToJSON(m_helpers, iReqResp);
                    if (request != null) {

                        iReqResp.setRequest(request);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        menus.add(sendXMLToRepeater);
        menus.add(sendJSONToRepeater);
        return menus;
    }

}