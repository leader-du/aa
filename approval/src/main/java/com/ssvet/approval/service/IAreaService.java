package com.ssvet.approval.service;

import com.ssvet.approval.entity.Area;

public interface IAreaService {

    /**
     * 通过总经理ID查出基地归属区域
     * @param uid
     * @return
     */
    Area getAreaByUid(Integer uid);
}
