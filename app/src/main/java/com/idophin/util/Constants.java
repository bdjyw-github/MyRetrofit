package com.idophin.util;

import android.os.Environment;

import com.idophin.farmingtong.BuildConfig;

public class Constants {

    public static boolean DEBUG = true;

    public static final String URL_V1 = "1.0";

//    public static final String BASE_URL_H5 = "https://www.callhsl.com"; // 生产
//    public static final String URL_COMMUNICATION = "https://api.callhsl.com/api"; // 生产

//    public static final String BASE_URL_H5 = "http://dev.byhsl.tmlsoft.com:82"; // 开发
//    public static final String URL_COMMUNICATION = "http://dev.byhsl.tmlsoft.com:81/api"; // 开发

//    public static final String BASE_URL_H5 = "http://test.byhsl.tmlsoft.com:82"; // 测试
//    public static final String URL_COMMUNICATION = "http://test.byhsl.tmlsoft.com:81/api"; // 测试

    public static String BASE_URL_H5 = BuildConfig.SHARE_URL;
    public static String URL_COMMUNICATION = BuildConfig.API_URL;

    public static final String URL_SHARE_TOPIC_DETAIL=BASE_URL_H5+ "/index.php/App/Popup/index/id/"; //帖子详情分享
    //public static final String URL_SHARE_NEWS_DETAIL= "http://54.222.187.31:801/index.php/App/FlashDetails/index/id/"; //快讯/法律法规详情分享
    public static final String URL_SHARE_NEWS_DETAIL= BASE_URL_H5+"/index.php/App/FlashDetails/index/id/"; //快讯/法律法规详情分享
    //public static final String URL_SHARE_APP="https://api.callhsl.com/download/download.html"; //分享APP
    public static final String URL_SHARE_APP="https://www.callhsl.com/index.php/App/Download/index"; //分享APP
    public static final String URL_USER_GUIDE= BASE_URL_H5+"/index.php/App/MisAutoJuy/index/"; //用户指南
    public static final String URL_USER_PROTOCAL= BASE_URL_H5+"/index.php/App/MisAutoPnv/index/"; //用户协议
    public static final String URL_ABOUT_PEACEMAKER= BASE_URL_H5+"/index.php/App/MisAutoUsq/index/"; //关于巴渝和事佬
    //public static final String URL_FIND_RECEIVER= "http://M.lvjianxia.com/case/release.html"; //找律师
    //public static final String URL_FIND_RECEIVER= "http://m.hourspay.cn/case/release.html"; //找律师
    public static final String URL_FIND_RECEIVER= "http://www.hourspay.cn/case/choose.html"; //找律师

    public static final int PAGE_SIZE = 10;

    public static final String WX_APP_ID = "wx3a07d0e2320c8e02"; //微信
    public static final String KEY = "e1CdOoKJNab3VmIupsY4PjUTFxGqMlyE"; //商户秘钥
    public static final String APPSECRET = "d07d3b98a654726f14286b2e7704e006"; //应用秘钥
    public static final String QQ_APP_ID = "1105417915"; //QQ

    public static final String APPID = "59252052"; //讯飞语音
    public static final String basePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/idophin/";
    public static final String voicePath = basePath + "voice/";
    public static final String cachePath = basePath + "cache/";
    public static final String Service_Key = "3B1AC561-137B-4A37-9BD0-4755F105DD64";
    public static final String Service_PublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAu4VsiKQ9gsQ33ebGIGxh" +
            "dJcHDPXGnoB7FidfXmeIp9qg6j8t3C39b4F1Ki0JDJVKNfcboiPvnyybCLqN6Atp" +
            "188bwv9rO3zoV7GSMHgrr4h0TZ6+h4yIDMHLOq24bUcqvh1MXDxDWKemk5RX0UHh" +
            "Ps3KIsJO+xDNgQprzEGsjAqGGZXjLjTL9CiXUIhcOEHuy1LyGRbZaVW0DovscrgS" +
            "pfGMn00hLzrQzHIxmGRdKyEZZZXqAhM1+OZm1gu2SH1HxKj0/WXHHhRc3UsrB18s" +
            "2z1D6Oyd3zpnLAldOJSZjs1Ur/JRCfcPU4dAtXeNMW4SKwmhJ5dNt99tOkJN2/kW" +
            "gQIDAQAB";

    public static final String ACTION_EXIT = "action_exit";
    public static final String ACTION_RELOGIN = "action_relogin";
    public static final String ACTION_UPDATE_VERSION = "action_update_version";


    //2045436852
    /** 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY */
    public static final String WEIBO_APP_KEY      = "849497964";
    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     *
     * <p>
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    public static final String WEIBO_REDIRECT_URL = "http://www.sina.com";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     *
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     *
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     *
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String WEIBO_SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    public static final int REQUEST_CODE_NAME = 0xff01;
    public static final int REQUEST_CODE_SEX = 0xff02;
    public static final int REQUEST_CODE_ADDRESS = 0xff03;
    public static final int REQUEST_CODE_RECEIVER = 0xff04;
    public static final int REQUEST_CODE_RESPONDENT = 0xff05;
    public static final int REQUEST_CODE_CONTACT = 0xff06;
    public static final int REQUEST_CODE_PHONE = 0xff07;
    public static final int REQUEST_CODE_JOB = 0xff08;
    public static final int REQUEST_CODE_CAPTURE = 0xff09;
    public static final int REQUEST_CODE_COMMITTEE = 0xff10;
    public static final int REQUEST_CODE_STREET = 0xff11;
    public static final int REQUEST_CODE_PRACTICE_NUM = 0xff12;
    public static final int REQUEST_CODE_DISPUTE_SUMMARY = 0xff13;
    public static final int REQUEST_CODE_REGISTER = 0xff14;
    public static final int REQUEST_CODE_CASE_REASON = 0xff15;
    public static final int REQUEST_CODE_RECEIVER_RESELECT = 0xff16;
    public static final int REQUEST_CODE_POSITION = 0xff17;
    public static final int REQUEST_CODE_POSTING = 0xff18;
    public static final int REQUEST_CODE_LOGIN = 0xff19;

    // 网页加载常量
    public static final String EXTRA_WEB_URL = "extra_web_url";
    public static final String EXTRA_WEB_TITLE = "extra_web_title";
    public static final String TEMP_FILE_PAHT = "temp_file_path";
    public static final String TEMP_FILE_DATA = "temp_file_data";

    public static final String EXTRA_MODIFY_NAME = "extra_modify_name";
    public static final String EXTRA_MODIFY_SEX = "extra_modify_sex";
    public static final String EXTRA_MODIFY_ADDRESS = "extra_modify_address";
    public static final String EXTRA_RECEIVER_TYPE = "extra_receiver_type";
    public static final String EXTRA_SELECT_RECEIVER = "extra_select_receiver";
    public static final String EXTRA_SELECT_RESPONDENT = "extra_select_respondent";
    public static final String EXTRA_ACTIVATE_RECEIVER = "extra_activate_receiver";
    public static final String EXTRA_ACTIVATE_RECEIVER_STATUS = "extra_activate_receiver_status";
    public static final String EXTRA_TEXT_MODIFY_TYPE = "extra_text_modify_type";
    public static final String EXTRA_MODIFY_PHONE = "extra_modify_phone";
    public static final String EXTRA_MODIFY_JOB = "extra_modify_job";
    public static final String EXTRA_MODIFY_COMMITTEE = "extra_modify_committee";
    public static final String EXTRA_MODIFY_STREET = "extra_modify_street";
    public static final String EXTRA_CASE_TYPE = "extra_case_type";
    public static final String EXTRA_CASE_ID = "extra_case_id";
    public static final String EXTRA_PIC_PATHES = "extra_pic_pathes";
    public static final String EXTRA_PIC_PATHES_INDEX = "extra_pic_pathes_indexv";
    public static final String EXTRA_CASE_DETAIL = "extra_case_detail";
    public static final String EXTRA_COSHOW_DETAIL = "EXTRA_COSHOW_DETAIL";
    public static final String EXTRA_COSHOW_DETAIL_ID = "extra_coshow_detail_id";
    public static final String EXTRA_ADDRESS_DISTRICT = "extra_address_district";
    public static final String EXTRA_ADDRESS_DISTRICT_ID = "extra_address_district_id";
    public static final String EXTRA_ADDRESS_STREET = "extra_address_street";
    public static final String EXTRA_LAYER_PRACTICE_NUM = "extra_layer_practice_num";
    public static final String EXTRA_RECEIVER_DETAIL_ID = "extra_receiver_detail_id";
    public static final String EXTRA_RECEIVER_PAY_ID = "extra_receiver_pay_id";
    public static final String EXTRA_CASE_OBJECT = "extra_case_object";
    public static final String EXTRA_CASE_MESSAGE = "extra_case_message";
    public static final String EXTRA_LAY_REGULATIONS = "extra_lay_regulations";
    public static final String EXTRA_MEDIATER_APPLY_USER = "extra_mediater_apply_user";
    public static final String EXTRA_CASE_INFO_DETAIL = "EXTRA_CASE_INFO_DETAIL";
    public static final String EXTRA_NEWS_DETAIL_ID = "extra_news_detail_id";
    public static final String EXTRA_NEWS_DETAIL_TYPE = "extra_news_detail_type";
    public static final String EXTRA_MODIFY_VALUE = "extra_modify_value";
    public static final String EXTRA_PICTURES = "extra_pictures";
    public static final String EXTRA_DISPUTE_SUMMARY = "extra_dispute_summary";
    public static final String EXTRA_IS_REGISTER = "extra_is_register";
    public static final String EXTRA_REGISTER_SUCCESS = "extra_register_success";
    public static final String EXTRA_COSHOW_DETAIL_MY = "extra_coshow_detail_my";
    public static final String EXTRA_RECEIVER_DETIAL_SELECT = "extra_receiver_detial_select";
    public static final String EXTRA_CASE_REASON_TYPE = "extra_case_reason_type";
    public static final String EXTRA_CASE_REASON = "extra_case_reason";
    public static final String EXTRA_CASE_REASON_PROTOCAL = "extra_case_reason_protocal";
    public static final String EXTRA_PAY_TYPE = "extra_pay_type";
    public static final String EXTRA_POSITION_PICTURE = "extra_position_picture";
    public static final String EXTRA_REGISTER_TYPE = "extra_register_type";
    public static final String EXTRA_REGISTER_ID = "extra_register_id";
    public static final String EXTRA_POSITION_DETAIL = "extra_position_detail";
    public static final String EXTRA_SPLASH_ADVERT = "extra_splash_advert";
    public static final String EXTRA_HOME_START_WEB_ACTIVITY = "extra_home_start_web_activity";
    public static final String EXTRA_SEARCH_TYPE = "extra_search_type";
    public static final String EXTRA_SEARCH_KEY = "extra_search_key";
    public static final String EXTRA_CASE_COMPLETE_STATE = "extra_case_complete_state";
    public static final String EXTRA_CASE_APPLY_STATE = "extra_case_apply_state";
    public static final String EXTRA_REPORT_REPORTEE = "extra_report_reportee";
    public static final String EXTRA_REPORT_TYPE = "extra_report_type";
    public static final String EXTRA_REPORT_CONTENT = "extra_report_content";
    public static final String EXTRA_REPORT_REPORTEE_NICKNAME = "extra_report_reportee_nickname";
    public static final String EXTRA_REPORT_NEWS = "extra_report_news";
    public static final String EXTRA_REPORT_TOPIC = "extra_report_topic";
    public static final String EXTRA_REPORT_TIME = "extra_report_time";
    public static final String EXTRA_RECEIVER_GOODAT = "extra_receiver_goodat";
    public static final String EXTRA_FILE = "extra_file";

    public static final String EXTRA_VERSION = "extra_version";

    public static final String PREF_TAG_RECEIVERS = "pref_tag_receivers";
    public static final String PREF_LOGININFO = "pref_login_info";
    public static final String PREF_TAG_NOTICE = "pref_tag_notice";
    public static final String PREF_TAG_VERSION = "pref_tag_version";
    public static final String PREF_TAG_HISTORY = "pref_tag_history";
    public static final String PREF_TAG_SHOW_USERINFO = "pref_tag_show_userinfo_";
    public static final String PREF_TAG_SHOW_BOOTPAGE = "pref_tag_show_bootpage";

    public static final String FROM_ACTIVITY = "from_activity";
    public static final String PREF_GESTURE = "pref_gesture_";
    public static final String PREF_GESTURE_PWD = "pref_gesture_pwd_";
    public static final String PREF_GESTURE_USERNAME = "pref_gesture_username";

    // */ 手势密码点的状态
    public static final int POINT_STATE_NORMAL = 0; // 正常状态
    public static final int POINT_STATE_SELECTED = 1; // 按下状态
    public static final int POINT_STATE_WRONG = 2; // 错误状态

    public static final String EXTRA_RESET_GESTURE = "extra_reset_gesture";
    public static final String EXTRA_FORGET_GESTURE = "extra_forget_gesture";



}
