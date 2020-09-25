package com.ssvet.approval.controller;


import com.ssvet.approval.entity.User;
import com.ssvet.approval.mapper.UserMapper;
import com.ssvet.approval.service.IUserService;
import com.ssvet.approval.utils.resp.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author 刘志红
 * @since 2020-08-25
 */
@RestController
@RequestMapping("/approval/user")
public class UserController {
    @Autowired
    private IUserService userService;

    /**
     * 更新当前用户密码
     * @param user 新密码
     * @return
     */
    @PostMapping("/updatePassword")
    public CommonResult updatePassword(@RequestBody User user) {
        return userService.updatePassword(user);
    }

    /**
     * 获取审批人
     * @param user 上一个审批人信息
     */
    @RequestMapping("/getApprovalList")
    public CommonResult getApprovalList(@RequestBody User user){
        return userService.getApprovalList(user);
    }

    @RequestMapping("/getBaseManager")
    public List<User> getBaseManager(@RequestBody User user) {
        return userService.getBaseManager(user.getId());
    }

    @RequestMapping("/getFuManager")
    public User getFuManager(@RequestBody User user) {
        return userService.getFuManager(user.getId());
    }

    @RequestMapping("/getUserByUid")
    public User getUserByUid(@RequestBody User user) {
        return userService.getUserByUid(user.getId());
    }
}
