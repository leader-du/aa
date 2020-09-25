package com.ssvet.approval.controller;


import com.ssvet.approval.entity.Event;
import com.ssvet.approval.entity.Record;
import com.ssvet.approval.service.IRecordService;
import com.ssvet.approval.utils.resp.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 审批记录表 前端控制器
 * </p>
 *
 * @author 刘志红
 * @since 2020-08-25
 */
@RestController
@RequestMapping("/approval/record")
public class RecordController {

    @Autowired
    private IRecordService recordService;

    @RequestMapping("/getRecords")
    public CommonResult getRecords(@RequestBody Event event){

        return recordService.getRecord(event);

    }

    @RequestMapping("/addRecord")
    public CommonResult addRecord(@RequestBody Record record){

        return recordService.addRecord(record);

    }

}
