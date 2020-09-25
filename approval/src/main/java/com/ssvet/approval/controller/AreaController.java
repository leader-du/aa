package com.ssvet.approval.controller;

import com.ssvet.approval.entity.Area;
import com.ssvet.approval.entity.User;
import com.ssvet.approval.service.IAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/approval/area")
public class AreaController {

    @Autowired
    private IAreaService areaService;

    @RequestMapping("/getAreaByUid")
    public Area getArea(@RequestBody User user){

        return areaService.getAreaByUid(user.getId());
    }

}
