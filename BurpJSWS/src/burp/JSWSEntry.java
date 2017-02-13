package burp;

import java.util.List;

public class JSWSEntry {

    final String bindingName;
    final byte[] request;
    final String operationName;
    final IHttpRequestResponse requestResponse;
    final List<String> endpoints;

    JSWSEntry(String bindingName, byte[] request, String operationName, List<String> endpoints, IHttpRequestResponse requestResponse) {
        this.bindingName = bindingName;
        this.request = request;
        this.operationName = operationName;
        this.endpoints = endpoints;
        this.requestResponse = requestResponse;
    }

}
