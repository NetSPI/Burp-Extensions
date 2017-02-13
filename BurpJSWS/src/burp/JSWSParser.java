package burp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JSWSParser {

    private IExtensionHelpers helpers;
    private JSWSParserTab tab;
    private OutputStream out;
    private IBurpExtenderCallbacks callbacks;
    private boolean passiveScan;
    private List<IScanIssue> instances;

    public JSWSParser(IExtensionHelpers helpers, JSWSParserTab tab, IBurpExtenderCallbacks callbacks) {
        this.helpers = helpers;
        this.tab = tab;
        this.callbacks = callbacks;
        this.passiveScan = false;
        this.instances = new ArrayList<IScanIssue>();
        out = callbacks.getStdout();
    }

    public boolean isPassiveScan() {
        return passiveScan;
    }

    public void setPassiveScan(boolean passiveScan) {
        this.passiveScan = passiveScan;
    }

    private boolean parse(IHttpRequestResponse requestResponse, JSWSTab jswsTab, IBurpExtenderCallbacks callbacks, String url, boolean debug, boolean embedded) throws IOException {
        byte[] response = requestResponse.getResponse();
        String sResp = new String(response);
        Scanner inS = new Scanner(sResp);
        String tmp = "";

        while(inS.hasNextLine() && !embedded)
        {
            tmp = inS.nextLine();
            if(tmp.length()==0)
            {
                break;
            }
        }
        if(inS.hasNextLine() && !embedded)
        {
            String line = inS.nextLine();
            String p = "^(Type\\.registerNamespace)";
            Pattern r = Pattern.compile(p);
            Matcher m = r.matcher(line);
            if(!m.find())
            {
                return false;
            }
        }
        while(inS.hasNextLine()) {
            tmp = inS.nextLine();
            if (tmp.contains(":function(")) {
                String[] pts = tmp.split(":function");
                if (pts.length < 2) continue;

                String name = pts[0];
                String type = "";
                List<Parameter> alParams = new ArrayList<Parameter>();
                if (!debug) {
                    String[] params = pts[1].replace("(", "").replace(")", "").replace("{", "").trim().split(",");
                    for (String p : params) {
                        alParams.add(new Parameter(p.trim()));
                    }
                } else {
                    Pattern namePattern = Pattern.compile("(param name=\"\\S+\" )");
                    Pattern typeName = Pattern.compile("type=\"(\\w*)\"");
                    Pattern typePattern = Pattern.compile("(>.*</param>)");
                    Matcher m;
                    String paramName = "";
                    while (inS.hasNextLine()) {
                        tmp = inS.nextLine();
                        if (!tmp.substring(0, 3).equals("///")) break;

                        m = namePattern.matcher(tmp);
                        if (m.find()) {
                            paramName = m.group(0).replace("param name=\"", "").replaceAll("\" ", "");
                        }
                        m = typeName.matcher(tmp);
                        if(m.find())
                        {
                            type = m.group(0).replace("type=\"","").replaceAll("\"$","")+": ";
                        }
                        m = typePattern.matcher(tmp);
                        if (m.find()) {
                            type += m.group(0).replace(">", "").replace("</param","");
                        }
                        alParams.add(new Parameter(paramName, type));

                    }
                }
                if (alParams.isEmpty()) alParams.add(new Parameter(""));
                List<String> endpoints = new ArrayList<String>();
                endpoints.add(getEndPoint(url, name));
                jswsTab.addEntry(new JSWSEntry(name, createRequest(requestResponse, getEndPoint(url, name), alParams), name, endpoints, requestResponse));



            }
        }
        return true;
    }

    public int parseJSWS(IHttpRequestResponse requestResponse, IBurpExtenderCallbacks callbacks) throws IOException {

        byte[] response = requestResponse.getResponse();
        if (response == null){

            IHttpRequestResponse request = callbacks.makeHttpRequest(requestResponse.getHttpService(), requestResponse.getRequest());
            response = request.getResponse();
        }
        if (response == null){
            return -1;
        }

        IRequestInfo requestInfo = helpers.analyzeRequest(requestResponse.getRequest());

        List<String> reqHeaders = requestInfo.getHeaders();
        String path = reqHeaders.get(0).replace("GET ", "").replace(" HTTP/1.1", "");
        String url = requestResponse.getHttpService().getProtocol() + "://" + requestResponse.getHttpService().getHost() + path;

        JSWSTab jswsTab = tab.createTab(url);

        String pattern = "(.*)(/js)(debug)?$";
        Pattern regex = Pattern.compile(pattern);
        Matcher m = regex.matcher(url);
        boolean debug = false;
        boolean embedded = false;
        if(!m.find())
        {
            String sResponse = helpers.bytesToString(response);
            Document respDoc = Jsoup.parse(sResponse);
            Elements scriptElements = respDoc.getElementsByTag("script");
            for(Element element : scriptElements)
            {
                String body = element.toString();
                body = body.replaceAll("(<)(/)?(script)( type=\"text/javascript\")?(\\s)*(>)","");
                body = body.trim();
                if(body.contains("PageMethods"))
                {
                    body = body.replace("//<![CDATA[\n","").replace("//]]>","");
                    embedded = true;
                    final byte[] req = requestResponse.getRequest();
                    final byte[] resp = body.getBytes();
                    final IHttpService serv = requestResponse.getHttpService();
                    requestResponse = new IHttpRequestResponse() {
                        @Override
                        public byte[] getRequest() {
                            return req;
                        }

                        @Override
                        public void setRequest(byte[] message) {

                        }

                        @Override
                        public byte[] getResponse() {
                            return  resp;
                        }

                        @Override
                        public void setResponse(byte[] message) {

                        }

                        @Override
                        public String getComment() {
                            return null;
                        }

                        @Override
                        public void setComment(String comment) {

                        }

                        @Override
                        public String getHighlight() {
                            return null;
                        }

                        @Override
                        public void setHighlight(String color) {

                        }

                        @Override
                        public IHttpService getHttpService() {
                            return serv;
                        }

                        @Override
                        public void setHttpService(IHttpService httpService) {

                        }
                    };
                    debug = body.contains("/// <param name");
                    break;
                }
            }

        }
        else
        {
            pattern = "(.*)(/jsdebug)$";
            regex = Pattern.compile(pattern);
            m = regex.matcher(url);
            debug = m.find();
        }



        if(!parse(requestResponse, jswsTab, callbacks, url, debug, embedded)) return -2;

        return 0;
    }

    private byte[] createRequest(IHttpRequestResponse requestResponse, String url, List<Parameter> parameters) throws IOException {
        String endpointURL = url.replaceAll("(http)(s)?(://[^/]*)", "");
        List<String> headers;
        String host = requestResponse.getHttpService().getHost();
        StringBuilder message = new StringBuilder("{");


        headers = helpers.analyzeRequest(requestResponse).getHeaders();

        for(Parameter p : parameters)
        {
            message.append("\"");
            message.append(p.getpName());
            message.append("\": ");
            if(p.knowType())
            {
                message.append("\"");
                message.append(p.getpType());
                message.append("\"");
            }
            else
            {
                message.append("\"\"");
            }
            if(!parameters.get(parameters.size()-1).equals(p))
            {
                message.append(", ");
            }
        }
        message.append("}");


        headers.remove(0);
        headers.add(0, "POST " + endpointURL + " HTTP/1.1");
        Iterator<String> iter = headers.iterator();
        String i;
        while (iter.hasNext()) {
            i = iter.next();
            if (i.contains("Host:")) {
                iter.remove();
            }
            if (i.contains("Content-Type:")) {
                iter.remove();
            }
        }
        headers.add("Content-Type: application/json;charset=UTF-8");
        headers.add("Host: " + host);

        return helpers.buildHttpMessage(headers, message.toString().getBytes());
    }

    private String getEndPoint(String url, String name) {
        if(url.contains("/js"))
        {
            return url.replaceAll("(/js)(debug)?$", "/"+name);
        }
        else
        {
            return url.substring(0, url.lastIndexOf("?")) + "/"+name;
        }
    }

}



