package burp;

public class BurpExtender implements IBurpExtender
{

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks)
    {
        IExtensionHelpers helpers = callbacks.getHelpers();

        callbacks.setExtensionName("Content-Type Converter");

        callbacks.registerContextMenuFactory(new Menu(helpers));

    }
}
