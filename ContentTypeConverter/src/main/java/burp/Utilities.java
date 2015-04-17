package burp;

import com.google.gson.Gson;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.*;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.*;
import javax.xml.parsers.*;

public class Utilities {

    public static byte[] convertToXML(IExtensionHelpers helpers, IHttpRequestResponse requestResponse) throws Exception {

        byte[] request = requestResponse.getRequest();

        IRequestInfo requestInfo = helpers.analyzeRequest(request);

        int bodyOffset = requestInfo.getBodyOffset();

        byte content_type = requestInfo.getContentType();

        String body = new String(request, bodyOffset, request.length - bodyOffset);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = new Document() {
            @Override
            public DocumentType getDoctype() {
                return null;
            }

            @Override
            public DOMImplementation getImplementation() {
                return null;
            }

            @Override
            public Element getDocumentElement() {
                return null;
            }

            @Override
            public Element createElement(String tagName) throws DOMException {
                return null;
            }

            @Override
            public DocumentFragment createDocumentFragment() {
                return null;
            }

            @Override
            public Text createTextNode(String data) {
                return null;
            }

            @Override
            public Comment createComment(String data) {
                return null;
            }

            @Override
            public CDATASection createCDATASection(String data) throws DOMException {
                return null;
            }

            @Override
            public ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
                return null;
            }

            @Override
            public Attr createAttribute(String name) throws DOMException {
                return null;
            }

            @Override
            public EntityReference createEntityReference(String name) throws DOMException {
                return null;
            }

            @Override
            public NodeList getElementsByTagName(String tagname) {
                return null;
            }

            @Override
            public Node importNode(Node importedNode, boolean deep) throws DOMException {
                return null;
            }

            @Override
            public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
                return null;
            }

            @Override
            public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
                return null;
            }

            @Override
            public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
                return null;
            }

            @Override
            public Element getElementById(String elementId) {
                return null;
            }

            @Override
            public String getInputEncoding() {
                return null;
            }

            @Override
            public String getXmlEncoding() {
                return null;
            }

            @Override
            public boolean getXmlStandalone() {
                return false;
            }

            @Override
            public void setXmlStandalone(boolean xmlStandalone) throws DOMException {

            }

            @Override
            public String getXmlVersion() {
                return null;
            }

            @Override
            public void setXmlVersion(String xmlVersion) throws DOMException {

            }

            @Override
            public boolean getStrictErrorChecking() {
                return false;
            }

            @Override
            public void setStrictErrorChecking(boolean strictErrorChecking) {

            }

            @Override
            public String getDocumentURI() {
                return null;
            }

            @Override
            public void setDocumentURI(String documentURI) {

            }

            @Override
            public Node adoptNode(Node source) throws DOMException {
                return null;
            }

            @Override
            public DOMConfiguration getDomConfig() {
                return null;
            }

            @Override
            public void normalizeDocument() {

            }

            @Override
            public Node renameNode(Node n, String namespaceURI, String qualifiedName) throws DOMException {
                return null;
            }

            @Override
            public String getNodeName() {
                return null;
            }

            @Override
            public String getNodeValue() throws DOMException {
                return null;
            }

            @Override
            public void setNodeValue(String nodeValue) throws DOMException {

            }

            @Override
            public short getNodeType() {
                return 0;
            }

            @Override
            public Node getParentNode() {
                return null;
            }

            @Override
            public NodeList getChildNodes() {
                return null;
            }

            @Override
            public Node getFirstChild() {
                return null;
            }

            @Override
            public Node getLastChild() {
                return null;
            }

            @Override
            public Node getPreviousSibling() {
                return null;
            }

            @Override
            public Node getNextSibling() {
                return null;
            }

            @Override
            public NamedNodeMap getAttributes() {
                return null;
            }

            @Override
            public Document getOwnerDocument() {
                return null;
            }

            @Override
            public Node insertBefore(Node newChild, Node refChild) throws DOMException {
                return null;
            }

            @Override
            public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
                return null;
            }

            @Override
            public Node removeChild(Node oldChild) throws DOMException {
                return null;
            }

            @Override
            public Node appendChild(Node newChild) throws DOMException {
                return null;
            }

            @Override
            public boolean hasChildNodes() {
                return false;
            }

            @Override
            public Node cloneNode(boolean deep) {
                return null;
            }

            @Override
            public void normalize() {

            }

            @Override
            public boolean isSupported(String feature, String version) {
                return false;
            }

            @Override
            public String getNamespaceURI() {
                return null;
            }

            @Override
            public String getPrefix() {
                return null;
            }

            @Override
            public void setPrefix(String prefix) throws DOMException {

            }

            @Override
            public String getLocalName() {
                return null;
            }

            @Override
            public boolean hasAttributes() {
                return false;
            }

            @Override
            public String getBaseURI() {
                return null;
            }

            @Override
            public short compareDocumentPosition(Node other) throws DOMException {
                return 0;
            }

            @Override
            public String getTextContent() throws DOMException {
                return null;
            }

            @Override
            public void setTextContent(String textContent) throws DOMException {

            }

            @Override
            public boolean isSameNode(Node other) {
                return false;
            }

            @Override
            public String lookupPrefix(String namespaceURI) {
                return null;
            }

            @Override
            public boolean isDefaultNamespace(String namespaceURI) {
                return false;
            }

            @Override
            public String lookupNamespaceURI(String prefix) {
                return null;
            }

            @Override
            public boolean isEqualNode(Node arg) {
                return false;
            }

            @Override
            public Object getFeature(String feature, String version) {
                return null;
            }

            @Override
            public Object setUserData(String key, Object data, UserDataHandler handler) {
                return null;
            }

            @Override
            public Object getUserData(String key) {
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
            JSONObject json = new JSONObject(body);

            xml.append(XML.toString(json));
            xml.append("</root>");

            DocumentBuilder builder = factory.newDocumentBuilder();

            ByteArrayInputStream input =  new ByteArrayInputStream(
                    xml.toString().getBytes("UTF-8"));
            doc = builder.parse(input);

        }catch (Exception e){
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

            headers.add("Content-Type: application/xml;charset=UTF-8");

            return helpers.buildHttpMessage(headers, prettyPrint(doc).getBytes());

        }

    }

    public static byte[] convertToJSON(IExtensionHelpers helpers, IHttpRequestResponse requestResponse) {

        byte[] request = requestResponse.getRequest();

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
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        List<String> pairs = Arrays.asList(body.split("&"));

        for (String pair : pairs) {
            final int idx = pair.indexOf("=");
            final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
            if (!query_pairs.containsKey(key)) {
                query_pairs.put(key, "");
            }
            final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
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
