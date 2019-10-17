package burp;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Menu implements IContextMenuFactory {

    IBurpExtenderCallbacks callbacks;
    IExtensionHelpers helpers;

    public Menu(IBurpExtenderCallbacks callbacks) {
        this.callbacks = callbacks;
        this.helpers = callbacks.getHelpers();
    }

    @Override
    public List<JMenuItem> createMenuItems(IContextMenuInvocation invocation) {
        List<JMenuItem> menuItemList = new ArrayList<>();
        JMenuItem menuItem = new JMenuItem("Copy in-scope URLs");
        menuItem.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                copyUrls(getUrls());
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        menuItemList.add(menuItem);
        return menuItemList;
    }

    private ArrayList<String> getUrls() {
        IHttpRequestResponse[] siteMap = this.callbacks.getSiteMap(null);
        ArrayList<String> inScopeUrls = new ArrayList<>();

        for (IHttpRequestResponse requestObj : siteMap) {
            URL url = this.helpers.analyzeRequest(requestObj).getUrl();

            if (callbacks.isInScope(url)) {
                URI reformedUri = null;
                try {
                    if (url.getProtocol().equals("http") && url.getPort() == 80 ||
                            url.getProtocol().equals("https") && url.getPort() == 443) {
                        reformedUri = new URI(url.getProtocol(), url.getHost(), url.getPath(), null);
                    } else {
                        reformedUri = new URI(url.getProtocol(), null, url.getHost(), url.getPort(), url.getPath(), null, null);
                    }
                } catch(Exception e) {
                    Logger.log(e.getMessage());
                    continue;
                }

                if (!inScopeUrls.contains(reformedUri.toString())) {
                    inScopeUrls.add(reformedUri.toString());
                }
            }
        }

        Collections.sort(inScopeUrls);
        return inScopeUrls;
    }

    private void copyUrls(ArrayList<String> urls) {
        StringBuilder urlStringBuilder = new StringBuilder();
        for (String url : urls) {
            urlStringBuilder.append(url + System.lineSeparator());
        }
        StringSelection selection = new StringSelection(urlStringBuilder.toString().trim());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }

}
