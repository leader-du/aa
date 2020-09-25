package com.ssvet.approval.service;

import com.ssvet.approval.entity.Event;
import com.ssvet.approval.entity.Record;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ssvet.approval.utils.resp.CommonResult;

/**
 * <p>
 * 审批记录表 服务类
 * </p>
 *
 * @author 刘志红
 * @since 2020-08-25
 */
public interface IRecordService{

    CommonResult addRecord(Record record);

    CommonResult getRecord(Event event);

}
