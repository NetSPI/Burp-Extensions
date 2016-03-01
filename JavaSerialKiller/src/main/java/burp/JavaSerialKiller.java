package burp;

public class JavaSerialKiller {

    private final JavaSerialKillerTab tab;

    public JavaSerialKiller(JavaSerialKillerTab tab){
        this.tab = tab;
    }

    public void sendToTab(IHttpRequestResponse requestResponse) {

        tab.createTab(String.valueOf(JavaSerialKillerTab.tabCount),requestResponse);

    }

}
