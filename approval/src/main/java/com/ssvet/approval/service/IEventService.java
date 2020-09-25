package com.ssvet.approval.service;

import com.ssvet.approval.entity.Event;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ssvet.approval.entity.User;
import com.ssvet.approval.utils.resp.CommonResult;
import org.springframework.web.multipart.MultipartRequest;

import java.util.List;

/**
 * <p>
 * 审批事件表 服务类
 * </p>
 *
 * @author 刘志红
 * @since 2020-08-25
 */
public interface IEventService{

    /**
     * 查询当前用户是否有相应审批
     * @param user
     * @return
     */

    public CommonResult getApprovalList(User user);

    /**
     * 获取用户所有审批
     * @param id
     * @return
     */

    CommonResult getApprovalList(Integer id);

    /**
     * 添加审批
     * @param e
     * @return
     */
    CommonResult addApproval(Event e);

    /**
     * 修改审批
     * @param e
     * @return
     */
    CommonResult updateApproval(Event e);

    /**
     * 提交审批
     * @param event
     * @param request 审批中上传的图片
     * @return
     */
    CommonResult saveApproval(Event event, MultipartRequest request);

    /**
     * 查询出所有规定天数内已完成的审批
     * @param days
     * @return
     */
    List<Event> getCompletedApprovalList(Integer days);
}
