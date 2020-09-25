package com.ssvet.approval.utils.exception;

/**
 * @author Programmer_Liu.
 * @since 2020/8/25 10:13
 */
public class ApprovalException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private Integer code;
    private Integer httpCode;


    public ApprovalException(Integer httpCode, Integer code, String msg) {
        super(msg);
        this.httpCode = httpCode;
        this.code = code;
    }

    public ApprovalException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.httpCode = 200;
    }
}
