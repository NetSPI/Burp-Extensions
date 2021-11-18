package burp;

import com.google.gson.Gson;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.XML;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URLDecoder;
import java.util.*;

public class Utilities {
    public static byte[] convertToXML(IExtensionHelpers helpers,IBurpExtenderCallbacks callbacks, IHttpRequestResponse requestResponse) throws Exception {

        PrintWriter stdout = new PrintWriter(callbacks.getStdout(),true);
        PrintWriter stderr = new PrintWriter(callbacks.getStderr(),true);

        byte[] request = requestResponse.getRequest();

        if (Objects.equals(helpers.analyzeRequest(request).getMethod(), "GET")){
            request = helpers.toggleRequestMethod(request);
        }

        IRequestInfo requestInfo = helpers.analyzeRequest(request);

        int bodyOffset = requestInfo.getBodyOffset();

        byte content_type = requestInfo.getContentType();

        String body = new String(request, bodyOffset, request.length - bodyOffset, "UTF-8");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = new Document() {
            public String getNodeName() {
                return null;
            }

            public String getNodeValue() throws DOMException {
                return null;
            }

            public void setNodeValue(String nodeValue) throws DOMException {

            }

            public short getNodeType() {
                return 0;
            }

            public Node getParentNode() {
                return null;
            }

            public NodeList getChildNodes() {
                return null;
            }

            public Node getFirstChild() {
                return null;
            }

            public Node getLastChild() {
                return null;
            }

            public Node getPreviousSibling() {
                return null;
            }

            public Node getNextSibling() {
                return null;
            }

            public NamedNodeMap getAttributes() {
                return null;
            }

            public Document getOwnerDocument() {
                return null;
            }

            public Node insertBefore(Node newChild, Node refChild) throws DOMException {
                return null;
            }

            public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
                return null;
            }

            public Node removeChild(Node oldChild) throws DOMException {
                return null;
            }

            public Node appendChild(Node newChild) throws DOMException {
                return null;
            }

            public boolean hasChildNodes() {
                return false;
            }

            public Node cloneNode(boolean deep) {
                return null;
            }

            public void normalize() {

            }

            public boolean isSupported(String feature, String version) {
                return false;
            }

            public String getNamespaceURI() {
                return null;
            }

            public String getPrefix() {
                return null;
            }

            public void setPrefix(String prefix) throws DOMException {

            }

            public String getLocalName() {
                return null;
            }

            public boolean hasAttributes() {
                return false;
            }

            public String getBaseURI() {
                return null;
            }

            public short compareDocumentPosition(Node other) throws DOMException {
                return 0;
            }

            public String getTextContent() throws DOMException {
                return null;
            }

            public void setTextContent(String textContent) throws DOMException {

            }

            public boolean isSameNode(Node other) {
                return false;
            }

            public String lookupPrefix(String namespaceURI) {
                return null;
            }

            public boolean isDefaultNamespace(String namespaceURI) {
                return false;
            }

            public String lookupNamespaceURI(String prefix) {
                return null;
            }

            public boolean isEqualNode(Node arg) {
                return false;
            }

            public Object getFeature(String feature, String version) {
                return null;
            }

            public Object setUserData(String key, Object data, UserDataHandler handler) {
                return null;
            }

            public Object getUserData(String key) {
                return null;
            }

            public DocumentType getDoctype() {
                return null;
            }

            public DOMImplementation getImplementation() {
                return null;
            }

            public Element getDocumentElement() {
                return null;
            }

            public Element createElement(String tagName) throws DOMException {
                return null;
            }

            public DocumentFragment createDocumentFragment() {
                return null;
            }

            public Text createTextNode(String data) {
                return null;
            }

            public Comment createComment(String data) {
                return null;
            }

            public CDATASection createCDATASection(String data) throws DOMException {
                return null;
            }

            public ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
                return null;
            }

            public Attr createAttribute(String name) throws DOMException {
                return null;
            }

            public EntityReference createEntityReference(String name) throws DOMException {
                return null;
            }

            public NodeList getElementsByTagName(String tagname) {
                return null;
            }

            public Node importNode(Node importedNode, boolean deep) throws DOMException {
                return null;
            }

            public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
                return null;
            }

            public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
                return null;
            }

            public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
                return null;
            }

            public Element getElementById(String elementId) {
                return null;
            }

            public String getInputEncoding() {
                return null;
            }

            public String getXmlEncoding() {
                return null;
            }

            public boolean getXmlStandalone() {
                return false;
            }

            public void setXmlStandalone(boolean xmlStandalone) throws DOMException {

            }

            public String getXmlVersion() {
                return null;
            }

            public void setXmlVersion(String xmlVersion) throws DOMException {

            }

            public boolean getStrictErrorChecking() {
                return false;
            }

            public void setStrictErrorChecking(boolean strictErrorChecking) {

            }

            public String getDocumentURI() {
                return null;
            }

            public void setDocumentURI(String documentURI) {

            }

            public Node adoptNode(Node source) throws DOMException {
                return null;
            }

            public DOMConfiguration getDomConfig() {
                return null;
            }

            public void normalizeDocument() {

            }

            public Node renameNode(Node n, String namespaceURI, String qualifiedName) throws DOMException {
                return null;
            }
        };

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        xml.append("<root>");

        if (content_type == 0 || content_type == 1) {

            Map<String,String> params = splitQuery(body);
            Gson gson = new Gson();
            body = gson.toJson(params);
        }

        Boolean success = true;

        try {
            Object item = new JSONTokener(body).nextValue();
            Object json = item;

            xml.append(XML.toString(json));
            xml.append("</root>");

            DocumentBuilder builder = factory.newDocumentBuilder();

            ByteArrayInputStream input =  new ByteArrayInputStream(
                    xml.toString().getBytes("UTF-8"));
            doc = builder.parse(input);

        }catch (Exception e){
            stderr.println(e);
            success = false;

        }

        if (!success) {
            return null;
        } else {

            List<String> headers;

            headers = helpers.analyzeRequest(request).getHeaders();

            Iterator<String> iter = headers.iterator();
            while(iter.hasNext()){
                if(iter.next().contains("Content-Type"))
                    iter.remove();
            }

            headers.add("Content-Type: application/xml;charset=UTF-8");

            return helpers.buildHttpMessage(headers,prettyPrint(doc).getBytes());

        }

    }

    public static byte[] convertToJSON(IExtensionHelpers helpers, IBurpExtenderCallbacks callbacks, IHttpRequestResponse requestResponse) {

        byte[] request = requestResponse.getRequest();
        PrintWriter stdout = new PrintWriter(callbacks.getStdout(),true);
        PrintWriter stderr = new PrintWriter(callbacks.getStderr(),true);
        if (Objects.equals(helpers.analyzeRequest(request).getMethod(), "GET")){
            request = helpers.toggleRequestMethod(request);
        }

        IRequestInfo requestInfo = helpers.analyzeRequest(request);

        int bodyOffset = requestInfo.getBodyOffset();

        byte content_type = requestInfo.getContentType();

        String body = new String(request, bodyOffset, request.length - bodyOffset);

        String json = "";

        Boolean success = true;

        try {
            if (content_type == 3) {
                JSONObject xmlJSONObject = XML.toJSONObject(body);
                json = xmlJSONObject.toString(2);
            } else if (content_type == 0 || content_type == 1) {
                Map<String,String> params = splitQuery(body);
                Gson gson = new Gson();
                json = gson.toJson(params);
            } else {
                json = body;
            }
        }catch (Exception e){
            stderr.println(e);
            success = false;

        }

        if (!success) {
            return request;
        } else {

            List<String> headers;

            headers = helpers.analyzeRequest(request).getHeaders();

            Iterator<String> iter = headers.iterator();
            while(iter.hasNext()){
                if(iter.next().contains("Content-Type"))
                    iter.remove();
            }

            headers.add("Content-Type: application/json;charset=UTF-8");

            return helpers.buildHttpMessage(headers, json.getBytes());

        }


    }

    private static Map<String,String> splitQuery(String body) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<>();
        List<String> pairs = Arrays.asList(body.split("&"));

        for (String pair : pairs) {
            final int idx = pair.indexOf("=");
            final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
            if (!query_pairs.containsKey(key)) {
                query_pairs.put(key, "");
            }
            final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : "";
            query_pairs.put(key,value.trim());
        }
        return query_pairs;
    }

    public static String prettyPrint(Document xml) throws Exception {
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        Writer out = new StringWriter();
        tf.transform(new DOMSource(xml), new StreamResult(out));
       return(out.toString());
    }
}
