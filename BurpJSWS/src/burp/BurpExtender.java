package burp;

import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BurpExtender implements IBurpExtender, IScannerCheck
{
    private IBurpExtenderCallbacks callbacks;
    private IExtensionHelpers helpers;

    private OutputStream out;
    private static final byte[] GREP_STRING = "Type.registerNamespace".getBytes();



    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks)
    {

        this.callbacks = callbacks;
        helpers = callbacks.getHelpers();

        callbacks.setExtensionName("JSWebServices");

        out = callbacks.getStdout();
        callbacks.registerContextMenuFactory(new Menu(callbacks));
        callbacks.registerScannerCheck(this);

    }




    @Override
    public List<IScanIssue> doPassiveScan(IHttpRequestResponse baseRequestResponse) {
        List<int[]> matches = getMatches(baseRequestResponse.getResponse(), GREP_STRING);
        try {
            out.write(baseRequestResponse.getResponse());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String p = ".*(Type\\.registerNamespace|asmx/js|PageMethods).*";
        Pattern r = Pattern.compile(p);
        Matcher m = r.matcher(baseRequestResponse.getResponse().toString());
        if(m.find())
        {
            try {
                out.write("found keyword\n".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<IScanIssue> issues = new ArrayList<>(1);
            issues.add(new JSWSIssue(baseRequestResponse.getHttpService(),
                        helpers.analyzeRequest(baseRequestResponse).getUrl(),
                        new IHttpRequestResponse[] { baseRequestResponse },
                        "JavaScript Service Proxy Available",
                        "The page may utilize a JavaScript Service Proxy, which can be parsed to create all possible sample requests",
                        "Information"));
            return issues;
        }
        return null;
    }

   private List<int[]> getMatches(byte[] response, byte[] match)
   {
       List<int[]> matches = new ArrayList<int[]>();

       int start = 0;
       while(start < response.length)
       {
           start = helpers.indexOf(response, match, true, start, response.length);
           if(start == -1)
           {
               break;
           }
           matches.add(new int[] {start, start + match.length});
           start += match.length;
       }
       return matches;
   }

    @Override
    public List<IScanIssue> doActiveScan(IHttpRequestResponse baseRequestResponse, IScannerInsertionPoint insertionPoint) {
        return null;
    }

    @Override
    public int consolidateDuplicateIssues(IScanIssue existingIssue, IScanIssue newIssue) {
        return 0;
    }
}