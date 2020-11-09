package me.shy.cmdtool;

import java.io.File;
import me.shy.cmdtool.utils.file.FileUtil;
import me.shy.cmdtool.utils.file.MassFileOperater;

/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @fileName: com.ysb.commands.me.shy.cmdtool.FileDecoder.java
 * @version: 1.0
 * @since: 2016-4-10 下午6:49:04
 * @describe : 本程序用来将某一目录（包含子目录）里的文件进行编码转换
 * 
 *           ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2016.
 */

public class FileDecoder extends MassFileOperater {
    private String encoding;
    
    public static void main(String[] args) throws InterruptedException {
        FileDecoder decoder = new FileDecoder();
        int paraSum = args.length;
        if (paraSum >= 1 && paraSum < 4) {
            String dir = null;
            String ext = "";
            String encoding = "";
            //初始化参数
            for (int i = 0; i < paraSum; i++) {
                String arg = args[i];
                if(!arg.startsWith("-d=") && !arg.startsWith("-e=") && arg.startsWith("-c=")){
                    decoder.showHelp();
                }else{
                    if (arg.startsWith("-d=")) {
                        dir = arg.replace("-d=", "");
                    }
                    if (arg.startsWith("-e=")) {
                        ext = arg.replace("-e=", "");
                    }
                    if (arg.startsWith("-c=")) {
                        encoding = arg.replace("-c=", "");
                    }
                }
            }
            if (encoding.equals("")) {
                System.out.println("Error: Encoding can'nt be null!");
                decoder.showHelp();
            } else {
                decoder.setEncoding(encoding);
                decoder.operate(dir, ext, true);
            }
        } else {
            decoder.showHelp();
        }
    }
    
    /**
     * @see com.ysb.commands.utils.file.MassFileOperater#progress(java.io.File)
     */
    @Override
    protected void progress(File currentFile) {
        FileUtil.decode(currentFile, encoding);
    }
    
    /**
     * 设置encoding 的值
     * 
     * @param encoding
     *            要设定的值
     */
    public final void setEncoding(String encoding) {
        this.encoding = encoding;
    }
    
    /**
     * @see com.ysb.commands.utils.file.MassFileOperater#showHelp()
     */
    @Override
    protected void showHelp() {
        System.out.println();
        System.out.println("USAGE: fdec -d<directory> -e<extension> -c=[encoding]");
        System.out.println();
        System.out.println("-d     --directory 目标目录，可以是绝对路径，也可以是相对路径，缺省为当前目录");
        System.out.println("-e     --extension 目标文件拓展名，指定要操作的文件类型，缺省为所有文件类型，多个拓展名以\",\"分割");
        System.out.println("-c     --encoding 文件的目标编码，不能缺省");
        System.out.println();
        System.out.println("本程序用来将某一目录（包含子目录）里的文件进行编码转换");
        System.out.println("ALL RIGHTS RESERVED, COPYRIGHT(C) Yu Shuibo, LIMITED 2016");
        System.exit(1);
    }
}
