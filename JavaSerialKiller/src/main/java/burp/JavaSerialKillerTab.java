package burp;

import java.awt.*;

import javax.swing.*;

public class JavaSerialKillerTab implements ITab {

    private final JTabbedPane tabs;
    private final IBurpExtenderCallbacks callbacks;
    static int tabCount = 0;
    static int removedTabCount = 0;

    public JavaSerialKillerTab(final IBurpExtenderCallbacks callbacks) {
        this.callbacks = callbacks;

        tabs = new JTabbedPane();

        callbacks.customizeUiComponent(tabs);

        callbacks.addSuiteTab(JavaSerialKillerTab.this);


    }

    public ChildTab createTab(String title, IHttpRequestResponse requestResponse) {

        ChildTab sktab = new ChildTab((callbacks), tabs, title, requestResponse.getRequest(), requestResponse.getHttpService());
        tabs.setSelectedIndex(tabCount - removedTabCount);
        tabCount++;

        return sktab;
    }

    public String getTabCaption() {
        return "Java Serial Killer";
    }

    public Component getUiComponent() {
        return tabs;
    }
}