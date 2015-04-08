package burp;

public class BurpExtender implements IBurpExtender
{

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks)
    {

        IExtensionHelpers helpers = callbacks.getHelpers();

        callbacks.setExtensionName("json2xml converter");

        callbacks.registerContextMenuFactory(new Menu(callbacks, helpers));

    }
}
