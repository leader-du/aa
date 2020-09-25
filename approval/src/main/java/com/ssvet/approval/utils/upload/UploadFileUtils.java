package com.ssvet.approval.utils.upload;

import com.ssvet.approval.entity.Note;
import com.ssvet.approval.utils.exception.ApprovalException;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadFileUtils {
    /**
     *
     * @param file 上传到文件，必须为非空
     * @param filePath 上传文件的地址
     * @param newFileName 上传文件的新文件名
     * @return 上传后的文件url
     */
    public static String uploadFile(MultipartFile file, String filePath, String newFileName) {
        String filename = file.getOriginalFilename();
        // 新图片名称
        if (StringUtils.isEmpty(newFileName)) {
            //生成文件名
            newFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + filename.substring(filename.lastIndexOf("."));
        }
        //文件URL
        String fileUrl = filePath+"/"+ newFileName;
        File folder = new File(filePath);
        //文件夹路径不存在
        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
        }
        // 新图片
        File newFile = new File(fileUrl);
        try {
            file.transferTo(newFile);
        } catch (IllegalStateException | IOException e) {
            throw new ApprovalException(401, e.getMessage());
        }
        return fileUrl;
    }



    /**
     * 获取文件后缀
     *
     * @param filename
     * @return
     */
    public static String getSuffix(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);//文件后缀;
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static void deleteFile(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return;
        }
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            if (!file.delete()) {
                throw new ApprovalException(11000, "文件删除失败");
            }
        } else {
            return;
        }
    }


    /**
     * @return
     * @Description: 根据图片地址转换为base64编码字符串
     * @Author:
     * @CreateTime:
     */
    public static String getImageStr(String imgFile) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }
}
