package com.geora.data.constants;

public class AppConstantClass {
    public static final int PAYMENT_REQUEST = 1001;

    public static final String SCREEN = "screen";
    public static final String VERIFIED = "verified";
    public static final String EDITPROFILEDATA = "editprofiledata";
    public static final String ENABLE = "enable";
    public static final String DISABLE = "disable";
    public static final String CHANGE = "change";
    public static final String RESEND = "resend";
    public static final String PRODUCTDATA = "productdata";
    public static final String DATA = "data";
    public static final String CHARGEDATA = "changeData";
    public static final String FROM = "from";
    public static final String POSITION = "positions";
    public static final String TITLE = "title";
    public static final String MESSAGE = "message";
    public static final String RATING = "rating";
    public static final String URL = "url";

    public interface STATIC_PAGES {
        String ABOUT_US = "aboutus";
        String TERMSANDCONDITION = "termsandcondition";
        String PRIVACYPOLICY = "privacypolicy";
        String PAGE = "page";
    }

    public interface CATEGORY {
        String EVENT = "Events";
        String SALES = "Sales";
        String FUNDRAISING = "FundRaisings";
        String PROMOTION = "Promotions";
        String ALLACTIVE = "All";
    }

    public interface CAMPTYPE {
        int EVENT = 4;
        int SALES = 3;
        int FUNDRAISING = 2;
        int PROMOTION = 1;
    }

    public interface PROMOTION_TYPE {
        String FOLLOW_US  = "Follow Us";
        String OPEN_LINK  = "Open Link";
        String CONTACT  = "Contact";
        String APP_STORE  = "App Store";
        String DOWNLOAD  = "Download";

    }


    public static class APIConstant {
        public static final String METHOD = "method";
        public static final String NAME = "name";
        public static final String FUND_ID = "fund_id";
        public static final String BUSNISS_USER_IS = "business_user_id";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
        public static final String PHONE = "phone";
        public static final String DOB = "dob";
        public static final String AMOUNT = "amount";
        public static final String SALEID = "sale_id";
        public static final String COLORID = "color_id";
        public static final String DEVICETYPE = "device_type";
        public static final String DEVICETOKEN = "device_token";
        public static final String COUNTRYCODE = "country_code";
        public static final String TYPE = "type";
        public static final String IMAGE = "image";
        public static final String TOKEN = "token";
        public static final String CLIENTOTP = "client_otp";
        public static final String CLIENT_ID = "client_id";
        public static final String CLIENTSECRET = "client_secret";
        public static final String USERNAME = "username";
        public static final String USERID = "userId";
        public static final String ISSOCIALLOGIN = "isSocialLogin";
        public static final String GRANTTYPE = "grant_type";
        public static final String GOOGLEID = "google_id";
        public static final String SOCIALID = "social_id";
        public static final String FACEBOOKID = "facebook_id";
        public static final String ISPHONEVERIFIED = "is_phone_verified";
        public static final String ISEMAILVARIFIED = "is_email_varified";
        public static final String ISADDRESSEDITTED = "is_address_edited";
        public static final String ADDRESS = "address";
        public static final String FLATNO = "flat_no";
        public static final String CITY = "city";
        public static final String STATE = "state";
        public static final String STREETNAME = "stree_name";
        public static final String FORMATEDADDRESS = "formattedAddress";
        public static final String LAT = "lat";
        public static final String LNG = "lng";
        public static final String PINCODE = "pin_code";
        public static final String ADDRESSTYPE = "address_type";
        public static final String ACTION = "action";
        public static final String FULLNAME = "full_name";
        public static final String MOBILENO = "mobile_no";
        public static final String SAVE = "save";
        public static final String HOME = "hone";
        public static final String SALEACTIVE = "sales_active";
        public static final String ALLACTIVE = "all_active";
        public static final String PROMOTIONSACTIVE = "promotions_active";
        public static final String FUNDRAISINGACTIVE = "fundraising_active";
        public static final String EVENTACTIVE = "event_active";
        public static final String CARDID = "cardID";
        public static final String ISNUMBEREDITED = "is_number_edited";
        public static final String CLIENTID = "clientID";
        public static final String OLDPASSWORD = "old_password";
        public static final String NEWPASSWORD = "new_password";
        public static final String BEACON_ID = "beacon_id";

        public static final String PROXIMITYRANGE = "proximity_range";
        public static final String NOTIFICATIONFLAG = "notification_flag";
        public static final String ADDRESSID = "addressID";
        public static final String ADDRESS_ID = "address_id";
        public static final String QTY = "qty";
        public static final String ACTIONTYPE = "action_type";
        public static final String SIZEID = "size_id";
        public static final String EVENTID = "event_id";
        public static final String ISBUSINESSUSER = "is_business_user";
        public static final String CAMP_ID = "camp_id";
    }

    public static class stripeKeys {
        public static final String STRIPE_KEY = "pk_live_sc7Ly0AQxy3DaynTZF8Zb2B1";
        public static final String STRIPE_SECRET = "sk_live_RebKwWsN9Rn2QdyVehXArlMT";
//        public static final String STRIPE_KEY = "pk_test_AuSENQSgSGZBIb3IVlwXNCE5";
//        public static final String STRIPE_SECRET = "sk_test_2nqw3U4bJw5AgQRd3oCAjrBh";
    }
}
