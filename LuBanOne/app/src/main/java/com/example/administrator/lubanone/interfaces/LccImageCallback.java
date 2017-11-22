package com.example.administrator.lubanone.interfaces;

import java.io.File;

/**
 * Created by hou on 2017/7/5.
 */

public interface LccImageCallback {

    /**
     * Fired when the compression is started, override to handle in your own code
     */
    void onStart();

    /**
     * Fired when a compression returns successfully, override to handle in your own code
     */

    void onFileSuccess(File file);

    /**
     * Fired when a compression fails to complete, override to handle in your own code
     */
    void onError(Throwable e);

}
