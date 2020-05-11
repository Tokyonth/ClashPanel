package com.tokyonth.clashpanel.utils.store;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.tokyonth.clashpanel.Contents;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    /**
     * 检测sdcard是否可用
     *
     * @return true为可用，否则为不可用
     */
    public static boolean isSDCardAvailable() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    public static boolean isCheckSDCardWarning() {
        return !isSDCardAvailable();
    }

    public static boolean createDir(String path) {
        if (isCheckSDCardWarning()) {
            return false;
        }
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return true;
    }

    public static File createFile(String path, String filename) {
        if (!createDir(path)) {
            return null;
        }
        if (TextUtils.isEmpty(filename)) {
            return null;
        }
        File file = null;
        file = new File(path, filename);
        if (file.exists()) {
            return file;
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            return null;
        }
        return file;
    }

    public static File createFile(String absolutePath) {
        if (TextUtils.isEmpty(absolutePath)) {
            return null;
        }
        if (isCheckSDCardWarning()) {
            return null;
        }
        File file = new File(absolutePath);
        if (file.exists()) {
            return file;
        } else {
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                return null;

            }
        }
        return file;
    }

    public static boolean isFileExist(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return false;

        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    public static void deleteFile(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return;

        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    public static File createNewFile(String path, String name) {
        if (isCheckSDCardWarning()) {
            return null;
        }
        File file = new File(path, name);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File getApkFile(Context context, String fileName) {
        return FileUtils.createFile(FileUtils.getSDCardAppCachePath(context) + File.separator + "apks", fileName);
    }

    public static String getApkAbsolutePath(Context context, String fileName) {
        return getApkFile(context,fileName).getPath();
    }
    // 程序sdcard目录
    public static String getSDCardAppCachePath(@NonNull Context context) {
        File file = context.getExternalCacheDir();
        if (file == null) {
            return null;
        }
        return file.getAbsolutePath();
    }
    public static String getExternalStorageDirectory(@NonNull Context context) {
        File file = Environment.getExternalStorageDirectory();
        if (file == null) {
            return null;
        }
        return file.getAbsolutePath();
    }

    /**
     * 将内容写入sd卡中
     * @param filename 要写入的文件名
     * @param content  待写入的内容
     */
    public static void writeExternal(String filename, String content) {
        //获取外部存储卡的可用状态
        String storageState = Environment.getExternalStorageState();
        //判断是否存在可用的的SD Card
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            filename = Contents.CLASH_CONFIG_DIR + File.separator + filename;
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(filename);
                outputStream.write(content.getBytes());
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从sd card文件中读取数据
     * @param filename 待读取的sd card
     * @return String
     */
    public static String readExternal(String filename) {
        StringBuilder sb = new StringBuilder();
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            filename = Contents.CLASH_CONFIG_DIR + File.separator + filename;
            //打开文件输入流
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(filename);
                byte[] buffer = new byte[1024];
                int len = inputStream.read(buffer);
                //读取文件内容
                while(len > 0){
                    sb.append(new String(buffer,0,len));
                    //继续将数据放到buffer中
                    len = inputStream.read(buffer);
                }
                //关闭输入流
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}

