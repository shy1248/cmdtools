package me.shy.cmdtool.utils.file.excel;

import java.io.IOException;

/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @date: 2015年3月21日下午9:39:34
 * @fileName: EcxelOpretions.java
 * @version:1.0
 * @describe : Excel 文件常用操作类封装 poi读取excel 支持2003 --2007
 * 
 *           ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2015.
 */
public class ExcelUtil {
    
    // excel2003扩展名
    private static final String EXCEL03_EXTENSION = ".xls";
    // excel2007扩展名
    private static final String EXCEL07_EXTENSION = ".xlsx";
    
    /**
     * 描述：使用EventUserModel方式读取Excel文件，该方式占用资源小，速度快
     * 
     * @param rowHandler
     *            业务逻辑处理接口
     * @param fileName
     *            目标文件全路径
     * @param sheetIds
     *            目标sheet的集合，从1开始，有序
     * @throws Exception
     *             目标文件不合要求或是sheet不存在
     */
    public static void read(RowHandable rowHandler, String fileName, int[] sheetIds)
            throws Exception {
        // 处理excel2003文件
        if (fileName.endsWith(EXCEL03_EXTENSION)) {
            Excel2K3Reader excel03 = new Excel2K3Reader();
            excel03.setRowHandler(rowHandler);
            // 处理excel2007文件
            excel03.process(fileName);
        } else if (fileName.endsWith(EXCEL07_EXTENSION)) {
            Excel2K7Reader excel07 = new Excel2K7Reader();
            excel07.setRowHandler(rowHandler);
            excel07.process(fileName, sheetIds);
        } else {
            throw new RuntimeException("文件不是excel文档或是sheet不存在！");
        }
        
    }
    
    /**
     * 描述: 使用EventUserModel方式写入Excel文件，该方式占用资源小，速度快
     * 
     * @param writer
     *            AbstractExcel2K7Writer的实现类
     * @param fileName
     *            写入的Excel文件名
     * @param sheetName
     *            写入Excel文件的sheet名
     * @author Yu Shuibo 2015-3-29 上午9:22:13
     */
    public static void write(AbstractExcel2K7Writer writer, String fileName, String sheetName) {
        try {
            writer.process(fileName, sheetName);
        } catch (IOException e) {
            throw new RuntimeException("Excel写入时出错！");
        }
    }
}
