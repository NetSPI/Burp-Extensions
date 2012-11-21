"Deflate" Burp Plugin (DeflateBurpPlugin) v0.1

Joe Hemler (Gotham Digital Science)

2/19/2008

This is a plug-in for Burp Proxy (it implements the IBurpExtender interface) that decompresses HTTP response content in the ZLIB (RFC1950) and DEFLATE (RFC1951) compression formats.  This arose out of an immediate need on a web application security assessment. At present, Burp Proxy only unpacks gzip compressed data.

The plug-in will attempt to decompress every HTTP response body it handles, irrespective of whether the "Content-Encoding: deflate" HTTP response header is present. If decompression fails, the original response message will be passed on by the plug-in unchanged.

To load the plug-in, place DeflateBurpPlugin.jar and suite_deflate_plugin.bat in the same directory as burpsuite_v1.1.jar (or whatever your Burp Suite jar is called) and launch the provided batch file.

By default, "debug" information is written to standard out. To disable this feature, edit the provided batch file by removing the "debug" command-line option and re-launch.

To test the plug-in, DeflateTestServlet servlet is provided. Additional information about this test harness can be found in its own README file.

A command-line utility for compressing and decompressing files is also exposed by DeflateBurpPlugin.jar. To see the available options, run the following from the command-line:

java -classpath DeflateBurpPlugin.jar com.gdssecurity.utils.Compression

Additional information about the plug-in can be found at http://www.gdssecurity.com/l/b

All questions regarding the plug-in can be sent to tools@gdssecurity.com

DeflateBurpPlugin is copyright (c) Gotham Digital Science 2008. All rights reserved.