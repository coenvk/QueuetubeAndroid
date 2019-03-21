package com.arman.queuetube.config;

public class Constants {

    public interface Action {

        public static final String MAIN_ACTION = "com.arman.queuetube.action.main";
        public static final String PLAY_ACTION = "com.arman.queuetube.action.play";
        public static final String PAUSE_ACTION = "com.arman.queuetube.action.pause";
        public static final String NEXT_ACTION = "com.arman.queuetube.action.next";
        public static final String STOP_ACTION = "com.arman.queuetube.action.stop";

    }

    public interface Notification {

        public static final int ID = 666;
        public static final String CHANNEL_NAME = "Queuetube notifications";
        public static final String CHANNEL_ID = "49210233949201482942101";

    }

    public interface YouTube {

        public interface Topics {

            public static final String MUSIC = "/m/04rlf";
            public static final String GAMING = "/m/0bzvm2";
            public static final String SPORTS = "/m/06ntj";
            public static final String ENTERTAINMENT = "/m/02jjt";
            public static final String LIFESTYLE = "/m/019_rr";
            public static final String SOCIETY = "/m/098wr";

            public static final String KNOWLEDGE = "/m/01k8wb";

            public interface Music {

                public static final String CHRISTIAN = "/m/02mscn";
                public static final String CLASSICAL = "/m/0ggq0m";
                public static final String COUNTRY = "/m/01lyv";
                public static final String ELECTRONIC = "/m/02lkt";
                public static final String HIP_HOP = "/m/0glt670";
                public static final String INDEPENDENT = "/m/05rwpb";
                public static final String JAZZ = "/m/03_d0";
                public static final String ASIAN = "/m/028sqc";
                public static final String LATIN_AMERICAN = "/m/0g293";
                public static final String POP = "/m/064t9";
                public static final String REGGAE = "/m/06cqb";
                public static final String RHYTHM_AND_BLUES = "/m/06j6l";
                public static final String ROCK = "/m/06by7";
                public static final String SOUL = "/m/0gywn";

            }

            public interface Gaming {

                public static final String ACTION = "/m/025zzc";
                public static final String ACTION_ADVENTURE = "/m/02ntfj";
                public static final String CASUAL = "/m/0b1vjn";
                public static final String MUSIC = "/m/02hygl";
                public static final String PUZZLE = "/m/04q1x3q";
                public static final String RACING = "/m/01sjng";
                public static final String ROLE_PLAYING = "/m/0403l3g";
                public static final String SIMULATION = "/m/021bp2";
                public static final String SPORTS = "/m/022dc6";
                public static final String STRATEGY = "/m/03hf_rm";

            }

            public interface Sports {

                public static final String AMERICAN_FOOTBALL = "/m/0jm_";
                public static final String BASEBALL = "/m/018jz";
                public static final String BASKETBALL = "/m/018w8";
                public static final String BOXING = "/m/01cgz";
                public static final String CRICKET = "/m/09xp_";
                public static final String FOOTBALL = "/m/02vx4";
                public static final String GOLF = "/m/037hz";
                public static final String ICE_HOCKEY = "/m/03tmr";
                public static final String MIXEL_MARTIAL_ARTS = "/m/01h7lh";
                public static final String MOTORSPORT = "/m/0410tth";
                public static final String TENNIS = "/m/07bs0";
                public static final String VOLLEYBALL = "/m/07_53";

            }

            public interface Entertainment {

                public static final String HUMOR = "/m/09kqc";
                public static final String MOVIES = "/m/02vxn";
                public static final String PERFORMING_ARTS = "/m/05qjc";
                public static final String PROFESSIONAL_WRESTLING = "/m/066wd";
                public static final String TV_SHOWS = "/m/0f2f9";

            }

            public interface LifeStyle {

                public static final String FASHION = "/m/032tl";
                public static final String FITNESS = "/m/027x7n";
                public static final String FOOD = "/m/02wbm";
                public static final String HOBBY = "/m/03glg";
                public static final String PETS = "/m/068hy";
                public static final String BEAUTY = "/m/041xxh";
                public static final String TECHNOLOGY = "/m/07c1v";
                public static final String TOURISM = "/m/07bxq";
                public static final String VEHICLES = "/m/07yv9";

            }

            public interface Society {

                public static final String BUSINESS = "/m/09s1f";
                public static final String HEALTH = "/m/0kt51";
                public static final String MILITARY = "/m/01h6rj";
                public static final String POLITICS = "/m/05qt0";
                public static final String RELIGION = "/m/06bvp";

            }

        }

    }

    public interface Key {

        public static final String API_KEY = "AIzaSyAf95ydwL8dJb-yUmvFKAC9AzDiwbzlrVU";
        public static final String TEST_AD_KEY = "ca-app-pub-3940256099942544/6300978111";
        public static final String AD_KEY = "ca-app-pub-5272639228074525~9828413436";

    }

    public interface Fragment {

        public static final int MAIN = 0;
        public static final int STREAM = 1;
        public static final int FAVORITES = 2;
        public static final int HISTORY = 3;
        public static final int PLAYLISTS = 4;

        public interface Argument {

            public static final String PLAYLIST_NAME = "playlistName";
            public static final String IS_CLICKABLE = "isClickable";
            public static final String IS_SORTABLE = "isSortable";

        }

    }

    public interface VideoData {

        public static final String TITLE = "title";
        public static final String ID = "id";
        public static final String CHANNEL = "channel";
        public static final String DESCRIPTION = "description";
        public static final String PUBLISHED_ON = "publishedOn";
        public static final String THUMBNAIL_URL = "thumbnailUrl";
        public static final String VIEWS = "views";
        public static final String LIKES = "likes";
        public static final String DISLIKES = "dislikes";

    }

    public interface Json {

        public static final String STORAGE_FILE_NAME = "playlists.json";

        public interface Key {

            public static final String ID = "id";
            public static final String PLAYLISTS = "playlists";
            public static final String PLAYLIST = "playlist";
            public static final String NAME = "name";

        }

        public interface Playlist {

            public static final String FAVORITES = "Favorites";
            public static final String HISTORY = "History";

        }

    }

}
