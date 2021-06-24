package com.geora.constants;

public class AppConstants {

    public static final String USER_DETAILS = "user_details";
    public static final String TOKEN = "user_id";
    public static final int TAKE_PICTURE = 201;
    public static final int REQUEST_PERMISSION_GALLEY = 202;
    public static final int REQUEST_PERMISSION_CAMERA = 203;
    public static final int REQUEST_DETAIL_SCREEN = 2001;
    public static final String NOTIFICATION_TYPE = "notification_type";



    public static class UIVALIDATIONS {
        public static final int EMAIL_EMPTY = 101;
        public static final int PASSWORD_EMPTY = 102;
        public static final int INVALID_PASSWORD = 104;
        public static final int INVALID_EMAIL = 103;
        public static final int NAME_EMPTY = 105;
        public static final int NEW_PASSWORD_EMPTY = 106;
        public static final int CONFIRM_PASSWORD_EMPTY = 107;
        public static final int PASSWORD_NOT_MATCHED = 108;
        public static final int INVALID_NAME = 109;
        public static final int PHONE_EMPTY = 110;
        public static final int INVALID_PHONE = 111;
        public static final int OLD_PASSWORD_EMPTY = 112;
        public static final int DOB = 113;
        public static final int OTP = 114;
        public static final int OTP_LENGTH = 115;

        public static final int OTP_EMPTY = 113;
        public static final int INVALID_OTP = 114;
        public static final int CODE_EMPTY = 115;
        public static final int DOB_EMPTY = 117;
        public static final int NO_INTERNET = 116;
        public static final int INVALID_CVV = 119;
        public static final int INVALID_CARD = 200;

        public static final int FLATNO = 201;
        public static final int STATE = 202;
        public static final int CITY = 203;
        public static final int ADDRESSTYPE = 204;
    }

    public static class PreferenceConstants {

        public static final String REFRESH_TOKEN = "refresh_token";
        public static final String ACCESS_TOKEN = "access_token";
        public static final String TOKEN = "token";
        public static final String USER_NAME = "user_name";
        public static final String USER_DETAILS = "user_detail";
        public static final String DEVICE_TOKEN = "device_token";
    }

    public static class CountryConstants {
        public static final String COUNRTY_CODE = "CountryCode";
        public static final String COUNRTY_ID = "CountryID";
        public static final String COUNRTY_ENGLISH_NAME = "CountryEnglishName";

    }

    public static class BundleConstants {
        public static final String SCREEN = "screen";
        public static final String LAUNCHSCREEN = "launchScreen";
        public static final String COUNTRY_DATA = "countryData";
        public static final String RIDE_STARTED = "rideStarted";

        public static final String SCOOTERDATA = "scooterData";
        public static final String RIDEDATA = "rideData";
    }

    public static class HomeListViewConstants{
        public static final int ORDERS_FRAGMENT=1;
        public static final int FUNDRAISED_FRAGMENT=2;
        public static final int FORMS_FRAGMENT=3;

        public static final String TITLE="Title";
        public static final String FORM_DETAILS="Details";
        public static final String ORDER_DETAILS="Order Details";
        public static final String FUND_DETAILS="Promotion Details";
        public static final String ID="id";
    }
}
