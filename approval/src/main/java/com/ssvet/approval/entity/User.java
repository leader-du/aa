package com.ssvet.approval.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import com.ssvet.approval.entity.Role;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Programmer_Liu.
 * @since 2020/8/29 9:13
 */
@Data
public class User implements UserDetails {

    @TableId(value="uid",type=IdType.AUTO)
    private Integer id;
    @TableField("uname")
    private String uName;
    private String password;
    private String chName;
    private Integer deptId;
    private Integer status;
    @TableField(exist = false)
    private List<Role> roles;
    @Version
    private Integer version;
    // 选择第几个审批人
    @TableField(exist = false)
    private Integer selectCount;
    //上一个审批人ID
    @TableField(exist = false)
    private Integer preApprovalId;

    @TableField(exist = false)
    private Dept dept;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(roles == null || roles.size()<=0){
            return null;
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return authorities;
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return uName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
