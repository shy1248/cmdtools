/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @date: 2015年3月21日下午9:15:45
 * @fileName: JarFileFilter.java
 * @version:1.0
 * @describe : jar文件过滤器
 *
 *           ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2015.
 */

package me.shy.cmdtool.utils.file.filters;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class JarFileFilter extends FileFilter {
    @Override public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        if (file.getName().toLowerCase().endsWith(".jar")) {
            return true;
        }
        return false;
    }

    @Override public String getDescription() {
        return "Jar File (*.jar)";
    }
}