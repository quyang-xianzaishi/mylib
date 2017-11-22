package com.example.administrator.lubanone;

/**
 * Created by Administrator on 2017/6/21.
 */

public interface Urls {


  //关于我们
  String us_spread_upload = "api.php/Home/News/uploadnews";//推广上传图片

  /**
   * 首页数据
   */
  String TEST_URL = "http://www.baidu.com";

  //保存姓名
  String SAVW_NAME_URL = "http://www.baidu.com";

  //根域名
  String ROOT_URL = "http://103.210.239.20/";
//  String ROOT_URL = "http://192.168.3.215/";
//  String ROOT_URL = "http://42.51.40.5/";

  //注册
  String REGISTER = ROOT_URL + "api.php/Home/Reg/regadd";

  //登陆
  String LAND = ROOT_URL + "api.php/Home/Login/login";

  //找回密码
  String FIND_PWD = ROOT_URL + "api.php/Home/Login/checkinfo";

  //激活账户 one
  String ACTIVE_ACCOUNT_ONE = ROOT_URL + "api.php/Home/reg/checkstep1";

  //设置支付密码
  String SET_PAY_PWD = ROOT_URL + "api.php/Home/SetInfo/setPaypwd";

  //设置真实姓名
  String SET_REAL_NAME = ROOT_URL + "api.php/Home/SetInfo/setTrueName";

  //设置银行账户
  String SET_BANK_ACCOUNT = ROOT_URL + "api.php/Home/SetInfo/setBankAcc";

  //注册获取验证码
  String SEND_SMS = ROOT_URL + "api.php/Home/reg/sendtosms";

  //校验用户号码是否被激活
  String CHECK_ACTIVE = ROOT_URL + "api.php/Home/reg/checkstep1";

  //注册获取国家类型
  String SELECT_COUNTRY = ROOT_URL + "";

  //获取银行列表
  String BANK_LIST = ROOT_URL + "api.php/Home/SetInfo/banklist";

  //spalsh轮播图
  String SPLASH_VIEW_PAGER = ROOT_URL + "api.php/Home/Reg/flash";


  //----------------------用户基本信息 下-----------------------------
  //账户设置
  String ACCOUNT_SET = ROOT_URL + "api.php/Home/SetInfo/accountSet";

  //设置昵称
  String NICK_NAME_SET = ROOT_URL + "api.php/Home/SetInfo/setNick";

  //设置sex
  String SEX_SET = ROOT_URL + "api.php/Home/SetInfo/setSex";

  //地区选择
  String AREA_LIST = ROOT_URL + "api.php/Home/SetInfo/countrylist";

  //地区设置
  String AREA_SET = ROOT_URL + "api.php/Home/SetInfo/setcountry";

  //设置用户签名
  String ASSIGN_SET = ROOT_URL + "api.php/Home/SetInfo/setAutograph";

  //添加银行卡
  String ADD_CARD = ROOT_URL + "api.php/Home/SetInfo/setBankCard";

  //根据token获取sms
  String SMS_BY_TOKNE = ROOT_URL + "api.php/Home/SetInfo/sendphonecode";

  //通过手机号码获取验证码
  String SMS_BY_PHONE = ROOT_URL + "api.php/Home/reg/sendtosms";

  //修改登陆密码
  String UPDATE_LAND_PWD = ROOT_URL + "api.php/Home/SetInfo/changePwd";

  //修改支付密码
  String UPDATE_PAY_PWD = ROOT_URL + "api.php/Home/SetInfo/changeSecpwd";

  //上传头像设置头像
  String UPLOAD_ICON = ROOT_URL + "api.php/Home/SetInfo/setImg";

  //----------------------用户基本信息 上-----------------------------

  //首页
  String HOME_FRAMENT = ROOT_URL + "/api.php/Home/index/home";

  //刷新首页列表
  String HOME_FRAMENT_LIST_VIEW = ROOT_URL + "api.php/Home/index/homerefresh";

  //---------------------首页----------------------------------

  //---------------------财务中心-------------------------

  //总览
  String ALL_SCAN = ROOT_URL + "api.php/Home/Index/financeCenter";

  //梦想背包
  String DREAM_PACKAGE = ROOT_URL + "api.php/Home/Index/dreambagDetail";

  //奖金背包
  String MONEY_PACKAGE = ROOT_URL + "api.php/Home/Index/rewardbagDetail";

  //激活码
  String ACTIVE_CODE = ROOT_URL + "api.php/Home/Index/activecodeDetail";

  //催化剂
  String CUIHUAJI = ROOT_URL + "api.php/Home/Index/catabagDetail";

  //---------------------培训-------------------------
  String MY_TEAM = ROOT_URL + "api.php/Home/Myteam/index";

  //买入种子 首页
  String BUY_SEEDS = ROOT_URL + "api.php/Home/index/buyseed";

  //计算梦想种子的金额
  String SHOULD_PAY = ROOT_URL + "api.php/Home/index/computeprice";

  //买入种子提交
  String BUY_SEEDS_COMMIT = ROOT_URL + "api.php/Home/index/buyseeddeal";

  //卖出种子
  String SELL_SEEDS_COMMIT = ROOT_URL + "api.php/Home/index/sellseed";

  //卖出种子 提交
  String SELL_SEEDS_COMMIT_INFO = ROOT_URL + "api.php/Home/index/sellseeddeal";

  //买入 带匹配
  String BUY_MATCH = ROOT_URL + "api.php/Home/index/buymatchlist";

  //买入 代付款
  String BUY_NO_MONEY = ROOT_URL + "api.php/Home/index/buypaylist";

  //会员信息
  String VIP_INFO = ROOT_URL + "";

  //买入待确认
  String BUY_CONFIRM = ROOT_URL + "api.php/Home/index/buyconfirmlist";

  //催收
  String CUI_SHOU = ROOT_URL + "api.php/Home/index/buypress";

  //上传付款凭证
  String UPLOAD_PAY_PIC = ROOT_URL + "api.php/Home/index/uploadpayproof";

  //买入 评价
//  String BUY_PINGJIA = "http://192.168.3.215/" + "api.php/Home/index/buyevaluate";
  String BUY_PINGJIA = ROOT_URL + "api.php/Home/index/buyevaluate";

  //买入待评论
  String BUY_PINGJIA_LIST = ROOT_URL + "api.php/Home/index/buyevaluatelist";

  //卖出 待评价
  String SELL_PINGJIA = ROOT_URL + "api.php/Home/index/sellevaluatelist";

  //卖家 评价
  String SELL_PINGJIA_INTO = ROOT_URL + "api.php/Home/index/sellevaluate";

  //卖出待确认
  String SELL_CONFRIM = ROOT_URL + "api.php/Home/index/sellconfirmlist";

  //卖出待确认 确认收款和投诉
  String SELL_COMFIRM_MONEY = ROOT_URL + "api.php/Home/index/confirmbutton";

  //卖出待付款
  String SELL_NO_MONEY = ROOT_URL + "api.php/Home/index/sellpaylist";

  //卖出待付款 催一崔
  String SELL_CUI = ROOT_URL + "api.php/Home/index/sellpress";

  //卖出带匹配
  String SELL_MATCH = ROOT_URL + "api.php/Home/index/sellmatchlist";

  //上传视屏
  String UPLOAD_VIDEO = ROOT_URL + "api.php/Home/index/uploadproofvideo";

  //------------------------------个人中心----------------------------

  //个人中心
  String USER_CENTER = ROOT_URL + "api.php/Home/index/homecenter";

  //银行卡或账号列表
  String CARD_LIST = ROOT_URL + "api.php/Home/SetInfo/bankaccountlist";

  //收割梦想种子
  String REAP_SEEDS = ROOT_URL + "api.php/Home/index/reapseed";

  //转码
  String TRANSFER_CODE = ROOT_URL + "api.php/Home/index/activecodeTransfer";

  //卖家投诉
  String COMPLAINT = ROOT_URL + "api.php/Home/index/complaint";

  //卖家同意延长打款时间
  String AGREE_LONGER_PAY_TIME = ROOT_URL + "api.php/Home/index/sellprolong";

  //买入种子记录
  String BUY_SEEDS_RECORDS = ROOT_URL + "api.php/Home/index/buyseedrecord";

  //卖出种子记录
  String SELL_SEEDS_RECORDS = ROOT_URL + "api.php/Home/index/sellseedrecord";

  //申请延长打款时间
  String LONG_PAY_TIME = ROOT_URL + "api.php/Home/index/buyapplyprolong";

  //激活账号
  String ACTIVATE_ACCOUNT = ROOT_URL + "api.php/Home/index/jhuser";

  //修改登录密码  完成
  String COMPLETE_UPDATE_PWD = ROOT_URL + "";

  //修改密码 回传手机号码
  String SEND_PHONE = ROOT_URL + "api.php/Home/Login/pwdgetCode";

  //验证手机验证码
  String CHECK_PHONE_CODE = ROOT_URL + "api.php/Home/Login/pwdcheckCode";

  //设置密码
  String RESET_PWD = ROOT_URL + "api.php/Home/Login/pwdreset";

  //会员级别
  String VIP_LEVEL = ROOT_URL + "api.php/Home/index/memberlevel";

  //常见问题
  String ALWAYS_PROBLES = ROOT_URL + "api.php/Home/index/commonquestion";

  //信用评分记录
  String CREDIT_RECORD = ROOT_URL + "api.php/Home/index/creditCodeRecord";

  //梦想基金 新闻列表 http://42.51.40.5/api.php/home/news/newsDetail/token/c58f6e951cc6f7794973de8fc50df954/if_id/111
  String DREAM_FOUND_NEWS = ROOT_URL + "api.php/home/news/newsList";

  //新闻详情
  String DREAM_FOUND_NEWS_DETAILS = ROOT_URL + "api.php/home/news/newsDetail";

  //app地址
  String APP_URL = ROOT_URL + "api.php/Home/reg/getversion";

  //买家崔确认
  String CUI_COMFRIM = ROOT_URL + "/api.php/Home/index/buypress";

  //信用评分制度
  String CREDIT_SYSTEM = ROOT_URL + "api.php/Home/index/creditpolicy";

  //------------------消息模块-------------------
  //好友申请
  String APPLY_FRIEND = ROOT_URL + "api.php/Home/Rong/joinfriend";

  //获取用户信息
  String GET_USER_INFO = ROOT_URL + "api.php/Home/Rong/getuserinfo";

  //同意申请加入好友
  String AGREE_APPLY_FRIEND = ROOT_URL + "api.php/Home/Rong/agreejoinfriend";

  //删除好友
  String Delete_FRIEND = ROOT_URL + "api.php/Home/Rong/deletefriends";

  //搜索好友
  String SEARCH_FRIEND = ROOT_URL + "api.php/Home/Rong/searchfriends";

  /**
   * 消息订单信息
   */
  String MSG_ORDER_LIST = ROOT_URL + "api.php/Home/Rong/ordermessage";

  //获取系统消息
  String SYSTEM_GET = ROOT_URL + "api.php/Home/Rong/systemmessage";

  //团队消息
  String TEAM_MSG = ROOT_URL + "api.php/home/news/newsDetail";

  //获取用户的激活码个数
  String GET_ACTIVE_CODE = ROOT_URL + "api.php/Home/index/getactivecode";

  //获取会员信息
  String GET_MEMBER_INFO = ROOT_URL + "api.php/Home/Rong/getsearchuserinfo";

  //获取好友申请列表
  String GET_FRIEND_APPLY_LIST = ROOT_URL + "api.php/Home/Rong/joinfriendlist";

  //获取通讯录列表
  String GET_ADDRESS_LIST = ROOT_URL + "api.php/Home/Rong/listfriends";

  //删除好友
  String DELETE_FRIEND = ROOT_URL + "api.php/Home/Rong/deletefriends";

  //获取融云token
  String GET_RONG_TOKEN = ROOT_URL + "api.php/Home/Rong/gettoken";

  //获取好友资料设置信息
  String GET_SET_INFO = ROOT_URL + "api.php/Home/Rong/setfriend";

  //设置星标朋友
  String SET_STAR_STATE = ROOT_URL + "api.php/Home/Rong/setstar";

  //加入黑名单
  String JOIN_BLACK_LIST = ROOT_URL + "api.php/Home/Rong/setblack";

  //创建群组
  String CREATE_GROUP = ROOT_URL + "api.php/Home/Rong/creategroup";

  //加入群组
  String ADD_GROUP = ROOT_URL + "api.php/Home/Rong/addgroup";

  //获取群组信息
  String GET_GROUP_INFO = ROOT_URL + "api.php/Home/Rong/getgroupinfo";

  //获取群成员列表
  String GET_GROUP_MEMBER = ROOT_URL + "api.php/Home/Rong/groupfriendlist";

  //群主删除群成员
  String DELETE_GROUP_MEMBER = ROOT_URL + "api.php/Home/Rong/delgroupfriend";

  //获取群组设置信息
  String GET_GROUP_SET_INFO = ROOT_URL + "api.php/Home/Rong/setgroup";

  //设置群聊昵称
  String SET_GROUP_NAME = ROOT_URL + "api.php/Home/Rong/setgroupname";

  //设置群公告
  String SET_GROUP_NOTICE = ROOT_URL + "api.php/Home/Rong/setgroupnotice";

  //解散群组
  String DISSOLVE_GROUP = ROOT_URL + "api.php/Home/Rong/reliefgroup";

  //退出群组
  String EXIT_GROUP = ROOT_URL + "api.php/Home/Rong/exitgroup";

  //获取群组人数
  String GET_GROUP_COUNT = ROOT_URL + "api.php/Home/Rong/groupcount";

  //群组保存到通讯录
  String GROUP_SAVE_ADDRESS = ROOT_URL + "api.php/Home/Rong/setgroupmaillist";

  //获取群组列表
  String GET_GROUP_LIST = ROOT_URL + "api.php/Home/Rong/grouplist";

  //获取投诉原因列表
  String GET_COMPLAIN_LIST = ROOT_URL + "api.php/Home/Rong/complainttypelist";

  //投诉
  String COMPLAIN = ROOT_URL + "api.php/Home/Rong/complaint";

  //推荐好友
  String RECOMMEND_FRIEND = ROOT_URL + "api.php/Home/Rong/recommendfriend";

  //推荐好友列表
  String RECOMMEND_FRIEND_LIST = ROOT_URL + "api.php/Home/Rong/recommendmessage";

  //培训消息列表
  String GET_TRAIN_MESSAGE_LIST = ROOT_URL + "api.php/Home/Rong/friend_messagetrain";

  //积分记录 
  String CREDIT_RECORD_LIST = ROOT_URL + "api.php/Home/index/traincode";

  //上传视频
  String upload_video = ROOT_URL + "api.php/Home/index/uploadproofvideo";

  //新首页 买入待匹配
  String buy_no_match = ROOT_URL + "api.php/Home/index/buymatchtab";

  //新首页 买入待付款
  String buy_no_pay = ROOT_URL + "api.php/Home/index/buypaytab";

  //新首页 买入待确认
  String buy_no_confirm = ROOT_URL + "api.php/Home/index/buyconfirmtab";

  //新首页 买入待评价
  String buy_no_pingjia = ROOT_URL + "api.php/Home/index/buyevaluatetab";

  //新首页 卖出待匹配
  String sell_no_match = ROOT_URL + "api.php/Home/index/sellmatchtab";

  //新首页 卖出待付款
  String sell_no_pay = ROOT_URL + "api.php/Home/index/sellpaytab";

  //新首页 卖出待确认
  String sell_no_confirm = ROOT_URL + "api.php/Home/index/sellconfirmtab";

  //新首页 卖出待评价
  String sell_no_pingjia = ROOT_URL + "api.php/Home/index/sellevaluatetab";

  //成长中的种子列表
  String GROWING_SEEDS_LSIT = ROOT_URL + "api.php/Home/index/getgrowseeds";

  //获取消息未读数量
  String GET_MESSAGE_UNREAD_COUNT = ROOT_URL + "api.php/Home/Rong/lookunreadinformation";

  //卖出种子会员信息
  String SELL_MEMBER_INFO = ROOT_URL + "api.php/Home/index/sellmemberInfo";

  //买入种子会员信息
  String BUY_MEMBER_INFO = ROOT_URL + "api.php/Home/index/buymemberInfo";

  //删除银行卡
  String DELETE_CARD = ROOT_URL + "api.php/Home/SetInfo/removebankaccount";

  //获取折线图
  String GET_CHAR = ROOT_URL + "api.php/Home/index/getchart";

  //会员级别制度
  String MEMBER_LEVEL_SYSTEM = ROOT_URL + "api.php/home/taskdetail/questionuri";

  //删除支付宝账户
  String DELETE_ALI_ACCOUNT = ROOT_URL + "api.php/home/setInfo/delqrcode";

  //上传二维码
  String UPLOAD_ERWEIMA = ROOT_URL + "api.php/home/setInfo/uploadqrcode";
}

