/*
 * Copyright (C) 20015 MaiNaEr All rights reserved
 */
package com.example.administrator.lubanone.chunkupload.utils;

import com.example.administrator.lubanone.BuildConfig;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 类/接口描述
 *
 * @author wangjian
 * @date 2016/3/30.
 */
public class LoggerUtil {

    static Logger mLogger = Logger.getLogger("okhttp");

    public static void i(String msg) {
        if(BuildConfig.DEBUG){
            mLogger.log(Level.INFO, msg);
        }


    }

    public static void w(String msg) {
        if(BuildConfig.DEBUG){
            mLogger.log(Level.WARNING, msg);
        }
    }

}
