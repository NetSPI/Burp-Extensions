package burp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.UUID;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class WCFConverterUtils {
	private static final String DOUBLELINEBREAK = "\r\n\r\n";
	static String LINESEPARATOR = System.getProperty("line.separator");
	public static byte[] getBody(byte[] message) {

		String testStr = new String(message);
		String[] strHeadersAndContent = testStr.split(DOUBLELINEBREAK);
		byte[] reqBody = Arrays.copyOfRange(message, strHeadersAndContent[0].getBytes().length + 4, message.length);
		return reqBody;
	}

	public static byte[] processRequest(byte[] message, byte[] body) {

		try {
			String strMessage = new String(message);
			String[] strHeadersAndContent = strMessage.split(DOUBLELINEBREAK);
			String headerWithUpdatedLength = strHeadersAndContent[0].replaceAll("Content-Length: .*", "Content-Length: " + String.valueOf(body.length));
			byte[] header = (headerWithUpdatedLength + LINESEPARATOR + LINESEPARATOR).getBytes();
			byte[] retArray = new byte[header.length + body.length];
			System.arraycopy(header, 0, retArray, 0, header.length);
			System.arraycopy(body, 0, retArray, header.length, body.length);
			return retArray;
		} catch (Exception e) {
			e.printStackTrace();
			return message;
		}

	}

	public static byte[] base64decode(String msg) throws IOException {
		BASE64Decoder decoder = new BASE64Decoder();
		return decoder.decodeBuffer(msg);

	}

	public static String base64encode(byte[] msg) throws IOException {
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(msg);

	}

	static String writeToTmpFile(byte[] bytes) {
		String uuid = UUID.randomUUID().toString().substring(0, 5);
		File file = new File(System.getProperty("java.io.tmpdir") + uuid + ".txt");
		try {
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
			fos.write(bytes);
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}


	static String encodeDecodeWcf(String strEncodeDecode, String filePath) {
		try {
			String line;
			String out;
			strEncodeDecode = strEncodeDecode == "decode" ? "-d" : "-e";
			String[] commandWithArgs = { "wcfxml", strEncodeDecode, "-f", filePath, "-b64" };
			Process p = Runtime.getRuntime().exec(commandWithArgs);
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			if ((line = input.readLine()) != null) {
				out = line;
			} else {
				out = "An Error Has Occurred";
			}
			input.close();
			File file = new File(filePath);
			file.delete();
			return out;
		} catch (Exception err) {
			System.out.println(err.getMessage());
			File file = new File(filePath);
			file.delete();
			return err.getMessage();
		}
	}

	static byte[] encodeDecodeWcfRequest(String strEncodeDecode, String strMessage) {
		String[] strHeadersAndContent = strMessage.split(DOUBLELINEBREAK);
		String tmpFilePath = writeToTmpFile(strHeadersAndContent[1].getBytes());
		try {

			byte[] content = base64decode(encodeDecodeWcf(strEncodeDecode, tmpFilePath));
			if (content == null)
				return null;
			String headerWithUpdatedLength = strHeadersAndContent[0].replaceAll("Content-Length: .*", "Content-Length: " + String.valueOf(content.length));
			byte[] header = (headerWithUpdatedLength + LINESEPARATOR + LINESEPARATOR).getBytes();
			byte[] retArray = new byte[header.length + content.length];
			System.arraycopy(header, 0, retArray, 0, header.length);
			System.arraycopy(content, 0, retArray, header.length, content.length);
			File file = new File(tmpFilePath);
			file.delete();
			return retArray;
		} catch (Exception e) {
			e.printStackTrace();
			File file = new File(tmpFilePath);
			file.delete();
			return strMessage.getBytes();
		}
	}

	public static String removeHttpHeader(String strHttpHeaders, String strPattern) {
		strHttpHeaders = strHttpHeaders.replaceAll(strPattern, "");
		return strHttpHeaders;
	}

}
