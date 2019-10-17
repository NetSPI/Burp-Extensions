package burp;

import java.io.PrintWriter;

public class BurpExtender implements IBurpExtender {

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
        callbacks.setExtensionName("CopyPaths");
        Logger.init(new PrintWriter(callbacks.getStdout(), true));
        callbacks.registerContextMenuFactory(new Menu(callbacks));
    }
}
