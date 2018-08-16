package com.msw.et.util;

import com.msw.et.exception.MyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 文件读写 删除
 *
 * @author mashuangwei
 * @create 2017-12-27 17:39
 **/
@Slf4j
public class FileUtil {

    /**
     * 保存单个文件
     *
     * @param file
     * @param path
     * @return
     */
    private static boolean saveFile(MultipartFile file, String path) {
        // 判断文件是否为空
        if (!file.isEmpty()) {
            try {
                // 转存文件
                file.transferTo(new File(path));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 保存多个文件
     *
     * @param files
     * @param path
     * @return
     */
    public static boolean saveFilesToLocal(MultipartFile[] files, String path, Long id) {
        //判断file数组不能为空并且长度大于0
        if (files != null && files.length > 0) {
            //循环获取file数组中得文件
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                //保存文件
                String savePath = path + "/" + file.getOriginalFilename();
                log.info("savePath：{}", savePath);
                if (!saveFile(file, savePath)) {
                    throw new MyException(500, "保存文件失败");
                }
            }
        }
        return true;
    }

    /**
     * 获取指定目录下的文件个数，不包含子目录
     *
     * @param path
     * @return
     */
    public static int getFileNum(String path) {
        File f = new File(path);
        if (!f.exists()) {
            return 0;
        }

        File fa[] = f.listFiles();
        int num = 0;
        for (int i = 0; i < fa.length; i++) {
            if (!fa[i].isDirectory()) {
                num++;
            }
        }
        return num;
    }

    /**
     * 在指定目录下创建文件夹
     * prefixPath: 指定目录
     * folderName: 文件夹名称
     */
    public static boolean createFolder(String prefixPath, String folderName) {
        File dir = new File(prefixPath + '/' + folderName);
        if (dir.exists()) {
            log.info("创建目录：" + folderName + "  已经存在");
            return true;
        } else {
            if (dir.mkdirs()) {
                log.info("创建目录" + prefixPath + "  成功");
                return true;
            } else {
                log.info("创建目录" + prefixPath + "  失败");
                return false;
            }
        }
    }

    /**
     * 删除指定目录下所有文件及文件夹
     *
     * @param delpath
     * @return
     *
     */
    public static void deletefile(String delpath) {
        File file = new File(delpath);
        if (file.isDirectory()) {
            String[] filelist = file.list();
            for (String delFile : filelist) {
                File delfile = new File(delpath + File.separator + delFile);
                if (delfile.isDirectory()) {
                    deletefile(delpath + File.separator + delFile);
                } else {
                    log.info("正在删除文件：" + delfile.getPath() + ",删除是否成功：" + delfile.delete());
                }
            }
            log.info("正在删除空文件夹：" + file.getPath() + ",删除是否成功：" + file.delete());
        } else {
            if (file.delete()) {
                log.info("正在删除文件：" + file.getPath() + ",删除成功");
            } else {
                log.info("正在删除文件：" + file.getPath() + ",删除失败");
            }

        }
    }

}
