package com.arman.queuetube.config;

public class Constants {

    public interface Action {

        public static final String MAIN_ACTION = "com.arman.queuetube.action.main";
        public static final String PLAY_ACTION = "com.arman.queuetube.action.play";
        public static final String PAUSE_ACTION = "com.arman.queuetube.action.pause";
        public static final String NEXT_ACTION = "com.arman.queuetube.action.next";
        public static final String STOP_ACTION = "com.arman.queuetube.action.stop";

    }

    public interface NotificationId {

        public static final int FOREGROUND_SERVICE = 101;

    }

    public interface Key {

        public static final String API_KEY = "AIzaSyAf95ydwL8dJb-yUmvFKAC9AzDiwbzlrVU";
        public static final String TEST_AD_KEY = "ca-app-pub-3940256099942544/6300978111";
        public static final String AD_KEY = "ca-app-pub-5272639228074525~9828413436";

    }

}
