/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @date: 2015年3月21日下午11:22:36
 * @fileName: Utils.java
 * @version:1.0
 * @describe : 此类包装了一些常用的小方法
 *
 *           ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2015.
 */

package me.shy.cmdtool.utils;

import java.io.IOException;
import me.shy.cmdtool.Constants;

public class Util {

    /**
     * 描述: 获取int类型数组中的最大值
     *
     * @param numbers
     *            数组
     * @return 最大值
     * @author Yu Shuibo 2015-3-29 上午9:14:37
     */
    public static int getMax(int[] numbers) {
        int max = numbers[0];
        for (int i = 1, j = numbers.length; i < j; i++) {
            if (numbers[i] > max) {
                max = numbers[i];
            }
        }
        return max;
    }

    /**
     * 描述：线程休眠时间
     *
     * @param mi
     *            要休眠的时间，毫秒为单位
     */
    public static void waitMoment(long mi) {
        try {
            Thread.sleep(mi);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String getUserInput() {
        String msg = null;
        byte[] b = new byte[1204];
        try {
            msg = new String(b, 0, System.in.read(b));
            msg = msg.replaceAll("\r\n", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public static String formatPath(String oldPath) {
        return oldPath.replaceAll("\\\\", "\\\\\\\\");
    }

    public static String getAbsolutePath(String path) {
        String absolutePath = "";
        if (null == path || path.trim().equals("")) {
            absolutePath = Constants.CURRENT_DIR;
            // 判断是否为绝对路径
        } else if (!path.startsWith("/") && !path.contains(":")) {
            absolutePath = Constants.CURRENT_DIR + path;
        } else {
            absolutePath = path;
        }
        return absolutePath;
    }
}
