package me.shy.cmdtool.utils.main.test;

import java.io.File;
import me.shy.cmdtool.utils.file.FileUtil;

/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @fileName: com.ysb.common.utils.main.test.Test.java
 * @version: 1.0
 * @since: 2015-3-29 上午10:53:10
 * @describe : 类说明。。。
 *
 *           ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2015.
 */

public class Test {
    public static void main(String[] args) {
        File src = FileUtil.openFile(null, null);
        FileUtil.rename(src, "test", "txt");
    }
}
