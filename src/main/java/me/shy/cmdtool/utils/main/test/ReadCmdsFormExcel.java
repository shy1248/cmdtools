/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @date: 2015年3月22日下午3:26:19
 * @fileName: Test.java
 * @version:1.0
 * @describe : 类说明。。。
 * 
 *           ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2015.
 */

package me.shy.cmdtool.utils.main.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import me.shy.cmdtool.utils.file.FileUtil;
import me.shy.cmdtool.utils.file.excel.DefaultRowHandler;
import me.shy.cmdtool.utils.file.excel.ExcelUtil;
import me.shy.cmdtool.utils.file.excel.RowHandable;
import me.shy.cmdtool.utils.file.filters.ExcelFileFilter;

public class ReadCmdsFormExcel {
    
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        File[] sourceFiles = FileUtil.openFiles(null, new ExcelFileFilter());
        if (sourceFiles == null) {
            return;
        }
        String[] bscNames = new String[] { "WZHBSC007", "WZHBSC014", "WZHBSC018", "WZHBSC019",
                "WZHBSC026", "WZHBSC027", "WZHBSC028", "WZHBSC033", "WZHBSC055", "WZHBSC058",
                "WZHBSC075", "WZHBSC101", "WZHBSC102", "WZHBSC103", "WZHBSC107", "WZHBSC109",
                "WZHBSC110", "WZHBSC111", "WZHBSC112", "WZHBSC115", "WZHBSC116", "WZHBSC117",
                "WZHBSC124", "WZHBSC125", "WZHBSC128", "WZHBSC129", "WZHBSC133", "WZHBSC134",
                "WZHBSC135", "WZHBSC136", "WZHBSC137", "WZHBSC140", "WZHBSC141", "WZHBSC145",
                "WZHBSC147", "WZHBSC149", "WZHBSC150", "WZHBSC154", "WZHBSC155", "WZHBSC161",
                "WZHBSC163" };
        
        File[] cmdFiles = creatFiles(bscNames);
        
        RowHandable handler = new DefaultRowHandler(3, bscNames, new int[] { 16 });
        List<ArrayList<String>> rows = getRows(sourceFiles[0], handler, new int[] { 1 });
        writeFile(rows, bscNames, cmdFiles);
        
        /*
         * RowHandable handler2 = new DefaultRowHandler(1, bscNames, new int[] {
         * 29 }); List<ArrayList<String>> rows2 = getRows(sourceFiles[1],
         * handler2, new int[] { 2, 3, 4, 5, 6, 7 }); writeFile(rows2, bscNames,
         * cmdFiles);
         * 
         * RowHandable handler3 = new DefaultRowHandler(1, bscNames, new int[] {
         * 37 }); List<ArrayList<String>> rows3 = getRows(sourceFiles[0],
         * handler3, new int[] { 9 }); writeFile(rows3, bscNames, cmdFiles);
         * 
         * RowHandable handler4 = new DefaultRowHandler(1, bscNames, new int[] {
         * 30 }); List<ArrayList<String>> rows4 = getRows(sourceFiles[1],
         * handler4, new int[] { 2 }); writeFile(rows4, bscNames, cmdFiles);
         */
        
        System.out.println("任务完成！");
    }
    
    public static List<ArrayList<String>> getRows(File sourceFile, RowHandable handler,
            int[] sheetIds) throws Exception {
        System.out.println("处理文件：" + sourceFile.getAbsolutePath());
        ExcelUtil.read(handler, sourceFile.getAbsolutePath(), sheetIds);
        List<ArrayList<String>> rows = ((DefaultRowHandler) handler).getRows();
        return rows;
    }
    
    private static void writeFile(List<ArrayList<String>> rows, String[] bscNames, File[] cmdFiles)
            throws IOException {
        
        ArrayList<String> row = null;
        for (int i = 0; i < rows.size(); i++) {
            row = rows.get(i);
            for (int h = 0, k = bscNames.length; h < k; h++) {
                if (row.get(0).equals(bscNames[h])) {
                    org.apache.commons.io.FileUtils.writeStringToFile(cmdFiles[h], row.get(1)
                            + "\r\n", true);
                    System.out.println("写入cmd（" + row.get(1) + "）到文件：" + cmdFiles[h].getAbsolutePath());
                }
            }
        }
    }
    
    private static File[] creatFiles(String[] bscNames) {
        File[] cmdFiles = new File[bscNames.length];
        for (int i = 0, j = bscNames.length; i < j; i++) {
            cmdFiles[i] = new File("D:/Documents/NSN/Bsctemp/" + bscNames[i] + "_CMD.txt");
            if (cmdFiles[i].exists()) {
                cmdFiles[i].delete();
            }
        }
        return cmdFiles;
    }
    
}
