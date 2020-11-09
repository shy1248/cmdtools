/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @date: 2015年3月22日下午8:35:54
 * @fileName: Excel2003Writer.java
 * @version:1.0
 * @describe : 类说明。。。
 * 
 *           ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2015.
 */

package me.shy.cmdtool.utils.file.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import me.shy.cmdtool.utils.file.FileUtil;

public class Excel2K3Writer {
    
    /**
     * 生成一个Excel文件
     * 
     * @param path
     *            目标文件的全路径（包括路径和文件名）
     * @param titleColumn
     *            表头
     * @param data
     *            要写入的内容
     * @param sheetName
     *            Sheet名字
     */
    public static void create(String path, String[] titleColumn, String[][] data, String sheetName) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(sheetName);
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFFont.COLOR_RED);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 8);
        HSSFRow titleRow = sheet.createRow(0);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(new HSSFColor.GREEN().getIndex());
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setFont(font);
        style.setWrapText(true);
        
        for (int i = 0; i < titleColumn.length; i++) {
            sheet.setColumnWidth(i, 3000);
            HSSFCell title = titleRow.createCell(i);
            title.setCellType(HSSFCell.CELL_TYPE_STRING);
            title.setCellStyle(style);
            title.setCellValue(titleColumn[i]);
        }
        
        int rowNumber = data.length > 65534 ? 65534 : data.length;
        
        for (int i = 0; i < rowNumber; i++) {
            HSSFRow row = sheet.createRow(i + 1);
            for (int j = 0; j < data[i].length; j++) {
                HSSFCell contant = row.createCell(j);
                contant.setCellType(HSSFCell.CELL_TYPE_STRING);
                contant.setCellValue(data[i][j].trim());
            }
        }
        
        try {
            File file = new File(path);
            if (FileUtil.isFileOpened(file)) {
                JOptionPane.showMessageDialog(null, "File is already open!", "Notify!",
                        JOptionPane.INFORMATION_MESSAGE);
                workbook.close();
                return;
            }
            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.flush();
            fos.close();
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 删除sheet的空行
     * 
     * @param sheet
     *            要删除的Sheet
     */
    public static void deleteBlankRow(HSSFSheet sheet) {
        int i = sheet.getLastRowNum();
        HSSFRow tempRow;
        while (i > 0) {
            i--;
            tempRow = sheet.getRow(i);
            if (tempRow.getCell(0).toString().trim().equals("")) {
                sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
            }
        }
    }
}
