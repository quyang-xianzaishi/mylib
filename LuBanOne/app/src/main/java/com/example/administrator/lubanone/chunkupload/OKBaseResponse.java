
/*
 *
 *  Copyright 2014-2016 wjokhttp.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.example.administrator.lubanone.chunkupload;

/**
 * 接口回调基本相应类型需要实现此接口
 *
 * @author wangjian
 * @date 2016/3/23.
 */
public interface OKBaseResponse {
    /**
     * 得到要解析的数据，参考demo BaseResponse
     *
     * @return
     */
    String getData();
}