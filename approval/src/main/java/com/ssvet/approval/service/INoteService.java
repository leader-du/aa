package com.ssvet.approval.service;

import com.ssvet.approval.entity.Event;
import com.ssvet.approval.entity.Note;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ssvet.approval.utils.resp.CommonResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 审批事件票据表 服务类
 * </p>
 *
 * @author 刘志红
 * @since 2020-08-25
 */
public interface INoteService {
    /**
     * 审批上传图片
     *
     * @param file
     * @return
     */
    CommonResult uploadImg(MultipartFile file);

    /**
     * 删除指定图片
     *
     * @param note
     * @return
     */
    public CommonResult delImg(@RequestBody Note note);
}