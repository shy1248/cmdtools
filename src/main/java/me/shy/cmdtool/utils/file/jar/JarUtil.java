package me.shy.cmdtool.utils.file.jar;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @fileName: com.ysb.common.utils.file.JarUtil.java
 * @version: 1.0
 * @since: 2016-3-14 下午1:28:08
 * @describe : 获取类打成jar包后，jar包的路径，名字
 *
 *           ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2016.
 */

public class JarUtil {
    /*jar包名*/
    private String jarName;
    /*jar包路徑*/
    private String jarPath;

    public JarUtil(Class<Object> clazz) {
        String path = clazz.getProtectionDomain().getCodeSource().getLocation().getFile();
        try {
            path = java.net.URLDecoder.decode(path, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        File jarFile = new File(path);
        this.jarName = jarFile.getName();
        File parent = jarFile.getParentFile();
        if (parent != null) {
            this.jarPath = parent.getAbsolutePath();
        }
    }

    /**
     *
     * 描述: 获取类文件所在jar包的包名
     *
     * @param enc
     *            输出编码
     * @param isHasExt
     *            指定是否包含拓展名
     * @return jar包名
     * @author Yu Shuibo 2016-3-14 下午1:42:42
     */
    public String getJarName(String enc, boolean isHasExt) {
        String jarName = null;
        if (isHasExt) {
            jarName = this.jarName;
        } else {
            jarName = this.jarName.split(".")[0];
        }

        try {
            return java.net.URLDecoder.decode(jarName, enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * 描述: 获取类文件所在jar包的包路径
     *
     * @param enc
     *            输出编码
     * @return jar包路径
     * @author Yu Shuibo 2016-3-14 下午1:45:10
     */
    public String getJarPath(String enc) {
        try {
            return java.net.URLDecoder.decode(jarPath, enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * 描述: 获取类文件所在jar包的包名，包括拓展名，默认以utf-8的编码输出，推荐使用该方法
     *
     * @param isHasExt
     *            指定是否包括拓展名
     * @return jar包名
     * @author Yu Shuibo 2016-3-14 下午1:45:47
     */
    public String getJarName(boolean isHasExt) {
        return getJarName("utf-8", isHasExt);
    }

    /**
     *
     * 描述:获取类文件所在jar包的包路径，默认以utf-8的编码输出，推荐使用该方法
     *
     * @return jar包路径
     * @author Yu Shuibo 2016-3-14 下午1:49:12
     */
    public String getJarPath() {
        return getJarPath("utf-8");
    }

}

