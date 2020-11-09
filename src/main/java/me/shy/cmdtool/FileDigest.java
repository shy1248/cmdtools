package me.shy.cmdtool;

import java.io.File;
import me.shy.cmdtool.utils.Util;
import me.shy.cmdtool.utils.file.DigestUtil;

/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @fileName: com.ysb.commands.FileHash.java
 * @version: 1.0
 * @since: 2016年4月24日 下午7:13:43
 * @describe : 本程序用来计算文件的信息摘要，如MD5/SHA256等
 *
 *           ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2016.
 */

public class FileDigest {
    public static void main(String[] args) {
        FileDigest digest = new FileDigest();
        int paraSum = args.length;
        if (paraSum >= 1 || paraSum < 3) {
            String filename = "";
            String digestType = "";
            for (int i = 0; i < paraSum; i++) {
                String arg = args[i];
                if (!arg.startsWith("-f=") && !arg.startsWith("-t=")) {
                    digest.showHelp();
                }
                if (arg.startsWith("-f=")) {
                    filename = arg.replace("-f=", "");
                }
                if (arg.startsWith("-t=")) {
                    digestType = arg.toLowerCase().replace("-t=", "");
                }
            }
            if (filename.equals("")) {
                digest.showHelp();
            } else {
                filename = Util.getAbsolutePath(filename);
                File f = new File(filename);
                if (f.isDirectory()) {
                    System.out.println("Error: The path can'nt be a directory!");
                    System.exit(1);
                }
            }
            if (digestType.equals("")) {
                digestType = "md5";
            }
            System.out.printf("\r" + digestType + ": " + digest.calculate(filename, digestType) + "\n");
        } else {
            digest.showHelp();
        }
    }

    private String calculate(String filename, String digestType) {
        String resultString = "";
        File f = new File(filename);
        System.out.println("[Filename]: " + f.getName());
        System.out.println("[Size]: " + f.length() / 1024 / 1024 + "MB (" + f.length() + ")");
        System.out.println("[Path]: " + f.getParent());
        System.out.printf("\rCalculating ...");
        if (digestType.equals("crc32")) {
            resultString = DigestUtil.getCrc32(digestType);
        } else if (digestType.equals("md2")) {
            resultString = DigestUtil.getDisgestString(f, "md2");
        } else if (digestType.equals("md5")) {
            resultString = DigestUtil.getDisgestString(f, "md5");
        } else if (digestType.equals("sha1")) {
            resultString = DigestUtil.getDisgestString(f, "sha1");
        } else if (digestType.equals("sha256")) {
            resultString = DigestUtil.getDisgestString(f, "sha256");
        } else if (digestType.equals("sha384")) {
            resultString = DigestUtil.getDisgestString(f, "sha384");
        } else if (digestType.equals("sha512")) {
            resultString = DigestUtil.getDisgestString(f, "sha512");
        } else {
            System.out.println("\rThe file disgest type is'nt supported!");
            System.exit(1);
        }
        return resultString;
    }

    private void showHelp() {
        System.out.println();
        System.out.println("USAGE: fdst -f=[filename] -t=<disgest_type>");
        System.out.println();
        System.err.println("-f     --filename 目标文件名，不能缺省");
        System.out.println("-t     --disgest type 信息摘要类型，支持CRC32/MD2/MD5/SHA1/SHA256/SHA384/SHA512，缺省为MD5");
        System.out.println();
        System.out.println("本程序用来计算文件的信息摘要，如MD5/SHA256等");
        System.out.println("ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2016");
        System.exit(1);
    }
}
