package com.ssvet.approval.service.impl;

import com.ssvet.approval.entity.Event;
import com.ssvet.approval.entity.Note;
import com.ssvet.approval.mapper.NoteMapper;
import com.ssvet.approval.service.INoteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ssvet.approval.utils.exception.ApprovalException;
import com.ssvet.approval.utils.resp.CommonResult;
import com.ssvet.approval.utils.upload.GetPath;
import com.ssvet.approval.utils.upload.UploadFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 审批事件票据表 服务实现类
 * </p>
 *
 * @author 刘志红
 * @since 2020-08-25
 */
@Service
@Slf4j
public class NoteServiceImpl implements INoteService {
//    @Value("${upload.path}")
//    private String filePath;
    @Autowired
    private NoteMapper noteMapper;

    @Override
    @Transactional
    public CommonResult uploadImg(MultipartFile file) {
        if (StringUtils.isEmpty(file) || StringUtils.isEmpty(file.getOriginalFilename())) {
            log.error("上传的文件传输有误");
            return CommonResult.validateFailed("服务器异常，请稍后再试或者联系管理员");
        }
        //白名单验证，即验证上传文件的后缀
        String originalFilename = file.getOriginalFilename();
        //后缀格式白名单
        String reg = "^(jpg|gif|ico|jpeg|tif|png|jfif)$";
        if (!UploadFileUtils.getSuffix(originalFilename).matches(reg)) {
            log.info("上传文件名:" + originalFilename);
            return CommonResult.failed("请传入正确的文件格式");
        }

        // 设置图片上传路径,没有目录就创建

        String uploadPath = GetPath.getUploadPath("static/upload");

        String name = System.currentTimeMillis() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

        File uploadFile = new File(uploadPath + File.separator + name);

        try {

            file.transferTo(uploadFile);
            String fileUrl = "upload/"+name;
            Note note = new Note();
            note.setApprovalNoteUrl(fileUrl);
            return CommonResult.success(note, "上传成功");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return CommonResult.failed("文件上传失败");

    }

    @Override
    public CommonResult delImg(Note note) {

        noteMapper.deleteById(note.getApprovalNoteId());

        return CommonResult.success("删除成功");
    }
}
