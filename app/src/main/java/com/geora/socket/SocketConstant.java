package com.geora.socket;

public class SocketConstant {
    public interface URL {
        //String SERVER_URL = "ws://appinventive.com:8234/beacon/connect/";
        //String SERVER_URL = "ws://appinventive.com.:8235/beacon/connect/";
        //String SERVER_URL = "ws://appinventive.com:8236/beacon/connect/";
//        String SERVER_URL = "ws://georaapp.com:8234/beacon/connect/";
        String SERVER_URL = "ws://socketpy.georaapp.com:8234/beacon/connect/";
    }
    public interface BECOMEBUSINESSUSERURL {
//        String BUSNIESS_URL = "http://georadevweb.appinventive.com/beacon-map";
        //String BUSNIESS_URL = "http://georastgweb.appinventive.com/beacon-map";
//        String BUSNIESS_URL = "https://www.georaapp.com/beacon-map";
        //String BUSNIESS_URL = "http://georaqaweb.appinventive.com/beacon-map";
        String BUSNIESS_URL = "https://admin.georaapp.com/beacon-map";
    }

    public interface SOCKEYKEYS {
        String ACTION_TYPE = "action_type";
        String IS_SUBSCRIBED_USER = "is_subscribed_user";
        String USER_ID = "user_id";
        String SAVED_ID = "saved_id";
        String BEACON_ID = "beacon_id";
        String FILTER = "filter";
        String ACTION = "action";
        String CAMP_ID = "camp_id";
        String NOTIFICATION = "notification";
        String SEND_NOTIFICATION = "send_notification";
        String SAVED = "saved";
        String DELETE_ITEM = "delete_item";
        String SEARCH = "search";
        String DIRECT_RECALL = "direct_recall";
        String INIT = "init";
        String CAMP_TYPE = "camp_type";
        String DETAIL = "detail";
        String DATA = "data";
        String LAT = "lat";
        String LNG = "lng";
        String MESSAGE = "message";
    }

}
