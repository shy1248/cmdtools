package me.shy.cmdtool.utils.file.excel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @fileName: com.ysb.common.utils.file.excel.Excel2K7Reader.java
 * @version: 1.0
 * @since: 2015-3-29 上午9:29:27
 * @describe : 抽象Excel2007读取器，excel2007的底层数据结构是xml文件，采用SAX的事件驱动的方法解析
 *           xml，需要继承DefaultHandler，在遇到文件内容时，事件会触发，这种做法可以大大降低
 *           内存的耗费，特别使用于大数据量的文件。
 * 
 *           ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2015.
 */
public class Excel2K7Reader extends DefaultHandler {
    // 共享字符串表
    private SharedStringsTable sst;
    // 上一次的内容
    private String lastContents;
    private int sheetIndex = -1;
    // 存放每一行的cell集合
    private List<String> cells = new ArrayList<String>();
    private int currentRow = 0;
    private int currentColumn = 0;
    private int predceColumn = 0;
    private int titleRow = 0;
    private int columnSize = 0;
    private RowHandable rowHandler;
    // 单元格数据类型，默认为字符串类型
    private CellDataType nextDataType = CellDataType.SSTINDEX;
    private final DataFormatter formatter = new DataFormatter();
    private short formatIndex;
    private String formatString;
    private StylesTable stylesTable;
    
    /**
     * 描述: 设置业务逻辑处理模块
     * 
     * @param rowHandler
     *            业务逻辑处理模块
     * @author Yu Shuibo 2015-3-29 上午9:37:04
     */
    public void setRowHandler(RowHandable rowHandler) {
        this.rowHandler = rowHandler;
    }
    
    /**
     * 只遍历一个电子表格，其中sheetId为要遍历的sheet索引，从1开始，1-3
     * 
     * @param filename
     * @param sheetId
     * @throws Exception
     */
    public void processOneSheet(String filename, int sheetId) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader(pkg);
        stylesTable = r.getStylesTable();
        SharedStringsTable sst = r.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst);
        
        // 根据 rId# 或 rSheet# 查找sheet
        InputStream sheet2 = r.getSheet("rId" + sheetId);
        if (sheet2 == null) {
            sheet2 = r.getSheet("rSheet#" + sheetId);
        }
        sheetIndex++;
        InputSource sheetSource = new InputSource(sheet2);
        parser.parse(sheetSource);
        sheet2.close();
    }
    
    /**
     * 描述：遍历指定的一组电子表格，其中sheetIds为要遍历的sheet索引的数组
     * 
     * @param filename
     *            要处理的Excel文件名
     * @param sheetIds
     *            要处理的sheets
     * @throws Exception
     *             发生错误时抛此异常
     */
    public void process(String filename, int[] sheetIds) throws Exception {
        for (int sheetId : sheetIds) {
            processOneSheet(filename, sheetId);
        }
    }
    
    /**
     * 描述：遍历工作簿中所有的电子表格
     * 
     * @param filename
     *            要处理的Excel文件名
     * @throws Exception
     *             发生错误时抛此异常
     */
    public void process(String filename) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader(pkg);
        stylesTable = r.getStylesTable();
        SharedStringsTable sst = r.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst);
        Iterator<InputStream> sheets = r.getSheetsData();
        while (sheets.hasNext()) {
            currentRow = 0;
            sheetIndex++;
            InputStream sheet = sheets.next();
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
            sheet.close();
        }
    }
    
    /**
     * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
     *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        
        // c => 单元格
        if ("c".equals(qName)) {
            currentColumn = this.getRowIndex(attributes.getValue("r"));
            this.setNextDataType(attributes);
        }
        // 置空
        lastContents = "";
    }
    
    /**
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        // v => 单元格的值，如果单元格是字符串则v标签的值为该字符串在SST中的索引
        // 将单元格内容加入rowlist中，在这之前先去掉字符串前后的空白符
        if ("v".equals(name) || "t".equals(name)) {
            String value = this.getDataValue(lastContents.trim(), "");
            int blank = currentColumn - predceColumn;
            if (blank > 1) {
                for (int i = 0; i < blank - 1; i++) {
                    cells.add(predceColumn, " ");
                }
            }
            predceColumn = currentColumn;
            cells.add(currentColumn - 1, value);
        } else {
            // 如果标签名称为 row ，这说明已到行尾，调用 optRows() 方法
            if (name.equals("row")) {
                int tmpCols = cells.size();
                if (currentRow > this.titleRow && tmpCols < this.columnSize) {
                    for (int i = 0; i < this.columnSize - tmpCols; i++) {
                        cells.add(cells.size(), " ");
                    }
                }
                rowHandler.getRow(sheetIndex, currentRow, cells);
                if (currentRow == this.titleRow) {
                    this.columnSize = cells.size();
                }
                cells.clear();
                currentRow++;
                currentColumn = 0;
                predceColumn = 0;
            }
        }
        
    }
    
    /**
     * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // 得到单元格内容的值
        lastContents += new String(ch, start, length);
    }
    
    // 得到列索引，每一列c元素的r属性构成为字母加数字的形式，字母组合为列索引，数字组合为行索引，
    // 如AB45,表示为第（A-A+1）*26+（B-A+1）*26列，45行
    private int getRowIndex(String rowStr) {
        rowStr = rowStr.replaceAll("[^A-Z]", "");
        byte[] rowAbc = rowStr.getBytes();
        int len = rowAbc.length;
        float num = 0;
        for (int i = 0; i < len; i++) {
            num += (rowAbc[i] - 'A' + 1) * Math.pow(26, len - i - 1);
        }
        return (int) num;
    }
    
    private XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
        XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        this.sst = sst;
        parser.setContentHandler(this);
        return parser;
    }
    
    private void setNextDataType(Attributes attributes) {
        nextDataType = CellDataType.NUMBER;
        formatIndex = -1;
        formatString = null;
        String cellType = attributes.getValue("t");
        String cellStyleStr = attributes.getValue("s");
        
        if ("b".equals(cellType)) {
            nextDataType = CellDataType.BOOL;
        } else if ("e".equals(cellType)) {
            nextDataType = CellDataType.ERROR;
        } else if ("inlineStr".equals(cellType)) {
            nextDataType = CellDataType.INLINESTR;
        } else if ("s".equals(cellType)) {
            nextDataType = CellDataType.SSTINDEX;
        } else if ("str".equals(cellType)) {
            nextDataType = CellDataType.FORMULA;
        }
        
        if (cellStyleStr != null) {
            int styleIndex = Integer.parseInt(cellStyleStr);
            XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
            formatIndex = style.getDataFormat();
            formatString = style.getDataFormatString();
            
            if ("m/d/yy" == formatString) {
                nextDataType = CellDataType.DATE;
                formatString = "yyyy-MM-dd";
            }
            
            if (formatString == null) {
                nextDataType = CellDataType.NULL;
                formatString = BuiltinFormats.getBuiltinFormat(formatIndex);
            }
        }
    }
    
    private String getDataValue(String value, String thisStr) {
        switch (nextDataType) {
        // 这几个的顺序不能随便交换，交换了很可能会导致数据错误
        case BOOL:
            char first = value.charAt(0);
            thisStr = first == '0' ? "FALSE" : "TRUE";
            break;
        case ERROR:
            thisStr = "\"ERROR:" + value.toString() + '"';
            break;
        case FORMULA:
            thisStr = value.toString();
            break;
        case INLINESTR:
            XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
            
            thisStr = rtsi.toString();
            rtsi = null;
            break;
        case SSTINDEX:
            String sstIndex = value.toString();
            try {
                int idx = Integer.parseInt(sstIndex);
                XSSFRichTextString rtss = new XSSFRichTextString(sst.getEntryAt(idx));
                thisStr = rtss.toString();
                rtss = null;
            } catch (NumberFormatException ex) {
                thisStr = value.toString();
            }
            break;
        case NUMBER:
            if (formatString != null) {
                thisStr = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex,
                        formatString).trim();
            } else {
                thisStr = value;
            }
            thisStr = thisStr.replace("_", "").trim();
            break;
        case DATE:
            thisStr = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex,
                    formatString);
            // 对日期字符串作特殊处理
            thisStr = thisStr.replace(" ", " T");
            break;
        default:
            thisStr = " ";
            break;
        }
        return thisStr;
    }
    
    // 单元格中的数据可能的数据类型
    private enum CellDataType {
        BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER, DATE, NULL
    }
    
}
