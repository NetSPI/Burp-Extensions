package burp;



public class BurpExtender implements IBurpExtender, IMessageEditorTabFactory
{
    private IBurpExtenderCallbacks m_callbacks;
    private IExtensionHelpers m_helpers;
 
    //
    // implement IBurpExtender
    //
    
    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks)
    {
        // keep a reference to our callbacks object
        this.m_callbacks = callbacks;
        
        // obtain an extension helpers object
        m_helpers = callbacks.getHelpers();
        
        // set our extension name
        callbacks.setExtensionName("WCF Deserializer");
        
        // register ourselves as a message editor tab factory
        WCFTabFactory factory = new WCFTabFactory(m_callbacks, m_helpers);

        callbacks.registerMessageEditorTabFactory(factory);
        callbacks.registerContextMenuFactory(new WCFMenu(callbacks,m_helpers));
        callbacks.registerHttpListener(new WcfHttpListener());
    }

    //
    // implement IMessageEditorTabFactory
    //
    
    @Override
    public IMessageEditorTab createNewInstance(IMessageEditorController controller, boolean editable)
    {
        // create a new instance of our custom editor tab
        return new WCFDeserializerTab(controller, editable, m_callbacks, m_helpers);
    }


   
}
