package com.ssvet.approval.controller;


import com.ssvet.approval.entity.Event;
import com.ssvet.approval.entity.Note;
import com.ssvet.approval.service.INoteService;
import com.ssvet.approval.utils.resp.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 审批事件票据表 前端控制器
 * </p>
 *
 * @author 刘志红
 * @since 2020-08-25
 */
@RestController
@RequestMapping("/approval/note")
public class NoteController {
    @Autowired
    private INoteService noteService;
    /**
     * 上传图片
     * @return
     */
    @RequestMapping("/uploadImg")
    public CommonResult uploadImg(MultipartFile file){
        return noteService.uploadImg(file);
    }

    @RequestMapping("/delImg")
    public CommonResult delImg(@RequestBody Note note){

        return noteService.delImg(note);

    }
}
