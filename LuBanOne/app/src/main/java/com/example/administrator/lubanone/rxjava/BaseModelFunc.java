package com.example.administrator.lubanone.rxjava;

import com.example.administrator.lubanone.bean.model.BaseModel;
import com.example.administrator.lubanone.utils.HouLog;
import rx.functions.Func1;


/**
 * Created by yuanchong on 17/3/7.
 * 先进行一次解析,判断code是否为1
 *
 * type 为 0，throw new HttpcodeException(baseModel.getType(), baseModel.getMsg());在error中获取
 */

public class BaseModelFunc<T> implements Func1<BaseModel<T>, T> {

  //  http://192.168.3.215/api.php/Home/News/newsDetail/token/5e5c2702c58f8f25eaf78197da8f5dc7/if_id/26
  @Override
  public T call(BaseModel<T> baseModel) {

    HouLog.d("<<<>>>><<><>",
        "type=======" + baseModel.getType() + "  msg: " + baseModel.getMsg() + "  result: " + baseModel
            .getResult());
    HouLog.d("<<<>>>><<><>", baseModel.toString());

    if (baseModel.getType() == 0) {

      throw new HttpcodeException(baseModel.getType(), baseModel.getMsg());

    } else if (baseModel.getType() == 1) {

      return baseModel.getResult();

    } else if (baseModel.getType() == 3) {
      throw new HttpcodeException(baseModel.getType(), baseModel.getMsg());
    }

    return null;
  }
}
