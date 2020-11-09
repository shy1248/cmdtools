package me.shy.cmdtool;

/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @fileName: com.ysb.commands.me.shy.cmdtool.Constants.java
 * @version: 1.0
 * @since: 2016年5月1日 上午11:03:09
 * @describe : 类说明。。。
 *
 *           ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2016.
 */

public class Constants {
    public static final String CURRENT_DIR = Thread.currentThread().getContextClassLoader()
            .getResource("").toString().replaceAll("file:/", "").replaceAll("%20", " ");
}
