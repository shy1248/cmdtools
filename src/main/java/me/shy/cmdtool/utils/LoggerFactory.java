package me.shy.cmdtool.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @fileName: com.ysb.common.utils.LogFactory.java
 * @version: 1.0
 * @since: 2015-3-29 上午10:38:16
 * @describe : 类说明。。。
 *
 *           ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2015.
 */

public class LoggerFactory {
    public static Logger console() {
        Logger logger = Logger.getLogger("com.ysb");
        logger.setLevel(Level.ALL);
        return logger;
    }

    public static Logger getFileLoger(String fileName) {
        Logger logger = null;
        try {
            logger = Logger.getLogger("com.ysb");
            FileHandler handler = new FileHandler(fileName);
            handler.setLevel(Level.INFO);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            logger.setLevel(Level.ALL);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logger;
    }
}
