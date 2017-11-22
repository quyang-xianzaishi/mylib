package com.example.administrator.lubanone.rxjava;

/**
 * Created by hou
 */

public class HttpcodeException extends RuntimeException {

    public static final int TYPE_0 = 0;
    public static final int TYPE_3 = 3;

    public HttpcodeException(int resultCode, String errorMsg) {
        this(getExceptionMessage(resultCode, errorMsg));
    }

    public HttpcodeException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 转换错误数据
     *
     * @param code
     * @return
     */
    private static String getExceptionMessage(int code, String errorMsg) {
        String message = "";
        switch (code) {
            case TYPE_3:
                message = "3";
                break;
            default:
                message = errorMsg;
                break;
        }
        return message;
    }
}

