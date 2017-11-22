package com.example.qlibrary.utils;

import com.example.qlibrary.entity.Result;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by quyang on 2017/5/27.
 */
public class GsonUtil {

  public static <E> Result<E> processJson(String json, Class<E> type) {
    if (StringUtil.isEmty(json)) {
      return null;
    } else {
      JsonObject jsonObj = getJsonObject(json);
      Result result = new Result();
      setRequestInfo(result, jsonObj);
      if (null != jsonObj.get("result")) {
        JsonElement jsonE = jsonObj.get("result");
        Gson gson = new Gson();
        Object obj = gson.fromJson(jsonE, type);
        if (null != obj) {
          result.setResult(obj);
        }
      }

      return result;
    }
  }

  private static void setRequestInfo(Result query, JsonObject jsonObject) {
    if (null != query && null != jsonObject && !"".equals(jsonObject)) {
      if (null != jsonObject.get("msg")) {
        query.setMsg(jsonObject.get("msg").getAsString());
      }

      if (null != jsonObject.get("type")) {
        query.setType(jsonObject.get("type").getAsString());
      }

//            if (null != jsonObject.get("success")) {
//                query.setSuccess(jsonObject.get("success").getAsBoolean());
//            }
    }
  }

  private static JsonObject getJsonObject(String input) {
    JsonParser parser = new JsonParser();
    return parser.parse(input).getAsJsonObject();
  }

}
