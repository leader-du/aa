package com.ssvet.approval.mapper;

import com.ssvet.approval.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author 刘志红
 * @since 2020-08-25
 */
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 通过用户id获取所有的角色
     * @param userId 通过用户id获取List
     * @return
     */
    List<Role> getUserAllRoles(Integer userId);
}
