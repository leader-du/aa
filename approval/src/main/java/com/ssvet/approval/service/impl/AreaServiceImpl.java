package com.ssvet.approval.service.impl;

import com.ssvet.approval.entity.Area;
import com.ssvet.approval.mapper.AreaMapper;
import com.ssvet.approval.service.IAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AreaServiceImpl implements IAreaService {

    @Autowired
    private AreaMapper areaMapper;

    @Override
    public Area getAreaByUid(Integer uid) {
        return areaMapper.getAreaByUid(uid);
    }
}
