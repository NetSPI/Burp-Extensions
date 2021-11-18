package burp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class Menu implements IContextMenuFactory {
    private final IExtensionHelpers m_helpers;
    private final IBurpExtenderCallbacks m_callbacks;

    public Menu(IExtensionHelpers helpers,IBurpExtenderCallbacks callbacks) {
        m_helpers = helpers;
        m_callbacks = callbacks;
    }

    public List<JMenuItem> createMenuItems(final IContextMenuInvocation invocation) {
        List<JMenuItem> menus = new ArrayList();

        if (invocation.getToolFlag() != IBurpExtenderCallbacks.TOOL_INTRUDER && invocation.getInvocationContext() != IContextMenuInvocation.CONTEXT_MESSAGE_EDITOR_REQUEST){
            return menus;
        }

        JMenuItem sendXMLToRepeater = new JMenuItem("Convert to XML");
        JMenuItem sendJSONToRepeater = new JMenuItem("Convert to JSON");


        sendXMLToRepeater.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IHttpRequestResponse iReqResp = invocation.getSelectedMessages()[0];
                try {
                    byte[] request = Utilities.convertToXML(m_helpers, m_callbacks, iReqResp);
                    if (request != null) {

                        iReqResp.setRequest(request);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        });

        sendJSONToRepeater.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IHttpRequestResponse iReqResp = invocation.getSelectedMessages()[0];
                try {
                    byte[] request = Utilities.convertToJSON(m_helpers, m_callbacks, iReqResp);
                    if (request != null) {

                        iReqResp.setRequest(request);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });




        menus.add(sendXMLToRepeater);
        menus.add(sendJSONToRepeater);
        return menus;
    }

}