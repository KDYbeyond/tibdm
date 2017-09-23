package com.thit.tibdm.NettyTransmission.util;

import com.thit.tibdm.util.ResourceUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by dongzhiquan on 2017/3/14.
 */
public class FileHandleUtil {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(FileHandleUtil.class);

    /**
     * 创建文件
     * @param msg 字符
     */

    public static void createFiles(String msg) {

        String filePath = null;
        filePath = ResourceUtil.getProValueByNameAndKey("server", "file_path") + File.separator + getDay() + File.separator + UUID.randomUUID() +
                ResourceUtil.getProValueByNameAndKey("server", "file_extension");
        logger.info(filePath);
        File dir = new File(ResourceUtil.getProValueByNameAndKey("server", "file_path") + File.separator + getDay());
        File file = new File(filePath);

        try {
            if (!dir.exists()) {
                if (dir.mkdirs()) {
                    logger.info("创建目录" + dir + "成功!");
                } else {
                    logger.error("创建目录" + dir + "失败!");
                }
            }
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
                if (newFile) {
                    logger.info("创建文件" + file + "成功!");
                    save2File(msg, filePath);
                }
            } else {
                logger.error("创建文件" + file + "失败!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param msg 字符
     * @param filePath 文件路径
     */
    public static void save2File(String msg, String filePath) {

        File file = FileUtils.getFile(filePath);

        try {
            FileUtils.writeStringToFile(file, msg, "GBK", true);
            logger.info("消息写入文件!");
        } catch (IOException e) {
            logger.error(FileHandleUtil.class + "消息写入文件失败!");
            e.printStackTrace();
        }


    }

    /**
     * @return String
     */
    public static String getDay() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
        String dateNowStr = sdf.format(d);
        return dateNowStr;
    }


}
