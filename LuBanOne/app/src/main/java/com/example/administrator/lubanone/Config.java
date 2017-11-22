package com.example.administrator.lubanone;

/**
 * Created by Administrator on 2017/6/20.
 */

public interface Config {

  public static final String APP_NAME = "clown.apk";

  public static final int APP_CACHE_SIZE = 15 * 1024 * 1024;

  public static boolean isOnline = true;

  public static boolean isOffLine = false;

  //购买种子界面向财务中心userdreamactivity跳转时的type
  String BUY_SEEDS_TYPE = "buy_seed";


  String USER_DREAM_PACKAGE_KEY = "user_dream";

  //总览
  int ALL_SCAN = 0x0000;

  //梦想背包
  int DREAM_PACKAGE = 0x0001;

  //奖金背包
  int MONEY_PACKAGE = 0x0002;

  //激活码
  int ACTIVE_CODE = 0x0003;

  //催化剂
  int CUIHUAJI = 0x0004;

  int BIANJI = 0x00e4;


  String BUY_PROCESS_KEY = "buy_process";

  //homefragment 买入流程的带匹配
  int buy_process_match_from_home_fragment = 0x0005;

  int buy_process_money_from_home_fragment = 0x0006;

  int buy_process_comfrim_from_home_fragment = 0x0007;

  int buy_process_pingjia_from_home_fragment = 0x0008;


  String SELL_PROCESS_KEY = "sell_process";

  //homefragment 买入流程的带匹配
  int SELL_PROCESS_MATCH_FROM_HOME_FRAGMENT = 0x0009;

  int SELL_PROCESS_MONEY_FROM_HOME_FRAGMENT = 0x000a;

  int SELL_PROCESS_COMFRIM_FROM_HOME_FRAGMENT = 0x000b;

  int SELL_PROCESS_PINGJIA_FROM_HOME_FRAGMENT = 0x000c;


  //培训积分
  int TRAIN_CREDIT_FROM_USER_CENTER = 0x000d;

  String TRAIN_CREDIT_KEY = "train_credit";

  String MAIN_PAIN__KEY = "main_page";

  String TRAIN_CREDIT_EXAM_KEY = "train_credit_exam";

  String TRAIN_CREDIT_TRAIN_KEY = "train_credit_train";

  String TRAIN_CREDIT_UPLOAD_KEY = "train_credit_upload_ziliao";

  String I_AGREE = "agree";

  String UPLOAD_SPREAD_INFO = "upload_spread_info";

  int REGISTER_PROTOCAL = 0x0010;

  String SP_FLE_NAME = "xiaochou_file";

  String USER_INFO = "user_info";

  boolean isTest = false;

  String REGISTER_REAL_NAME = "register_real_name";

  String USER_NAME = "real_name";

  String TOKEN = "token";

  String RONG_TOKEN = "rongtoken";

  String USER_ID = "userid";

  String NICK_NAME = "nick_name";

  String USER_SEX = "user_sex";

  String AREA = "area";

  String USER_AREA = "user_area";

  String USER_ASSIGN = "user_assign";

  String USER_NICK = "user_nick";

  String REAL_NAME = "real_name";

  //String TEST_TOKEN = "cc5ffda3d25f0df8aaa732109db9edc7";
  //String TEST_TOKEN = "f3c93c58f0c8553b4051d72e55c49853";
  String TEST_TOKEN = "f3c93c58f0c8553b4051d72e55c49851";

  String OLD_PWD = "old_pwd";

  String USER_PHONE = "user_phone";

  String USER_LEVEL = "user_level";

  String USER_AREA_REAL = "user_area_real";

  String HEAD_ICON_PATH = "head_icon_path";

  boolean isSetName = false;

  String UPDATE_LAND_PWD = "update_land_pwd";

  String default_value = "";

  String IS_LANDED = "is_land";

  String USER_PWD = "user_pwd";

  String HOME_FRAGMENT_DREAM_PACKAGE = "home_fragment_dream_package";

  String HOME_MONEY_DREAM_PACKAGE = "home_money_dream_package";

  String HOME_ACTIVE_CODE = "home_active_code";

  String head_image = "head_image";

  String USER_ACCOUNT = "user_account";

  String URL_NEW_APP = "clown";

  String LAND_NAME = "land_name";

  boolean test_memery = false;

  //修改密码的短信读秒
  int UPDATE_LAND_PWD_LIMIT_SECONDS = 60;

  //卖家同时卖家延长打款时间后的延长时间，在原基础上
  String long_time = "12";
}
