/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @date: 2015年3月21日下午10:45:32
 * @fileName: Timer.java
 * @version:1.0
 * @describe : 一个简单的计时器，在JLable上显示 使用方法： Timer timer = new Timer();
 *           timer.schedule(new SimpleTimer(timerLable), 0, 1000);
 *
 *           ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2015.
 */

package me.shy.cmdtool.utils;

import java.util.TimerTask;
import javax.swing.JLabel;

public class SimpleTimer extends TimerTask {
    private JLabel timerLable = null;
    private long ss = 0;
    private long mm = 0;
    private long hh = 0;

    /**
     * 构造方法
     *
     * @param timerLabel
     *            显示计时器的Lable
     */
    public SimpleTimer(JLabel timerLabel) {
        this.timerLable = timerLabel;
    }

    @Override public void run() {
        ss = ss + 1;
        if (ss >= 60) {
            mm = mm + 1;
            ss = 0;
        }
        if (mm >= 60) {
            hh = hh + 1;
            mm = 0;
        }
        String seconds;
        String minutes;
        String hours;
        if (ss < 10) {
            seconds = "0" + ss;
        } else {
            seconds = "" + ss;
        }
        if (mm < 10) {
            minutes = "0" + mm;
        } else {
            minutes = "" + mm;
        }
        if (hh < 10) {
            hours = "0" + hh;
        } else {
            hours = "" + hh;
        }

        timerLable.setText(" " + hours + ":" + minutes + ":" + seconds + " ");
    }
}
