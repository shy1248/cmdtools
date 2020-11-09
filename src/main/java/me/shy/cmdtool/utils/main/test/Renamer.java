package me.shy.cmdtool.utils.main.test;

import java.io.File;
import me.shy.cmdtool.utils.file.FileUtil;

/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @fileName: com.ysb.common.utils.main.test.Renamer.java
 * @version: 1.0
 * @since: 2015-4-18 下午1:43:55
 * @describe : 
 * 			        类说明。。。 
 *
 * ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2015.
 */

public class Renamer {
    public static void main(String[] args) {
        rename("jpg");
    }
    
    public static void rename(String extension){
        File parentDir = FileUtil.getDirectory(null);
        File[] targetFiles = parentDir.listFiles();
        for(int i=0,j=targetFiles.length;i<j;i++){
            File targetFile = targetFiles[i];
            if(FileUtil.getFileExtensionName(targetFile).toLowerCase().equals(extension)){
                long time = targetFile.lastModified();
                FileUtil.rename(targetFile, (time + i)+"", "jpg");
                System.out.println(targetFile.getName() + "has renamed!");
            }
        }
    }
}
