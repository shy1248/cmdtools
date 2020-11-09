/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @date: 2015年3月21日下午10:17:06
 * @fileName: ExcelFileFilter.java
 * @version:1.0
 * @describe : 类说明。。。
 *
 *           ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2015.
 */

package me.shy.cmdtool.utils.file.filters;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class ExcelFileFilter extends FileFilter {

    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        if (file.getName().endsWith(".xlsx") || file.getName().endsWith(".xls")) {
            return true;
        }
        return false;
    }

    public String getDescription() {
        return "Excle File(*.xls, * xlsx)";
    }
}