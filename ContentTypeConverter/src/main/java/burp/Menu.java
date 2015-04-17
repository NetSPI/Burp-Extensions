package burp;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Menu implements IContextMenuFactory {
    private final IBurpExtenderCallbacks m_callbacks;
    private final IExtensionHelpers m_helpers;

    public Menu(IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers) {
        m_callbacks = callbacks;
        m_helpers = helpers;
    }

    @Override
    public List<JMenuItem> createMenuItems(final IContextMenuInvocation invocation) {
        JMenuItem sendXMLToRepeater = new JMenuItem("Convert to XML and Send to Repeater");
        JMenuItem sendJSONToRepeater = new JMenuItem("Convert to JSON and Send to Repeater");
        sendXMLToRepeater.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent arg0) {

            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
                IHttpRequestResponse[] selectedMessages = invocation.getSelectedMessages();
                for (IHttpRequestResponse iReqResp : selectedMessages) {
                    IHttpService httpService = iReqResp.getHttpService();
                    try {
                        m_callbacks.sendToRepeater(httpService.getHost(), httpService.getPort(), (httpService.getProtocol().equals("https")), Utilities.convertToXML(m_helpers, iReqResp),null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent arg0) {
            }
        });

        sendJSONToRepeater.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent arg0) {

            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
                IHttpRequestResponse[] selectedMessages = invocation.getSelectedMessages();
                for (IHttpRequestResponse iReqResp : selectedMessages) {
                    IHttpService httpService = iReqResp.getHttpService();
                    m_callbacks.sendToRepeater(httpService.getHost(), httpService.getPort(), (httpService.getProtocol().equals("https")), Utilities.convertToJSON(m_helpers, iReqResp),null);

                }
            }

            @Override
            public void mouseReleased(MouseEvent arg0) {
            }
        });

        List<JMenuItem> menus = new ArrayList();
        menus.add(sendXMLToRepeater);
        menus.add(sendJSONToRepeater);
        return menus;
    }

}