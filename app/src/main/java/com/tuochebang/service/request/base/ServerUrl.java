package com.tuochebang.service.request.base;

public class ServerUrl {
    public static final String IMAGE = "image";
    public static final String MESSAGE = "message";
    private static final String REQUEST = "request";
    private static final String RETURNS = "returns";
    private static final String SETTING = "setting";
    private static String URL_PATH = "/tuochebang/rest/business/v1.3.0/";
    private static String URL_RELASE = "http://api.tuocb.com";
    private static String URL_TEST = "http://115.28.244.177:8188";
    public static String URL_UPLOAD = "http://tuochebang.oss-cn-qingdao.aliyuncs.com";
    public static final String USER = "user";
    private static ServerUrl mServerUrl;

    public static ServerUrl getInst() {
        if (mServerUrl == null) {
            mServerUrl = new ServerUrl();
        }
        return mServerUrl;
    }

    public static String getBaseUrlPath() {
        return URL_RELASE + URL_PATH;
    }

    public String getTcbDomain(String modulekey) {
        String rootUrl = getBaseUrlPath();
        if (modulekey.equals(USER)) {
            return rootUrl + "adminuser/";
        }
        if (modulekey.equals(IMAGE)) {
            return rootUrl + "image/";
        }
        if (modulekey.equals(MESSAGE)) {
            return rootUrl + "message/";
        }
        if (modulekey.equals(REQUEST)) {
            return rootUrl + "requestorder/";
        }
        if (modulekey.equals(RETURNS)) {
            return rootUrl + "tuochereturn/";
        }
        if (modulekey.equals(SETTING)) {
            return rootUrl + "setting/";
        }
        return rootUrl;
    }

    public String USER_LOGIN_URL() {
        return getTcbDomain(USER) + "login";
    }

    public String USER_REGISTER_URL() {
        return getTcbDomain(USER) + "driverregister";
    }

    public String CHECK_ACCOUNT_EXIST() {
        return getTcbDomain(USER) + "checkUserName";
    }

    public String ADMIN_REGISTER_URL() {
        return getTcbDomain(USER) + "masterregister";
    }

    public String UPDATE_USER_INFO_URL() {
        return getTcbDomain(USER) + "update/token";
    }

    public String USER_INFO_URL() {
        return getTcbDomain(USER) + "info/token";
    }

    public String DELETE_DRIVER_URL() {
        return getTcbDomain(USER) + "deletedriver/token";
    }

    public String FEEDBACK_URL() {
        return getTcbDomain(SETTING) + "submit";
    }

    public String UPLOAD_IMAGE() {
        return getTcbDomain(IMAGE) + "upload";
    }

    public String GET_PAY_METHOD() {
        return getTcbDomain("") + "pay/type";
    }

    public String GET_CAR_TYPE_METHOD() {
        return getTcbDomain("") + "car/type";
    }

    public String GET_REQUEST_TYPE() {
        return getTcbDomain("") + "tuochereturn/trailer/type";
    }

    public String ROB_ORDER_REQUEST() {
        return getTcbDomain("") + "requestorder/rob/token";
    }

    public String PUBLISH_RETURN_REQUEST() {
        return getTcbDomain("") + "tuochereturn/submit/token";
    }

    public String EDIT_RETURN_REQUEST() {
        return getTcbDomain("") + "tuochereturn/edit/token";
    }

    public String SUBMIT_TUOCHE_REQUEST() {
        return getTcbDomain(REQUEST) + "submit/token";
    }

    public String EDIT_TUOCHE_REQUEST() {
        return getTcbDomain(REQUEST) + "edit/token";
    }

    public String GET_TUOCHE_REQUEST() {
        return getTcbDomain(REQUEST) + "query/token";
    }

    public String GET_TUOCHE_RETURNS() {
        return getTcbDomain(RETURNS) + "query/token";
    }

    public String DELETE_TUOCHE_RETURNS() {
        return getTcbDomain(RETURNS) + "delete/token";
    }

    public String GET_TUOCHE_RETURNS_DETAIL() {
        return getTcbDomain(RETURNS) + "queryInfo/token";
    }

    public String GET_TUOCHE_REQUEST_DETAIL() {
        return getTcbDomain("") + "requestorder/queryInfo/token";
    }

    public String GET_WATING_TUOCHE_REQUST() {
        return getTcbDomain("") + "requestorder/requestlist/token";
    }

    public String GET_MANAGER_TUOCHE_REQUST() {
        return getTcbDomain("") + "requestorder/driver/token";
    }

    public String GET_USER_AUTH_REQUST() {
        return getTcbDomain("") + "auth/query/token";
    }

    public String GET_AUTH_DRIVER_REQUST() {
        return getTcbDomain("") + "auth/sure/token";
    }

    public String GET_TUOCHE_MESSAGE() {
        return getTcbDomain(MESSAGE) + "query/token";
    }

    public String GET_TUOCHE_DETAIL_MESSAGE() {
        return getTcbDomain(MESSAGE) + "queryinfo/token";
    }

    public String PERSON_REGISTER_URL() {
        return getTcbDomain("") + "adminuser/userregister";
    }
}
