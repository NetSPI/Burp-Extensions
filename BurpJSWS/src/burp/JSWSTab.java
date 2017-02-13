package burp;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class JSWSTab extends AbstractTableModel implements IMessageEditorController {

    private final List<JSWSEntry> entries = new ArrayList<JSWSEntry>();
    public JSWSTable JSWSTable;
    public EachRowEditor rowEditor = new EachRowEditor(JSWSTable);
    private IMessageEditor requestViewer;
    private IHttpRequestResponse currentlyDisplayedItem;
    JSplitPane splitPane;
    JTabbedPane tabbedPane;

    public JSWSTab(final IBurpExtenderCallbacks callbacks, JTabbedPane tabbedPane, String request) {
        this.tabbedPane = tabbedPane;
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JSWSTable = new JSWSTable(JSWSTab.this);
        JSWSTable.setAutoCreateRowSorter(true);

                rowEditor = new EachRowEditor(JSWSTable);
        JScrollPane scrollPane = new JScrollPane(JSWSTable);

        splitPane.setLeftComponent(scrollPane);

        JTabbedPane tabs = new JTabbedPane();
        requestViewer = callbacks.createMessageEditor(JSWSTab.this, false);
        tabs.addTab("Request", requestViewer.getComponent());
        splitPane.setTopComponent(scrollPane);
        splitPane.setBottomComponent(tabs);
        tabbedPane.add(request, splitPane);
        tabbedPane.setTabComponentAt(JSWSParserTab.tabCount - JSWSParserTab.removedTabCount, new ButtonTabComponent(tabbedPane));

    }

    public void addEntry(JSWSEntry entry) {
        synchronized (entries) {
            int row = entries.size();
            entries.add(entry);
            fireTableRowsInserted(row, row);
            UIManager.put("tabbedPane.selected",
                    new javax.swing.plaf.ColorUIResource(Color.RED));
        }
    }

    public int getRowCount() {
        return entries.size();
    }

    public int getColumnCount() {
        return 3;
    }

    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Operation";
            case 1:
                return "Binding";
            case 2:
                return "Endpoint";
            default:
                return "";
        }
    }

    public Class getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {

        JSWSEntry JSWSEntry = entries.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return JSWSEntry.operationName;
            case 1:
                return JSWSEntry.bindingName;
            case 2:
                return JSWSEntry.endpoints.get(0);
            default:
                return "";
        }
    }

    public boolean isCellEditable(int row, int col) {
        return col >= 2;
    }

    public byte[] getRequest() {
        return currentlyDisplayedItem.getRequest();
    }

    public byte[] getResponse() {
        return currentlyDisplayedItem.getResponse();
    }

    public IHttpService getHttpService() {
        return currentlyDisplayedItem.getHttpService();
    }

    private class JSWSTable extends JTable {

        public JSWSTable(TableModel tableModel) {
            super(tableModel);
        }

        public void changeSelection(int row, int col, boolean toggle, boolean extend) {

            JSWSEntry JSWSEntry = entries.get(super.convertRowIndexToModel(row));
            requestViewer.setMessage(JSWSEntry.request, true);
            currentlyDisplayedItem = JSWSEntry.requestResponse;
            super.changeSelection(row, col, toggle, extend);
        }

        private boolean painted;

        public void paint(Graphics g) {
            super.paint(g);

            if (!painted) {
                painted = true;
                splitPane.setResizeWeight(.30);
                splitPane.setDividerLocation(0.30);
            }
        }
    }

 }
