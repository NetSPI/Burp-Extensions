package burp;

public class BurpExtender implements IBurpExtender
{

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks)
    {

        callbacks.setExtensionName("Java Serial Killer");

        callbacks.registerContextMenuFactory(new Menu(callbacks));

    }
}