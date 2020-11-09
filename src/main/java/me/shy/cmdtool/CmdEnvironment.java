package me.shy.cmdtool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.registry.RegStringValue;
import com.registry.RegistryKey;
import com.registry.ValueType;

/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @fileName: com.ysb.commands.CmdCustom.java
 * @version: 1.0
 * @since: 2016-3-15 下午12:56:47
 * @describe : commands运行环境
 * 
 *           ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2016.
 */

public class CmdEnvironment {
    
    /* 代表windows 系统注册表HKEY_LOCAL_MACHINE根节点 */
    private static final RegistryKey LOCAL_MACHINE = RegistryKey
            .getRootKeyForIndex(RegistryKey.HKEY_LOCAL_MACHINE_INDEX);
    /*
     * 代表cmd自动启动位置的注册表项:HKLM\Software\Microsoft\Command
     * Processor,该项下面的AutoRun指定cmd启动时自动运行的bat文件
     */
    private static final RegistryKey CMD_PROCESSOR = new RegistryKey(LOCAL_MACHINE,
            "\\Software\\Microsoft\\Command Processor");
    /* 修改系統CLASSPATH的這測表項 */
    private static final RegistryKey ENVIRONMENT_PATH = new RegistryKey(LOCAL_MACHINE,
            "\\SYSTEM\\CurrentControlSet\\Control\\Session Manager\\Environment");
    /* cmd啓動時自動運行的bat文件名 */
    private static final String NAME = "cmd_automatic.bat";
    /* doskey语句构成要素的hashmap集合，其中key为cmd中的命令，value为对应java类名 */
    private static final Map<String, String> DOSKEYS = new HashMap<String, String>();
    static {
        DOSKEYS.put("mcp", "com.ysb.commands.me.shy.cmdtool.MassCopy");
        DOSKEYS.put("fdec", "com.ysb.commands.me.shy.cmdtool.FileDecoder");
        DOSKEYS.put("fdst", "com.ysb.commands.me.shy.cmdtool.FileDigest");
    }
    /* 项目bin目录 */
    private static String binPath = null;
    /* cmd自动运行的bat文件路径 */
    private static String batFilePath = null;
    
    /**
     * 
     * 描述: main方法
     * 
     * @param args
     * @author Yu Shuibo 2016年4月30日 下午1:21:51
     */
    public static void main(String[] args) {
        createDoskey();
        registAutoRun();
        setClassPath();
    }
    
    /**
     * 
     * 描述:创建cmd自动执行的bat文件并向bat文件里插入Doskey语句。
     * Doskey相当于linux下的alias指令，其语法为："@Doskey name=source name $*"，
     * name为要设置的别名，source name为原命令，@表示执行这条语句时不现实本身， $*表示这条语句可以接受参数
     * 
     * @param name
     *            要设定的别名
     * @param className
     *            调用该函数的类的类名（包括包名）
     * @author Yu Shuibo 2016-3-16 下午5:04:55
     */
    protected static void createDoskey() {
        
        // 创建"cmd_automatic.bat"文件
        String path = Thread.currentThread().getContextClassLoader().getResource("").toString()
                .replace("file:/", "");
        binPath = path.replaceAll("/", "\\\\");
        batFilePath = "c:\\" + NAME;
        File batFile = new File(batFilePath);
        if (batFile.exists()) {
            batFile.delete();
        }
        try {
            batFile.createNewFile();
            // 设置bat文件只读属性，取消为-R
            Runtime.getRuntime().exec("attrib \"" + batFile.getAbsolutePath() + "\" +R");
         // 设置bat文件隐藏属性，取消为-H
            Runtime.getRuntime().exec("attrib \"" + batFile.getAbsolutePath() + "\" +H");
        } catch (IOException e) {
            System.out.println("Create file 'Cmd_automatic.bat' fail!");
            e.printStackTrace();
        }
        // 向"cmd_automatic.bat"文件写入doskey语句
        BufferedWriter writer = null;
        String libPath = binPath.replace("bin\\", "lib");
        try {
            writer = new BufferedWriter(new FileWriter(batFilePath, true));
            for (Map.Entry<String, String> entry : DOSKEYS.entrySet()) {
                // Doskey语句
                String doskey = "@doskey " + entry.getKey() + "=java -Djava.ext.dirs=" + libPath
                        + " " + entry.getValue() + " $*";
                writer.write(doskey);
                writer.newLine();
                writer.flush();
                System.out.println("Doskey \"" + doskey + "\" creat succeed!");
            }
        } catch (IOException e) {
            System.out.println("Insert doskey to 'Cmd_automatic.bat' fail!");
            e.printStackTrace();
        } finally {
            if (null != writer) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 
     * 描述:检测cmd自动运行的注册表中是否有设置autorun的bat文件，如果没设就设立
     * 
     * @author Yu Shuibo 2016-3-16 下午5:16:46
     */
    protected static void registAutoRun() {
        if (CMD_PROCESSOR.valueExists("AutoRun")) {
            RegStringValue value = (RegStringValue) CMD_PROCESSOR.getValue("AutoRun");
            String dir = value.getValue();
            if (null == dir || !dir.toUpperCase().equals(batFilePath.toUpperCase())) {
                value.setValue(batFilePath);
            }
        } else {
            RegStringValue value = (RegStringValue) CMD_PROCESSOR.newValue("AutoRun",
                    ValueType.REG_SZ);
            value.setValue(batFilePath);
        }
    }
    
    /**
     * 
     * 描述:将该项目路径设定到CLASSPATH里面，这样可以保证任何地方都可以运行java的class文件
     * 
     * @author Yu Shuibo 2016-3-16 下午5:17:49
     */
    protected static void setClassPath() {
        RegStringValue value = (RegStringValue) ENVIRONMENT_PATH.getValue("CLASSPATH");
        String oldEnvPath = value.getValue();
        String[] paths = oldEnvPath.split(";");
        for (int i = 0, j = paths.length; i < j; i++) {
            if (binPath.equals(paths[i])) {
                return;
            }
        }
        String newEnvPath = oldEnvPath + ";" + binPath;
        value.setValue(newEnvPath);
    }
}
