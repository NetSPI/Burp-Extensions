package burp;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;

public class WCFMenu implements IContextMenuFactory {
	private IBurpExtenderCallbacks m_callbacks;
	public WCFMenu(IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers) {
		m_callbacks = callbacks;

	}

	@Override
	public List<JMenuItem> createMenuItems(final IContextMenuInvocation invocation) {
		JMenuItem sendAMFToIntruderMenu = new JMenuItem("Send Deserialized WCF to Intruder");
		JMenuItem scanAMFMenu = new JMenuItem("Scan WCF with predefined insertion points");
		sendAMFToIntruderMenu.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				IHttpRequestResponse[] selectedMessages = invocation.getSelectedMessages();
				for (IHttpRequestResponse iReqResp : selectedMessages) {
					IHttpService httpService = iReqResp.getHttpService();
					m_callbacks.sendToIntruder(httpService.getHost(), httpService.getPort(),
							(httpService.getProtocol().equals("https") ? true : false),
							WCFConverterUtils.encodeDecodeWcfRequest("decode", new String(iReqResp.getRequest())));
				}
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		});

		scanAMFMenu.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				IHttpRequestResponse[] selectedMessages = invocation.getSelectedMessages();
				for (IHttpRequestResponse iReqResp : selectedMessages) {
					IHttpService httpService = iReqResp.getHttpService();
					m_callbacks.doActiveScan(httpService.getHost(), httpService.getPort(),
							(httpService.getProtocol().equals("https") ? true : false),
							WCFConverterUtils.encodeDecodeWcfRequest("encode", new String(iReqResp.getRequest())));

				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}

		});
		List<JMenuItem> menus = new ArrayList();
		menus.add(sendAMFToIntruderMenu);
		menus.add(scanAMFMenu);
		return menus;
	}
}
