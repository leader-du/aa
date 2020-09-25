package com.ssvet.approval.controller;


import com.ssvet.approval.entity.Event;
import com.ssvet.approval.entity.User;
import com.ssvet.approval.service.IEventService;
import com.ssvet.approval.service.INoteService;
import com.ssvet.approval.utils.resp.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartRequest;

import java.util.List;

/**
 * <p>
 * 审批事件表 前端控制器
 * </p>
 *
 * @author 刘志红
 * @since 2020-08-25
 */
@Slf4j
@RestController
@RequestMapping("/approval/event")
public class EventController {
    @Autowired
    private IEventService eventService;
    @Autowired
    private INoteService noteService;

    @RequestMapping("/getUserApprovalList")
    public CommonResult getApprovalList(@RequestBody User user) {

        return eventService.getApprovalList(user);

    }

    @RequestMapping("/getEventList")
    public CommonResult getApprovalList(@RequestBody Event e) {


        return eventService.getApprovalList(e.getApprovalUserId());
    }

    /**
     * 发起审批
     *
     * @param e
     * @return
     */
    @RequestMapping("/addApproval")
    public CommonResult addApproval(@RequestBody Event e) {


        return eventService.addApproval(e);
    }

    /**
     * 修改审批
     *
     * @param e
     * @return
     */
    @RequestMapping("/updateApproval")
    public CommonResult updateApproval(@RequestBody Event e) {
        return eventService.updateApproval(e);
    }

    /**
     * 提交审批
     * @param event  审批信息
     * @param request 审批上传的图片
     * @return
     */
    @RequestMapping("/saveApproval")
    public CommonResult saveApproval(@RequestBody Event event, MultipartRequest request) {
        log.info("event = " + event);

        return eventService.saveApproval(event,request);

    }

    @RequestMapping("/getCompletedApprovalList")
    public List<Event> getCompletedApprovalList(@RequestBody Event event) {

        return eventService.getCompletedApprovalList(event.getDays());

    }
}
