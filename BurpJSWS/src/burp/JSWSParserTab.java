package burp;

import java.awt.*;

import javax.swing.*;

public class JSWSParserTab implements ITab {

    JTabbedPane tabs;
    private IBurpExtenderCallbacks callbacks;
    static int tabCount = 0;
    static int removedTabCount = 0;

    public JSWSParserTab(final IBurpExtenderCallbacks callbacks) {
        this.callbacks = callbacks;

        tabs = new JTabbedPane();

        callbacks.customizeUiComponent(tabs);

        callbacks.addSuiteTab(JSWSParserTab.this);

    }

    public JSWSTab createTab(String request) {

        JSWSTab wsdltab = new JSWSTab((callbacks), tabs, request);
        tabs.setSelectedIndex(tabCount - removedTabCount);
        tabCount++;

        return wsdltab;
    }

    public String getTabCaption() {
        return "JSWS";
    }

    public Component getUiComponent() {
        return tabs;
    }


}
