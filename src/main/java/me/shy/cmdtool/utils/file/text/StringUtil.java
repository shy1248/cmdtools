/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @date: 2015年3月21日下午11:59:39
 * @fileName: TextUtils.java
 * @version:1.0
 * @describe : 类说明。。。
 * 
 *           ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2015.
 */

package me.shy.cmdtool.utils.file.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public class StringUtil {
    
    /**
     * 从目标字符串中取出目标子串 此方法将目标字符串根据空白字符分割成一个字符串数组，然后以数组的Index来获取字段，Index从0开始
     * 
     * @param srcString
     *            当前行
     * @param begin
     *            开始的index
     * @param end
     *            结束的Index
     * @return
     */
    public static String getTargetWords(String srcString, int begin, int end) {
        String targetWords = "";
        String[] temp = StringUtils.split(srcString, " ");
        if (begin < 0)
            begin = 0;
        if (end > temp.length)
            end = temp.length - 1;
        if (begin == end) {
            try {
                return temp[begin];
            } catch (ArrayIndexOutOfBoundsException e) {
                return "";
            }
            
        }
        for (int i = begin; i <= end; i++) {
            targetWords = targetWords + temp[i] + " ";
        }
        return targetWords;
    }
    
    /**
     * 从目标中取出目标子串 ， 可以直接取到行尾，或者只取Index的字段，Index从0开始
     * 
     * @param srcString
     *            目标字符串
     * @param index
     *            TargetWords index
     * @param isEnd
     *            是否到取到到当前行尾部
     * @return
     */
    public static String getTargetWords(String srcString, int index, boolean isEnd) {
        if (isEnd) {
            return getTargetWords(srcString, index, srcString.length());
        } else {
            return getTargetWords(srcString, index, index);
        }
    }
    
    /**
     * 
     * 描述: 根据正则表达式取出目标字符串中第一个匹配的子串
     * 
     * @param srcString
     *            目标字符串
     * @param regexp
     *            正则表达式
     * @return 如果正则表达式不匹配目标字符串，则返回空值，否则返回第一个匹配的子串
     * @author Yu Shuibo 2015-3-31 下午6:39:19
     */
    public static String getTargetWords(String srcString, String regexp) {
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(srcString);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }
    
    /**
     * 
     * 描述: 分割目标字符串
     * 
     * @param srcString
     *            目标字符串
     * @param separater
     *            分隔符
     * @return 分割后的字符数组
     * @author Yu Shuibo 2015-3-31 下午7:08:50
     */
    public static String[] split(String srcString, String separater) {
        return StringUtils.split(srcString, separater);
    }
    
    /**
     * 
     * 描述: 判断是否匹配正则表达式
     * 
     * @param srcString
     *            目标字符串
     * @param regexp
     *            正则表达式
     * @return
     * @author Yu Shuibo 2015-3-28 上午8:21:06
     */
    public static boolean isMatch(String srcString, String regexp) {
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(srcString);
        if (matcher.find()) {
            return true;
        }
        return false;
    }
    
}
