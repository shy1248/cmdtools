package me.shy.cmdtool.utils.file;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import me.shy.cmdtool.utils.UI;
import me.shy.cmdtool.utils.Util;
import me.shy.cmdtool.utils.file.text.EncodingDetector;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @date: 2015年3月21日下午8:10:18
 * @fileName: FileService.java
 * @version:1.0
 * @describe : 该类为文件常用操作的封装
 *
 *           ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2015.
 */

public class FileUtil {

    private static final String LAST_PATH_KEY = "last_path";
    private static final String LAST_PATH_NODE = "JavaApp";
    private static final String OPEN_WIN_TITLE = "Open File";
    private static final String SAVE_WIN_TITLE = "Save as...";
    private static JFileChooser fileChooser = null;

    static {
        UI.setDefaultUI(UI.CURRENT);
    }

    private static File[] showBrosweWindow(Component parent, FileFilter filter, boolean isMultiSelection) {
        File[] files;
        Preferences pref = Preferences.userRoot().node(LAST_PATH_NODE);
        String lastPath = pref.get(LAST_PATH_KEY, "");
        if (!lastPath.equals("")) {
            fileChooser = new JFileChooser(lastPath);
        } else {
            fileChooser = new JFileChooser();
        }
        fileChooser.setMultiSelectionEnabled(isMultiSelection);
        fileChooser.setDialogTitle(OPEN_WIN_TITLE);
        fileChooser.setFileFilter(filter);
        fileChooser.setFileHidingEnabled(true);

        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            if (isMultiSelection) {
                files = fileChooser.getSelectedFiles();
            } else {
                files = new File[1];
                files[0] = fileChooser.getSelectedFile();
            }
            pref.put(LAST_PATH_KEY, files[0].getPath());
            return files;
        }
        return null;
    }

    private static File showSaveAsWindow(Component parent, FileFilter filter) {
        File file = null;
        Preferences pref = Preferences.userRoot().node(LAST_PATH_NODE);
        String lastPath = pref.get(LAST_PATH_KEY, "");
        if (!lastPath.equals("")) {
            fileChooser = new JFileChooser(lastPath);
        } else {
            fileChooser = new JFileChooser();
        }
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setDialogTitle(SAVE_WIN_TITLE);
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showSaveDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            if (file.exists()) {
                file.delete();
            }
            pref.put(LAST_PATH_KEY, file.getPath());
            return file;
        } else {
            return null;
        }
    }

    /**
     *
     * 描述: 打开一个"Open File"对话框，获取目录
     *
     * @param parent
     * @return
     * @author Yu Shuibo 2015-3-31 下午8:46:37
     */
    public static File getDirectory(Component parent) {
        File dir;
        Preferences pref = Preferences.userRoot().node(LAST_PATH_NODE);
        String lastPath = pref.get(LAST_PATH_KEY, "");
        if (!lastPath.equals("")) {
            fileChooser = new JFileChooser(lastPath);
        } else {
            fileChooser = new JFileChooser();
        }
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            dir = fileChooser.getSelectedFile();
            pref.put(LAST_PATH_KEY, dir.getAbsolutePath());
            return dir;
        }
        return null;
    }

    /**
     * 描述：打开一个"Open File"对话框，获取文件，多选
     *
     * @param parent
     *            该对话框的父对话框
     * @param filter
     *            要浏览的文件类型过滤器
     * @return 点击确定后，返回选中的文件列表；否则返回空值
     */
    public static File[] openFiles(Component parent, FileFilter filter) {
        return showBrosweWindow(parent, filter, true);
    }

    /**
     * 描述：打开一个"Open File"对话框，获取文件，单选
     *
     * @param parent
     *            该对话框的父对话框
     * @param filter
     *            要浏览的文件类型过滤器
     * @return 点击确定后，返回选中的文件列表；否则返回空值
     */
    public static File openFile(Component parent, FileFilter filter) {
        return showBrosweWindow(parent, filter, false)[0];
    }

    /**
     * 描述：打开一个"Save File"对话框 此方法只是让你选择一个保存目录，及保存文件名
     *
     * @param parent
     *            该对话框的父对话框
     * @param filter
     *            要保存的文件类型过滤器
     * @param srcFile
     *            要保存的源文件
     * @throws IOException
     *             保存文件失败时抛出此异常
     */
    public static void saveAs(Component parent, FileFilter filter, File srcFile) throws IOException {
        File targetFile = showSaveAsWindow(parent, filter);
        if (targetFile == null) {
            return;
        }
        FileUtils.copyFile(srcFile, targetFile);
    }

    /**
     * 描述: 保存文件
     *
     * @param src
     *            源文件
     * @param tar
     *            目标文件
     * @throws IOException
     *             保存失败时抛此异常
     * @author Yu Shuibo 2015-3-31 下午9:39:03
     */
    public static void save(File src, File tar) throws IOException {
        FileUtils.copyFile(src, tar);
    }

    /**
     * 描述：调用系统组件打开一个文件
     *
     * @param file
     *            要打开的文件
     * @throws IOException
     */
    public static void openInSystem(File file) throws IOException {
        java.awt.Desktop.getDesktop().open(file);
    }

    /**
     * 描述：写入一个对象到文件，通常用于保存程序现场
     *
     * @param object
     *            要写入的对象
     * @param file
     *            要写入的目标文件
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void writeObjectToFile(Object object, File file) throws FileNotFoundException, IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file, false));
        outputStream.writeObject(object);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 描述：从文件中读出一个对象到程序，通常用于恢复程序现场
     *
     * @param file
     *            保存了对象的源文件
     * @return 读取的对象
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object readObjectFromFile(File file)
        throws FileNotFoundException, IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
        Object object = inputStream.readObject();
        inputStream.close();
        return object;
    }

    /**
     * 描述：判断文件是否被打开
     *
     * @param file
     *            要判断的文件
     * @return 如果文件被打开则返回true，否则为false
     */
    public static boolean isFileOpened(File file) {
        if (file.exists()) {
            if (!file.renameTo(file)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 描述：打开文件的BufferedReader
     *
     * @param file
     *            目标文件
     * @return 该文件的BufferedReader
     */
    public static BufferedReader getReader(File file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return reader;
    }

    /**
     * 描述：打开文件的BufferedWriter
     *
     * @param file
     *            目标文件
     * @return 该文件的BufferedWriter
     */
    public static BufferedWriter getWriter(File file, boolean isAppend) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, isAppend)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return writer;
    }

    /**
     * 描述：实现文件删除功能，将src文件删除掉 ,如何src是目录，则删除整个目录
     *
     * @param src
     *            要删除的目标文件
     */
    public static void delete(File src) {
        if (src.isFile()) {
            src.delete();
        }
        if (src.isDirectory()) {
            File[] f = src.listFiles();
            for (int i = 0; i < f.length; i++) {
                if (f[i].isFile()) {
                    f[i].delete();
                }
                if (f[i].isDirectory()) {
                    delete(f[i]); // 递归
                    f[i].delete(); // 将空文件夹删除，若不加这句，文件夹是删不掉的
                }
            }
        }
    }

    /**
     * 描述：文件重命名，包括拓展名
     *
     * @param src
     *            源文件
     * @param newName
     *            新的文件名
     * @param newExtension
     *            新拓展名
     * @return 重命名成功返回true，否则为false
     */
    public static boolean rename(File src, String newName, String newExtension) {
        String path = src.getAbsolutePath();
        String baseName = FilenameUtils.getBaseName(src.getName());
        String extension = FilenameUtils.getExtension(src.getName());
        File newFile = new File(path.replace(baseName, newName).replace(extension, newExtension));
        if (!src.exists()) {
            return false;
        } else {
            return src.renameTo(newFile);
        }
    }

    /**
     * 描述: 文件重命名，不包括拓展名
     *
     * @param src
     *            源文件
     * @param newName
     *            新名称
     * @return 重命名成功返回true，否则为false
     * @author Yu Shuibo 2015-3-31 下午10:14:03
     */
    public static boolean rename(File src, String newName) {
        return rename(src, newName, FilenameUtils.getExtension(src.getName()));
    }

    /**
     * 描述：对目录里面的所有文件按照数字方式进行重命名
     *
     * @param dir
     *            源目录
     * @param isContanisOldName
     *            是否包含旧的文件名
     */
    public static void rename(File dir, boolean isContanisOldName) {
        if (!dir.isDirectory()) {
            return;
        } else {
            File[] f = dir.listFiles();
            for (int i = 0; i < f.length; i++) {
                String baseName = FilenameUtils.getBaseName(f[i].getName());
                String extension = FilenameUtils.getExtension(f[i].getName());
                if (isContanisOldName) {
                    rename(f[i], i + "_" + baseName, extension);
                } else {
                    rename(f[i], i + "", extension);
                }
            }
        }
    }

    /**
     * 描述: 获取文件名，不包括拓展名
     *
     * @param file
     *            目标文件
     * @return
     * @author Yu Shuibo 2015-3-31 下午6:22:53
     */
    public static String getFileBaseName(File file) {
        return FilenameUtils.getBaseName(file.getAbsolutePath());
    }

    /**
     * 描述: 获取文件的拓展名
     *
     * @param file
     *            目标文件
     * @return
     * @author Yu Shuibo 2015-3-31 下午6:26:40
     */
    public static String getFileExtensionName(File file) {
        return FilenameUtils.getExtension(file.getAbsolutePath());
    }

    /**
     * 描述: 获取文件名，包括拓展名
     *
     * @param file
     *            目标文件
     * @return
     * @author Yu Shuibo 2015-3-31 下午6:25:10
     */
    public static String getFileName(File file) {
        return FilenameUtils.getName(file.getAbsolutePath());
    }

    /**
     *
     * 描述: 转码，比如将一个文件的编码由oldEncoding转为newEncoding
     *
     * @param file
     *            需要转码的文件
     * @param oldEncoding
     *            原编码,如gbk
     * @param newEncoding
     *            新编吗,如utf-8
     * @author Yu Shuibo 2016-4-11 上午2:24:51
     */
    public static void decode(File file, String newEncoding) {
        String name = FileUtil.getFileBaseName(file);
        String ext = FileUtil.getFileExtensionName(file);

        FileOutputStream fos = null;
        FileInputStream fis = null;
        InputStreamReader isr = null;
        OutputStreamWriter osw = null;
        File tmp = new File(file.getParent() + "\\\\" + name + "_" + newEncoding + "." + ext);

        try {
            String oldEncoding = EncodingDetector.detect(file);
            if (null == oldEncoding) {
                System.out.println("Can'nt detected source file encoding, please give it manually: ");
                oldEncoding = Util.getUserInput();
            }
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis, oldEncoding);
            fos = new FileOutputStream(tmp);
            osw = new OutputStreamWriter(fos, newEncoding);
            char c;
            int i;
            while ((i = isr.read()) != -1) {
                c = (char)i;
                osw.write(c);
                osw.flush();
            }
            FileUtil.delete(file);
            FileUtil.rename(tmp, name);
        } catch (FileNotFoundException e) {
            System.out.println("Error: File can'nt found!");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Error: Unsupported encoding!");
        } catch (IOException e) {
            System.out.println("Error: File accessed failed!");
        } finally {
            try {
                if (null != isr) {
                    isr.close();
                }
                if (null != fis) {
                    fis.close();
                }
                if (null != fos) {
                    fos.close();
                }
                if (null != osw) {
                    osw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileUtil.delete(tmp);
        }
    }

}
