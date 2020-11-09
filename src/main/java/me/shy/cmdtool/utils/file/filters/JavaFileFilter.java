package me.shy.cmdtool.utils.file.filters;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @fileName: com.ysb.common.utils.file.filters.JavaFileFilter.java
 * @version: 1.0
 * @since: 2015-3-31 上午5:02:36
 * @describe : 
 * 			        类说明。。。 
 *
 * ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2015.
 */

public class JavaFileFilter extends FileFilter {

    @Override public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        if (file.getName().toLowerCase().endsWith(".java")) {
            return true;
        }
        return false;
    }

    @Override public String getDescription() {
        return "Java Source File (*.java)";
    }
}
