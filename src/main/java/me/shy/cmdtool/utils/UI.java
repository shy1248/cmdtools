package me.shy.cmdtool.utils;

import java.awt.Component;
import java.awt.Font;
import java.util.Enumeration;
import java.util.Locale;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @fileName: com.ysb.common.utils.UI.java
 * @version: 1.0
 * @since: 2015-3-29 上午9:11:24
 * @describe : 类说明。。。
 *
 *           ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2015.
 */
public class UI {
    /** 描述：可跨平台的默认风格 */
    public static final String DEFAULT = UIManager.getCrossPlatformLookAndFeelClassName();
    /** 描述：当前系统的风格 */
    public static final String CURRENT = UIManager.getSystemLookAndFeelClassName();
    /** 描述：Metal风格 (默认) */
    public static final String METAL = "javax.swing.plaf.metal.MetalLookAndFeel";
    /** 描述：Windows风格 */
    public static final String WINDOWS = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    /** 描述：Windows Classic风格 */
    public static final String WINDOWS_CLASSIC = "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel";
    /** 描述：Nimbus风格 */
    public static final String NIM = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
    /** 描述：Motif风格 */
    public static final String MOTIF = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
    /** 描述：Mac风格 (需要在相关的操作系统上方可实现) */
    public static final String MAC = "com.sun.java.swing.plaf.mac.MacLookAndFeel";
    /** 描述：GTK风格 (需要在相关的操作系统上方可实现) */
    public static final String GTK = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";

    /**
     * 描述: UI样式跟随系统默认
     *
     * @author Yu Shuibo 2015-3-29 上午9:11:39
     */
    public static final void setDefaultUI(String type) {
        try {
            Locale locale = Locale.getDefault();
            Locale.setDefault(locale);
            UIManager.setLookAndFeel(type);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * 描述: 更新界面
     *
     * @param component
     *            要更新的组件
     * @author Yu Shuibo 2015-3-31 下午8:17:43
     */
    public static void update(Component component) {
        SwingUtilities.updateComponentTreeUI(component);
    }

    public static void setGlobalFont(Font font) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, font);
            }
        }
    }
}
