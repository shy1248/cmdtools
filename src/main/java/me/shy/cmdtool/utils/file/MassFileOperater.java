package me.shy.cmdtool.utils.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import me.shy.cmdtool.utils.Util;

/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @fileName: com.ysb.commands.utils.file.MassFileOperater.java
 * @version: 1.0
 * @since: 2016-4-10 下午10:51:23
 * @describe : 批量文件操作类 ，指定一个要操作文件的父目录，通过文件后缀名来区分是否需要对该文件 进行操作
 * 
 *           ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2016.
 */

public abstract class MassFileOperater {
    public String sdir;
    private List<File> targetFiles = new ArrayList<File>();
    private int counter;
    private String extension = null;
    
    /**
     * 
     *描述: 文件操作接口
     * @param sdir 要操作的主目录
     * @param ext  要操作文件的拓展名
     * @param isContainsTargetdir 是否包含主目录下的文件
     * @throws InterruptedException 中断异常
     * @author Yu Shuibo 2016年5月1日 上午10:04:30
     */
    public void operate(String sdir, String ext, boolean isContainsTargetdir) throws InterruptedException {
        this.sdir = Util.getAbsolutePath(sdir);
        File f = new File(this.sdir);
        extension = "." + ext;
        if (f.exists()) {
            if (f.isDirectory()) {
                File[] files = f.listFiles();
                for (int i = 0; i < files.length; i++) {
                    this.filftFile(files[i], f, isContainsTargetdir);
                }
            } else {
                System.out.println("Error: The path must be a directory!");
                return;
            }
            this.execute();
            System.out.println("Task finished!");
        } else {
            System.out.println("Error: Coundn't found the dir or file!");
        }
    }
    
    /**
     * 
     *描述: 文件过滤
     * @param source 源文件
     * @param target 目标目录
     * @param isContainsTargetDir 是否包含目标目录下的文件
     * @author Yu Shuibo 2016年5月1日 上午10:06:08
     */
    private void filftFile(File source, File target, boolean isContainsTargetDir) {
        System.out.printf("\rSanning ...");
        if (isContainsTargetDir) {
          filftFile(source, target);
        } else {
            filftFileWithNoTargets(source, target);
        }
    }
    
    /**
     * 
     *描述: 文件过滤，包含目标目录
     * @param source 源文件
     * @param target 目标目录
     * @author Yu Shuibo 2016年5月1日 上午10:00:35
     */
    private void filftFile(File source, File target) {
        if (!extension.equals(".")) {
            if (extension.contains(",")) {
                String[] exts = extension.split(",");
                for (int i = 0, j = exts.length; i < j; i++) {
                    if (!source.isDirectory()
                            && source.getName().toLowerCase().endsWith(exts[i].toLowerCase())) {
                        addFileToList(source);
                    }
                }
            } else {
                if (!source.isDirectory()
                        && source.getName().toLowerCase().endsWith(extension.toLowerCase())) {
                    addFileToList(source);
                }
            }
        } else {
            if (!source.isDirectory()) {
                addFileToList(source);
            }
        }
        if (source.isDirectory()) {
            File[] files = source.listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    filftFileWithNoTargets(files[i], target);
                }
            }
        }
    }
    
    /**
     * 
     *描述: 文件过滤，不包含目标目录
     * @param source 源文件
     * @param target 目标目录
     * @author Yu Shuibo 2016年5月1日 上午10:00:35
     */
    private void filftFileWithNoTargets(File source, File target) {
        if (!extension.equals(".")) {
            if (extension.contains(",")) {
                String[] exts = extension.split(",");
                for (int i = 0, j = exts.length; i < j; i++) {
                    if (!source.isDirectory() && !source.getParent().equals(target.getPath())
                            && source.getName().toLowerCase().endsWith(exts[i].toLowerCase())) {
                        addFileToList(source);
                    }
                }
            } else {
                if (!source.isDirectory() && !source.getParent().equals(target.getPath())
                        && source.getName().toLowerCase().endsWith(extension.toLowerCase())) {
                    addFileToList(source);
                }
            }
        } else {
            if (!source.isDirectory() && !source.getParent().equals(target.getPath())) {
                addFileToList(source);
            }
        }
        if (source.isDirectory()) {
            File[] files = source.listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    filftFile(files[i], target);
                }
            }
        }
    }
    
    private void addFileToList(File file){
        System.out.println("\rFound file: " + file.getAbsolutePath());
        targetFiles.add(file);
    }
    
    private boolean execute() throws InterruptedException {
        int total = targetFiles.size();
        System.out.print("\rTotal " + total
                + " files found. Press 'Y' to continue or 'N' to terminated! (Y/N ?) ");
        String cmd = Util.getUserInput();
        if (!"Y".equals(cmd.toUpperCase())) {
            if (!"N".equals(cmd.toUpperCase())) {
                execute();
            } else {
                System.out.println("Task terminated!");
                System.exit(1);
            }
        } else {
            for (int i = 0; i < total; i++) {
                this.progress(targetFiles.get(i));
                counter = counter + 1;
                String progress = counter * 100 / total + "%";
                String filename = targetFiles.get(i).getName();
                System.out.printf("\r[%4s] Processing file: %s\n", progress, filename);
            }
            return true;
        }
        return false;
    }
    
    protected abstract void progress(File currentFile);
    
    protected abstract void showHelp();
    
}
