package com.ssvet.approval.mapper;

import com.ssvet.approval.entity.Role;
import com.ssvet.approval.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author 刘志红
 * @since 2020-08-25
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 查出对应角色级别的所有用户
     * @param roleGrade 用户级别
     * @param chName 模糊查询用户名
     * @return
     */
    List<User> selectListByRoleGrade(@Param("currentUserId") Integer currentUserId, @Param("chName") String chName,@Param("roleGrade") Integer roleGrade);

    /**
     * 获取用户的所有角色
     * @param user
     * @return
     */
    List<Role> getRoles(User user);

    /**
     * 查询出比当前角色级别更高的用户列表
     * @param userMaxRoleGrade 角色级别
     * @param chName 模糊查询用户姓名
     * @return
     */
    List<User> selectLeaderList(@Param("chName") String chName,@Param("userMaxRoleGrade") Integer userMaxRoleGrade,@Param("prevUserRoleGrade") Integer prevUserRoleGrade);

    List<User> getUserByChName(String chName);

    List<Role> getRolesByUid(Integer preApprovalId);

    /**
     * 通过区域总经理ID查询对应基地总经理信息
     * @param uid
     * @return
     */
    List<User> getBaseManager(Integer uid);

    /**
     * 通过总经理ID查出副总信息
     * @param uid
     * @return
     */
    List<User> getFuManager(Integer uid);
}
