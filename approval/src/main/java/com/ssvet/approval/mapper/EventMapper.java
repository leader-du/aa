package com.ssvet.approval.mapper;

import com.ssvet.approval.entity.Event;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 审批事件表 Mapper 接口
 * </p>
 *
 * @author 刘志红
 * @since 2020-08-25
 */
public interface EventMapper extends BaseMapper<Event> {

    List<Event> getCompletedApprovalList(Integer days);

}
