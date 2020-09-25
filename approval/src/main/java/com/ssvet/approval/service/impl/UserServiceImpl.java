package com.ssvet.approval.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ssvet.approval.entity.Role;
import com.ssvet.approval.entity.User;
import com.ssvet.approval.mapper.DeptMapper;
import com.ssvet.approval.mapper.RoleMapper;
import com.ssvet.approval.mapper.UserMapper;
import com.ssvet.approval.service.IUserService;
import com.ssvet.approval.utils.resp.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;


/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 刘志红
 * @since 2020-08-25
 */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private DeptMapper deptMapper;

    @Override
    public CommonResult updatePassword(User user) {
        if (StringUtils.isEmpty(user.getPassword())) {
            return CommonResult.failed("新密码不能为空");
        }
        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (StringUtils.isEmpty(userDetails) || StringUtils.isEmpty(userDetails.getId())) {
            log.error("认证信息的用户信息为空");
            return CommonResult.validateFailed("服务器异常，请重新登陆再试或者联系管理员");
        }
        User userOld = userMapper.selectById(userDetails.getId());
        if (passwordEncoder.matches(user.getPassword(), userOld.getPassword())) {
            return CommonResult.failed("新旧密码不能相同");
        }
        user.setId(userDetails.getId());
        user.setVersion(userOld.getVersion());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.updateById(user);
        return CommonResult.success("密码修改成功");
    }

    @Override
    public CommonResult getApprovalList(User user) {
        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (StringUtils.isEmpty(userDetails) || StringUtils.isEmpty(userDetails.getId())) {
            log.error("认证信息的用户信息为空");
            return CommonResult.validateFailed("服务器异常，请重新登陆再试或者联系管理员");
        }
        //审批流程必然为先出纳级别为1，然后会计级别为2，然后其他
        List<User> users = null;

        //如果审批人为空，说明选择第一个审批人
        if (user.getSelectCount() == 1) {
            //第一个审批必然是出纳，即角色级别为1
            //查出所有角色级别为1的所有用户，但不包括自己
            Integer roleGrade = 1;

            Integer currentUserId = userDetails.getId();

            String chName = user.getChName();

            //用户名模糊查询
            users = userMapper.selectListByRoleGrade(currentUserId,chName,roleGrade);

        } else if (user.getSelectCount() == 2) {

            //说明第一个审批人是出纳，那么第二个审批人必然是会计
            //查询出所有会计,会计的角色级别为2，不包括自己
            users = userMapper.selectListByRoleGrade(userDetails.getId(), user.getChName(), 2);

        }else{

            // 获取当前用户的最大权限等级

            Integer userMaxRoleGrade = getUserMaxRoleGrade(userDetails);

            //获取上一个审批人roleGrade,并找出最大权限等级

            List<Role> roles = userMapper.getRolesByUid(user.getPreApprovalId());

            Integer prevUserRoleGrade = getUserMaxRoleGrade(roles);


            //查询当前登陆人最高级别
            if (userMaxRoleGrade == -1) {
                return CommonResult.failed("上一个审批人还未分配角色，请联系管理员");
            } else {
                //审批人的用户级别比当前用户级别要高  还要高于上一个审批人
                users = userMapper.selectLeaderList(user.getChName(),userMaxRoleGrade, prevUserRoleGrade);
            }

            if (user == null) {

                return CommonResult.validateFailed("服务器异常，请稍后再试或者联系管理员");
            }

        }

        // 为审批人添加部门信息

        for (User u : users) {

            u.setDept(deptMapper.selectById(u.getDeptId()));

        }

        return CommonResult.success(users);

    }

    /**
     * 根据区域经理ID查出所负责所有基地的总经理信息
     * @param uid
     * @return
     */

    @Override
    public List<User> getBaseManager(Integer uid) {

        List<User> list = userMapper.getBaseManager(uid);

        for (User user : list) {

            user.setDept(deptMapper.selectById(user.getDeptId()));

        }

        return list;
    }

    @Override
    public User getFuManager(Integer uid) {

        List<User> list = userMapper.getFuManager(uid);

        if(list.size() > 0){

            User user = list.get(0);

            user.setDept(deptMapper.selectById(user.getDeptId()));

            return user;
        }

        return null;


    }

    @Override
    public User getUserByUid(Integer uid) {

        User user = userMapper.selectById(uid);

        user.setDept(deptMapper.selectById(user.getDeptId()));

        return user ;
    }

//    @Override
//    public CommonResult getApprovalList(User user) {
//        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        if (StringUtils.isEmpty(userDetails) || StringUtils.isEmpty(userDetails.getId())) {
//            log.error("认证信息的用户信息为空");
//            return CommonResult.validateFailed("服务器异常，请重新登陆再试或者联系管理员");
//        }
//        //审批流程必然为先出纳级别为1，然后会计级别为2，然后其他
//        List<User> users = null;
//
//        System.out.println(StringUtils.isEmpty(user));
//        //如果审批人为空，说明选择第一个审批人
//        if (StringUtils.isEmpty(user) || StringUtils.isEmpty(user.getId())) {
//            //第一个审批必然是出纳，即角色级别为1
//            //查出所有角色级别为1的所有用户，但不包括自己
//            Integer roleGrade = 1;
//            //用户名模糊查询
//            users = userMapper.selectListByRoleGrade(userDetails.getId(), user.getChName(), roleGrade);
//        } else {
//            Integer userMaxRoleGrade = getUserMaxRoleGrade(user);
//            if (userMaxRoleGrade == 1) {
//                //说明第一个审批人是出纳，那么第二个审批人必然是会计
//                //查询出所有会计,会计的角色级别为2，不包括自己
//                users = userMapper.selectListByRoleGrade(userDetails.getId(), user.getChName(), 2);
//            } else {
//                Integer currentUserMaxRoleGrade = getUserMaxRoleGrade(userDetails);
//                //查询当前登陆人最高级别
//                if (userMaxRoleGrade == -1) {
//                    return CommonResult.failed("上一个审批人还未分配角色，请联系管理员");
//                } else {
//                    //审批人的用户级别比当前用户级别要高
//                    users = userMapper.selectLeaderList(user.getChName(), userMaxRoleGrade);
//                }
//            }
//        }
//        if (user == null) {
//            return CommonResult.validateFailed("服务器异常，请稍后再试或者联系管理员");
//        }
//        return CommonResult.success(users);
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("uname", username));
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        List<Role> userAllRoles = roleMapper.getUserAllRoles(user.getId());
        user.setRoles(userAllRoles);
        return user;
    }

    /**
     * 获取员工的最大角色级别
     *
     * @param user
     * @return
     */
    private Integer getUserMaxRoleGrade(User user) {
        List<Role> roles = userMapper.getRoles(user);
        if (roles == null || roles.size() <= 0) {
            return -1;
        }
        Integer roleGrade = roles.stream().max(Comparator.comparing(Role::getRoleGrade)).get().getRoleGrade();
        return roleGrade;
    }

    private Integer getUserMaxRoleGrade(List<Role> roles){

        Integer userMaxRoleGrade = 0;

        for (Role role : roles) {

            if(role.getRoleGrade() > userMaxRoleGrade){

                userMaxRoleGrade = role.getRoleGrade();

            }

        }

        return userMaxRoleGrade;

    }
}
