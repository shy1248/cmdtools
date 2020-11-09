package me.shy.cmdtool.utils.file.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @date: 2015年3月22日下午8:37:04
 * @fileName: AbstractExcel2007Writer.java
 * @version:1.0
 * @describe : 抽象excel2007读入器，先构建.xlsx一张模板，改写模板中的sheet.xml,使用这种方法
 *           写入.xlsx文件，不需要太大的内存。Excel2007以后的文件实际上是一个zip文件，可以
 *           将.xlsx文件改后缀名为.zip，然后打开就可以看到了
 * 
 *           ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2015.
 */
public abstract class AbstractExcel2K7Writer {
    
    /**
     * 描述：xml元数据文件写入器
     */
    private SpreadsheetWriter sheetWriter;
    
    /**
     * 描述: 生成Excel表格
     * 
     * @param fileName
     *            要生成的Excel表格的名字
     * @throws IOException
     *             文件生成失败时抛出此异常
     * @author Yu Shuibo 2015-3-29 上午5:54:45
     */
    public void process(String fileName, String sheetName) throws IOException {
        // 建立工作簿和电子表格对象
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);
        // 持有电子表格数据的xml文件名 例如 /xl/worksheets/sheet1.xml
        String sheetRef = sheet.getPackagePart().getPartName().getName();
        // 创建一个模板文件
        FileOutputStream outpurStream = new FileOutputStream("template.xlsx");
        workbook.write(outpurStream);
        workbook.close();
        outpurStream.close();
        // 生成xml文件
        File tmp = File.createTempFile("sheet", ".xml");
        Writer fw = new FileWriter(tmp);
        sheetWriter = new SpreadsheetWriter(fw);
        // 开始写入临时文件
        sheetWriter.sheetStart();
        generate();
        sheetWriter.sheetEnd();
        fw.close();
        // 使用产生的数据替换模板
        File templateFile = new File("template.xlsx");
        FileOutputStream out = new FileOutputStream(fileName);
        substitute(templateFile, tmp, sheetRef.substring(1), out);
        out.close();
        // 删除文件之前调用一下垃圾回收器，否则无法删除模板文件
        System.gc();
        // 删除临时模板文件
        if (templateFile.isFile() && templateFile.exists()) {
            templateFile.delete();
        }
    }
    
    /**
     * 描述: 业务逻辑接口，类使用者需要复写此方法，该方法中主要通过调用rowSatrt(),rowEnd(),creatCell()方法来填表
     * 
     * @throws IOException
     *             文件写入失败时抛出此异常
     * @author Yu Shuibo 2015-3-29 上午6:38:00
     */
    public abstract void generate() throws IOException;
    
    /**
     * 描述: 插入新行
     * 
     * @param rowNum
     *            要插入的行Id，从0开始
     * @throws IOException
     *             文件写入失败时返回此异常
     * @author Yu Shuibo 2015-3-29 上午5:34:56
     */
    public void rowSatrt(int rowNum) throws IOException {
        sheetWriter.rowStart(rowNum);
    }
    
    /**
     * 描述: 以字符串为值样式插入一个cell，不带style
     * 
     * @param columnIndex
     *            列Id
     * @param value
     *            cell值，字符串
     * @throws IOException
     *             文件写入失败时返回此异常
     * @author Yu Shuibo 2015-3-29 上午5:42:17
     */
    public void createCell(int columnIndex, String value) throws IOException {
        sheetWriter.createCell(columnIndex, value);
    }
    
    /**
     * 描述: 以双精度数值样式为值样式插入一个cell，不带style
     * 
     * @param columnIndex
     *            列Id
     * @param value
     *            cell值，double类型
     * @throws IOException
     *             文件写入失败时返回此异常
     * @author Yu Shuibo 2015-3-29 上午5:48:49
     */
    public void createCell(int columnIndex, double value) throws IOException {
        sheetWriter.createCell(columnIndex, value);
    }
    
    /**
     * 描述: 插入行结束标志
     * 
     * @throws IOException
     *             文件写入失败时返回此异常
     * @author Yu Shuibo 2015-3-29 上午5:34:38
     */
    public void endRow() throws IOException {
        sheetWriter.rowEnd();
    }
    
    /**
     * 描述：此方法用于将模板文件中非数据文件与我们生成的xml数据文件打包成新的excel文件
     * 
     * @param zipfile
     *            模板文件
     * @param tmpfile
     *            生成的xml底层文件
     * @param entry
     *            要替换的xml数据文件名, e.g. xl/worksheets/sheet1.xml
     * @param out
     *            最终要生成的excel文件的写入流
     */
    private static void substitute(File zipfile, File tmpfile, String entry, OutputStream out)
            throws IOException {
        ZipFile zip = new ZipFile(zipfile);
        ZipOutputStream zos = new ZipOutputStream(out);
        @SuppressWarnings("unchecked")
        Enumeration<ZipEntry> en = (Enumeration<ZipEntry>) zip.entries();
        // 首先将除了装载Excel数据的xml文件以外的文件复制新生成的Excel文件里
        while (en.hasMoreElements()) {
            ZipEntry ze = en.nextElement();
            if (!ze.getName().equals(entry)) {
                zos.putNextEntry(new ZipEntry(ze.getName()));
                InputStream is = zip.getInputStream(ze);
                copyStream(is, zos);
                is.close();
            }
        }
        // 然后将我们生成的Excel的xml数据文件复制到新生成的Excel文件里
        zos.putNextEntry(new ZipEntry(entry));
        InputStream is = new FileInputStream(tmpfile);
        copyStream(is, zos);
        is.close();
        zos.close();
        zip.close();
    }
    
    private static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int count;
        while ((count = in.read(buffer)) >= 0) {
            out.write(buffer, 0, count);
        }
    }
    
    /**
     * Excel底层xml文件生成器
     */
    private static class SpreadsheetWriter {
        private final Writer writer;
        private int rowNumber;
        private static String LINE_SEPARATOR = System.getProperty("line.separator");
        
        private SpreadsheetWriter(Writer writer) {
            this.writer = writer;
        }
        
        /**
         * 描述: 构建xml头部，在写入sheet数据之前都需要此操作
         * 
         * @throws IOException
         *             文件写入失败时返回此异常
         * @author Yu Shuibo 2015-3-29 上午5:24:19
         */
        private void sheetStart() throws IOException {
            writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                    + "<worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">");
            writer.write("<sheetData>" + LINE_SEPARATOR);
        }
        
        /**
         * 
         * 描述: 写入xml尾部，在sheet数据写入完成之后都需要此操作
         * 
         * @throws IOException
         *             文件写入失败时返回此异常
         * @author Yu Shuibo 2015-3-29 上午5:32:48
         */
        private void sheetEnd() throws IOException {
            writer.write("</sheetData>");
            writer.write("</worksheet>");
        }
        
        /**
         * 描述: 插入新行
         * 
         * @param rowNum
         *            要插入的行Id，从0开始
         * @throws IOException
         *             文件写入失败时返回此异常
         * @author Yu Shuibo 2015-3-29 上午5:34:56
         */
        private void rowStart(int rowNum) throws IOException {
            writer.write("<row r=\"" + (rowNum + 1) + "\">" + LINE_SEPARATOR);
            this.rowNumber = rowNum;
        }
        
        /**
         * 描述: 插入行结束标志
         * 
         * @throws IOException
         *             文件写入失败时返回此异常
         * @author Yu Shuibo 2015-3-29 上午5:34:38
         */
        private void rowEnd() throws IOException {
            writer.write("</row>" + LINE_SEPARATOR);
        }
        
        /**
         * 以字符串为值样式插入一个cell
         * 
         * @param columnIndex
         *            列Id
         * @param value
         *            cell值，字符串
         * @param styleIndex
         *            cell Style
         * @throws IOException
         *             文件写入失败时返回此异常
         */
        private void createCell(int columnIndex, String value, int styleIndex) throws IOException {
            String ref = new CellReference(rowNumber, columnIndex).formatAsString();
            writer.write("<c r=\"" + ref + "\" t=\"inlineStr\"");
            if (styleIndex != -1) {
                writer.write(" s=\"" + styleIndex + "\"");
            }
            writer.write(">");
            writer.write("<is><t>" + XMLEncoder.encode(value) + "</t></is>");
            writer.write("</c>");
        }
        
        /**
         * 描述: 以字符串为值样式插入一个cell，不带style
         * 
         * @param columnIndex
         *            列Id
         * @param value
         *            cell值，字符串
         * @throws IOException
         *             文件写入失败时返回此异常
         * @author Yu Shuibo 2015-3-29 上午5:42:17
         */
        private void createCell(int columnIndex, String value) throws IOException {
            createCell(columnIndex, value, -1);
        }
        
        /**
         * 描述: 以双精度数值样式为值样式插入一个cell
         * 
         * @param columnIndex
         *            列Id
         * @param value
         *            cell值，double类型
         * @param styleIndex
         *            cell style
         * @throws IOException
         *             文件写入失败时返回此异常
         * @author Yu Shuibo 2015-3-29 上午5:45:34
         */
        private void createCell(int columnIndex, double value, int styleIndex) throws IOException {
            String ref = new CellReference(rowNumber, columnIndex).formatAsString();
            writer.write("<c r=\"" + ref + "\" t=\"n\"");
            if (styleIndex != -1) {
                writer.write(" s=\"" + styleIndex + "\"");
            }
            writer.write(">");
            writer.write("<v>" + value + "</v>");
            writer.write("</c>");
        }
        
        /**
         * 描述: 以双精度数值样式为值样式插入一个cell，不带style
         * 
         * @param columnIndex
         *            列Id
         * @param value
         *            cell值，double类型
         * @throws IOException
         *             文件写入失败时返回此异常
         * @author Yu Shuibo 2015-3-29 上午5:48:49
         */
        private void createCell(int columnIndex, double value) throws IOException {
            createCell(columnIndex, value, -1);
        }
        
        /**
         * 描述: 以日期格式为值样式插入一个cell
         * 
         * @param columnIndex
         *            列Id
         * @param value
         *            cell值，Calendar类型
         * @param styleIndex
         *            cell style
         * @throws IOException
         *             文件写入失败时返回此异常
         * @author Yu Shuibo 2015-3-29 上午5:48:56
         */
        @SuppressWarnings("unused")
        private void createCell(int columnIndex, Calendar value, int styleIndex) throws IOException {
            createCell(columnIndex, DateUtil.getExcelDate(value, false), styleIndex);
        }
    }
}