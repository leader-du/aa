package com.ssvet.approval.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 审批事件票据表
 * </p>
 *
 * @author 刘志红
 * @since 2020-08-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("approval_note")
public class Note implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 票据id
     */
    @TableId(value = "approval_note_id", type = IdType.AUTO)
    private Integer approvalNoteId;

    /**
     * 票据URL
     */
    private String approvalNoteUrl;

    /**
     * 关联审批事件ID
     */
    private Integer noteEventId;


}
