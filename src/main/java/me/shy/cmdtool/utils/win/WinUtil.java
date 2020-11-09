package me.shy.cmdtool.utils.win;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @fileName: com.ysb.commands.utils.win.WinUtil.java
 * @version: 1.0
 * @since: 2016-4-22 上午1:28:10
 * @describe : 
 * 			       windows系统工具包
 *
 * ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2016.
 */

public class WinUtil {

    /**
     *
     *描述: 获取windows任务管理器条目集合
     * @return 任务管理器条目的List
     * @author Yu Shuibo 2016-4-22 上午1:34:25
     */
    public static List<String> getTasks() {
        List<String> tasks = new ArrayList<String>();
        try {
            Process p = Runtime.getRuntime().exec("cmd /c tasklist");
            InputStream is = p.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buf = new byte[4096];
            while (is.read(buf) > 0) {
                //System.out.println(new String(buf));
                tasks.add(new String(buf));
            }
            bis.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    /**
     *
     *描述: 获取当前程序自身虚拟机在任务管理器中的pid
     * @return pid的字符串
     * @author Yu Shuibo 2016-4-22 上午1:35:22
     */
    public static String getProgressPid() {
        // get name representing the running Java virtual machine.
        String name = ManagementFactory.getRuntimeMXBean().getName();
        // get pid  
        String pid = name.split("@")[0];
        return pid;
    }
}
