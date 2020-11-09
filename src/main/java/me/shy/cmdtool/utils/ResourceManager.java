package me.shy.cmdtool.utils;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;

/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @fileName: com.ysb.common.utils.ResouceManager.java
 * @version: 1.0
 * @since: 2015-3-29 上午9:09:25
 * @describe : 资源管理器类，使用前先要用Set方法设置Properties文件名和Image路径文件的
 *
 *           ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2015.
 */
public class ResourceManager {
    private static String bundleNanme;
    private static String imagesPath;

    private ResourceManager() {
    }

    /**
     * 描述: 通过properties的key获取值，通常在做国际化时用
     *
     * @param key
     *            key
     * @return 值
     * @author Yu Shuibo 2015-3-29 上午9:09:49
     */
    public static String getString(String key) {
        try {
            return ResourceBundle.getBundle(bundleNanme).getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }

    /**
     * 描述: 通过文件名获得ImageIcno对象
     *
     * @param filename
     *            ImageIcno对应的文件名
     * @return
     */
    public static ImageIcon getImageIcon(String filename) {
        return new ImageIcon(getFileURL(filename));
    }

    private static URL getFileURL(String filename) {
        String path = imagesPath + filename;
        return ClassLoader.class.getResource(path);
    }

    /**
     * @param bundleNanme
     *            设置properties文件名称
     */
    public static final void setBundleNanme(String bundleNanme) {
        ResourceManager.bundleNanme = bundleNanme;
    }

    /**
     * @param images路径
     *            （不包含文件名）
     */
    public static final void setImagesPath(String imagesPath) {
        ResourceManager.imagesPath = imagesPath;
    }

    /**
     *
     *描述: 实用于ImageIcon的缩放
     * @param icon 目标ImageIcon
     * @param with 目标宽度
     * @param height 目标高度
     * @return 缩放后的ImageIcon对象
     * @author Yu Shuibo 2015-11-14 上午12:59:19
     */
    public static final ImageIcon reSizeImageIcon(ImageIcon icon, int with, int height) {
        icon.getImage();
        Image temp = icon.getImage().getScaledInstance(with, height, Image.SCALE_DEFAULT);
        icon = new ImageIcon(temp);
        return icon;
    }

    /**
     *
     *描述: 适用于BufferedImage图片缩放
     * @param bufImage 目标图片
     * @param width 缩放目标宽度
     * @param height 缩放目标高度
     * @return 缩放后的图片
     * @author Yu Shuibo 2015-11-14 上午12:47:02
     */
    public static BufferedImage resizeBuff(BufferedImage bufImage, int width, int height) {
        AffineTransform transform = AffineTransform.getScaleInstance((double)width / (double)bufImage.getWidth(),
            (double)height / (double)bufImage.getHeight());
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        return op.filter(bufImage, null);
    }
}
