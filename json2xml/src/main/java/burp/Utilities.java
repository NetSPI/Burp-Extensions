package burp;


import org.json.JSONObject;
import org.json.XML;

import java.util.Iterator;
import java.util.List;

public class Utilities {

    public static byte[] convertToXML(IExtensionHelpers helpers, IHttpRequestResponse requestResponse){

        byte[] request = requestResponse.getRequest();

        int bodyOffset = helpers.analyzeRequest(request).getBodyOffset();

        String body = new String(request, bodyOffset, request.length - bodyOffset);
        Boolean success = true;
        String xml = "";
        try {
            JSONObject json = new JSONObject(body);
            xml = XML.toString(json);
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

            return helpers.buildHttpMessage(headers, xml.getBytes());

        }



    }

    public static byte[] convertToJSON(IExtensionHelpers helpers, IHttpRequestResponse requestResponse){

        byte[] request = requestResponse.getRequest();

        int bodyOffset = helpers.analyzeRequest(request).getBodyOffset();

        String body = new String(request, bodyOffset, request.length - bodyOffset);
        Boolean success = true;
        String json = "";
        try {
            JSONObject xmlJSONObject = XML.toJSONObject(body);
            json = xmlJSONObject.toString(2);
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

}
