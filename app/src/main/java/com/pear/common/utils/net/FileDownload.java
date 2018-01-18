package com.pear.common.utils.net;


import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 文件下载
 */

public class FileDownload {

    private static Logger log = Logger.getLogger(FileDownload.class);

    /**
     * 下载远程文件并保存到本地
     *
     * @param remoteFilePath
     *            远程文件路径
     * @param localFilePath
     *            本地文件路径
     */
    public static boolean downloadFile(String remoteFilePath,
                                       String localFilePath) {
        log.debug("准备下载文件，remoteFilePath:" + remoteFilePath
                + " localFilePath:" + localFilePath);
        URL urlfile = null;
        HttpURLConnection httpUrl = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            File f = new File(localFilePath);
            urlfile = new URL(remoteFilePath);
            httpUrl = (HttpURLConnection) urlfile.openConnection();
            httpUrl.setConnectTimeout(20000);
            httpUrl.setReadTimeout(60000);
            httpUrl.connect();

            int available = httpUrl.getContentLength();
            log.debug("总大小:" + (available));

            bis = new BufferedInputStream(httpUrl.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream(f));
            int len = 2048;
            byte[] b = new byte[len];

            int percent = 10;
            int downSize = 0;
            while ((len = bis.read(b)) != -1) {
                bos.write(b, 0, len);
                downSize += len;
                if (downSize / (double) available >= percent / 100.0) {
                    percent += 10;
                    log.debug("percent:"+percent);
                }
            }
            bos.flush();
            bis.close();
            httpUrl.disconnect();
        } catch (Exception e) {
            log.debug("下载文件出错，remoteFilePath:" + remoteFilePath
                    + " localFilePath:" + localFilePath + "  " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if(null!=bis)
                    bis.close();
                if(null!=bos)
                    bos.close();
            } catch (Exception e) {
                log.debug("下载文件出错，remoteFilePath:" + remoteFilePath
                        + " localFilePath:" + localFilePath + "  "
                        + e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
        log.debug("下载文件成功，remoteFilePath:" + remoteFilePath
                + " localFilePath:" + localFilePath);
        return true;
    }

}
