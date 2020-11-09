package me.shy.cmdtool.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @fileName: com.ysb.commands.utils.file.HashUtil.java
 * @version: 1.0
 * @since: 2016年4月24日 下午6:55:09
 * @describe : hash工具类，可计算文件的CRC3,MD5,HAS1,HAS256等
 *
 *           ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2016.
 */

public class DigestUtil {
    
    public static String getDisgestString(File f, String disgestType) {
        
        InputStream ins = null;
        String resultString = "";
        try {
            ins = new FileInputStream(f);
            if ("md2".equals(disgestType)) {
                resultString = DigestUtils.md2Hex(ins);
            } else if ("md5".equals(disgestType)) {
                resultString = DigestUtils.md5Hex(ins);
            } else if ("sha1".equals(disgestType)) {
                resultString = DigestUtils.sha1Hex(ins);
            } else if ("sha256".equals(disgestType)) {
                resultString = DigestUtils.sha256Hex(ins);
            } else if ("sha384".equals(disgestType)) {
                resultString = DigestUtils.sha384Hex(ins);
            } else if ("sha512".equals(disgestType)) {
                resultString = DigestUtils.sha512Hex(ins);
            }
            return resultString;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    
    public static String getCrc32(String filename) {
        CRC32 crc32 = new CRC32();
        FileInputStream fis = null;
        CheckedInputStream cis = null;
        String crc = "";
        try {
            fis = new FileInputStream(new File(filename));
            cis = new CheckedInputStream(fis, crc32);
            while (cis.read() != -1) {
                crc = Long.toHexString(crc32.getValue()).toUpperCase();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (cis != null) {
                try {
                    cis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return crc;
    }
}
