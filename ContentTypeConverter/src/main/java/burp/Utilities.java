package burp;

import com.google.gson.Gson;
import org.json.JSONObject;
import org.json.XML;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

public class Utilities {

    public static byte[] convertToXML(IExtensionHelpers helpers, IHttpRequestResponse requestResponse) throws UnsupportedEncodingException {

        byte[] request = requestResponse.getRequest();

        IRequestInfo requestInfo = helpers.analyzeRequest(request);

        int bodyOffset = requestInfo.getBodyOffset();

        byte content_type = requestInfo.getContentType();

        String body = new String(request, bodyOffset, request.length - bodyOffset);

        StringBuilder xml = new StringBuilder();

        if (content_type == 0 || content_type == 1) {

            Map<String,String> params = splitQuery(body);
            Gson gson = new Gson();
            body = gson.toJson(params);
        }

        Boolean success = true;

        try {
            JSONObject json = new JSONObject(body);

            xml = new StringBuilder(XML.toString(json));
            xml.insert(0,"<root>");
            xml.append("</root>");

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

            return helpers.buildHttpMessage(headers, xml.toString().getBytes());

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
            query_pairs.put(key,value);
        }
        return query_pairs;
    }

}
