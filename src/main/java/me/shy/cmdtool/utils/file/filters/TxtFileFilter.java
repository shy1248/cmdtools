/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @date: 2015年3月21日下午8:17:52
 * @fileName: TxtFileFilter.java
 * @version:1.0
 * @describe : TXT文件过滤器
 *
 *           ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2015.
 */

package me.shy.cmdtool.utils.file.filters;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class TxtFileFilter extends FileFilter {

    @Override public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        if (file.getName().toLowerCase().endsWith(".txt") || file.getName().toLowerCase().endsWith(".log")) {
            return true;
        }
        return false;
    }

    @Override public String getDescription() {
        return "Text File (*.txt, *log)";
    }
}
