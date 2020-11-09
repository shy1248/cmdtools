package me.shy.cmdtool;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import me.shy.cmdtool.utils.file.MassFileOperater;

/**
 * 
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @fileName: com.ysb.commands.me.shy.cmdtool.MassCopy.java
 * @version: 1.0
 * @since: 2016-3-16 下午5:26:36
 * @describe : 本工具用来将某一文件夹下（包含子目录）里的文件复制到该文件夹下面。
 * 
 *           ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2016.
 */

public class MassCopy extends MassFileOperater {
    
    public static void main(String[] args) throws InterruptedException {
        MassCopy massCopy = new MassCopy();
        int paraSum = args.length;
        if (paraSum <= 2) {
            String dir = null;
            String ext = "";
            // 初始化参数
            for (int i = 0; i < paraSum; i++) {
                String arg = args[i];
                if (!arg.startsWith("-d=") && !arg.startsWith("-e=")) {
                    massCopy.showHelp();
                } else {
                    if (arg.startsWith("-d=")) {
                        dir = arg.replace("-d=", "");
                    }
                    if (arg.startsWith("-e=")) {
                        ext = arg.replace("-e=", "");
                    }
                }
            }
            massCopy.operate(dir, ext, false);
        } else {
            massCopy.showHelp();
        }
    }
    
    /**
     * @see com.ysb.commands.utils.file.MassFileOperater#go()
     */
    @Override
    public void showHelp() {
        System.out.println();
        System.out.println("USAGE: mcp -d=<directory> -e=<extension>");
        System.out.println();
        System.out.println("-d     --directory 目标目录，可以是绝对路径，也可以是相对路径，缺省为当前目录");
        System.out.println("-e     --extension 目标文件拓展名，指定要操作的文件类型，缺省为所有文件类型，多个拓展名以\",\"分割");
        System.out.println();
        System.out.println("本程序用来将某一目录（包含子目录）里的文件复制到该目录下");
        System.out.println("ALL RIGHTS RESERVED, COPYRIGHT(C) Yu Shuibo, LIMITED 2016");
        System.exit(1);
    }
    
    /**
     * @see com.ysb.commands.utils.file.MassFileOperater#go()
     */
    @Override
    public void progress(File currentFile) {
        try {
            FileUtils.copyFileToDirectory(currentFile, new File(super.sdir));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}