/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @date: 2015年3月21日下午11:30:32
 * @fileName: HtmlUtils.java
 * @version:1.0
 * @describe : 类说明。。。
 *
 *           ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2015.
 */

package me.shy.cmdtool.utils.file.html;

public class HtmlUtil {

    /**
     * 格式化Html字符串
     *
     * @param content
     *            要格式化的元字符串
     * @param outputDir
     *            存放Html文件的父路径
     * @param isRemoveScripts
     *            是否移除JavaScript脚本
     * @param isRemoveStyles
     *            是否移除CSS脚本
     * @param isReplaceHtmlTag
     *            是否移所有的Html标签
     * @return
     */
    public static String formatHtmlString(String content, String outputDir, boolean isRemoveScripts,
        boolean isRemoveStyles, boolean isReplaceHtmlTag) {
        // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }
        String scriptRegex = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
        // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
        String styleRegex = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
        // 定义HTML标签的正则表达式
        String htmlTagRegex = "<[^>]+>";
        // 定义超链接表达式
        // String linkRegex = "(<a).+?(href=)([\"\']).+?([\"\'])";
        if (content == null || content.isEmpty()) {
            return "ERROR";
        }

        // 去除js
        if (isRemoveScripts) {
            content = content.replaceAll(scriptRegex, "");
        }

        // 去除style
        if (isRemoveStyles) {
            content = content.replaceAll(styleRegex, "");
        }

        if (isReplaceHtmlTag) {
            content = content.replaceAll(htmlTagRegex, "");
        }

        // 合并空格
        content = content.replaceAll("[\\s+", " ");
        return content.trim();
    }

    /**
     * 将连接中的特殊字符替换为"_"
     *
     * @param href
     * @return
     */
    public static String replaceIllegalChar(String href) {
        if (href.contains(".")) {
            href = href.replaceAll("\\.", "_");
        }
        if (href.contains("?")) {
            href = href.replaceAll("\\?", "_");
        }
        if (href.contains("=")) {
            href = href.replaceAll("=", "_");
        }
        return href;
    }
}
