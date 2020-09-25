package com.ssvet.approval.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ssvet.approval.entity.Area;

public interface AreaMapper extends BaseMapper<Area> {

    /**
     * 通过总经理ID查出基地归属区域
     * @param uid
     * @return
     */
    Area getAreaByUid(Integer uid);

}
