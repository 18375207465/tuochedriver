package com.tuochebang.service.constant;

import android.os.Environment;
import java.io.File;

public class AppConstant {
    public static final String ALIPAY_CHANNEL = "alipay";
    public static final String ALIYUN_OSS_BUCKET = "tuochebang";
    public static final String ALIYUN_OSS_KEY = "tuoche/";
    public static int APP_DEFAULT_START_COUNT = 2;
    public static final String APP_PHONE = "400-1000-0000";
    public static final String APP_START_COUNT = "app_start_count";
    public static int AppRole_HoneyCircle = 2;
    public static final String CACHE_ROOT_PATH = (Environment.getExternalStorageDirectory().getPath() + File.separator + AppConfig.APP_NAME);
    public static final String DEFAULT_CITY = "贵阳";
    public static final float DEFAULT_PIXEL = 1242.0f;
    public static final String DEFAULT_UUID = "00000000-0000-0000-0000-000000000000";
    public static String FALG_ACTIVITY_DETAIL = "cache_activity_detail";
    public static final String FALG_ACTIVITY_INFO = "cache_activity";
    public static final String FALG_CURRENT_ACTIVITY_INFO = "cache_current_activity_info";
    public static final String FALG_LOGIN_INFO = "cache_login_info";
    public static final String FALG_USER_PROFILE = "user_profile";
    public static final String FALG_VERSION_INFO = "cache_version_info";
    public static final String FLAG_USER_INFO = "cache_user_info";
    public static final int MESSAGE_SCOPE_IMAGE_TEXT = 6;
    public static final int MESSAGE_SCOPE_TEXT = 7;
    public static final int POST_TYPE_DEFAULT = 0;
    public static final int POST_TYPE_POI = 1;
    public static final int POST_TYPE_TAG = 0;
    public static final int SHARE_CATEGORY_ACTIVITY = 1;
    public static final int SHARE_CATEGORY_SERVICE = 3;
    public static final int SHARE_CATEGORY_STORY = 2;
    public static final String SINA_APP_KEY = "2132631553";
    public static final String SINA_APP_SERCRT = "ff9b57db55c738db81262f0f2ed757e5";
    public static final String UPLOAD_URL_ENROLL_TYPE = "enroll";
    public static final String UPLOAD_URL_HEADER_TYPE = "avatar";
    public static final String UPLOAD_URL_SCHEME_TYPE = "scheme";
    public static final String WECHAT_APP_ID = "wxdd27e57ca55e6d97";
    public static final String WECHAT_APP_SERCRT = "cca8a5c15d5b32c4ecab58d85b9cb044";

    public static final class BroadCastAction {
        public static final String AUTH_CHANGE = "auth_change";
        public static final String DRIVER_CHANGE = "driver_change";
        public static final String MESSAGE_CHANGE = "message_change";
        public static final String RECEIVE_NEW_MESSAGE = "receiver_new_message";
        public static final String REQUEST_ERROR = "request_error";
        public static final String RETURN_CHANGE = "return_change";
        public static final String ROB_ORDER_SUCCESS = "rob_order_success";
        public static final String TOKE_EXPIRE = "token_expire";
        public static final String USER_INFO_CHANGE = "user_info_change";
        public static final String USER_LOGIN_SUCCESS = "user_login_success";
        public static final String USER_LOGOUT_SUCCESS = "user_logout_success";
        public static final String USER_REGISTER_SUCCESS = "user_register_success";
        public static final String VALIDATA_TOKE = "validata_token";
    }
}
