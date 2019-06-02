package com.tuochebang.service.constant;

import java.io.File;

public class AppConfig {
    public static final String APP_NAME = "TouchebangService";
    public static final String CACHE_IMAGE_FOLDER = (AppConstant.CACHE_ROOT_PATH + File.separator + "Image");
    public static final boolean DEBUG_MODE = true;
    public static final int DEFAULT_PAGE_COUNT = 10;
    public static final int DEFAULT_START_PAGE = 1;
    public static final String DEVICE_TYPE = "android";
    public static final String IMG_URL_HEAD = "";
    public static final int MIN_PAGE_ITEM_SIZE = 10;
    public static final String USER_PROFILE = "user_profile";
}
