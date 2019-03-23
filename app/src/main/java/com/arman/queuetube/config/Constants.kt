package com.arman.queuetube.config

class Constants {

    interface Action {
        companion object {

            const val MAIN_ACTION = "com.arman.queuetube.action.main"
            const val PLAY_ACTION = "com.arman.queuetube.action.play"
            const val PAUSE_ACTION = "com.arman.queuetube.action.pause"
            const val NEXT_ACTION = "com.arman.queuetube.action.next"
            const val STOP_ACTION = "com.arman.queuetube.action.stop"
        }

    }

    interface Notification {
        companion object {

            const val ID = 666
            const val CHANNEL_NAME = "Queuetube notifications"
            const val CHANNEL_ID = "49210233949201482942101"
        }

    }

    interface YouTube {

        interface Topics {

            interface Music {
                companion object {

                    const val CHRISTIAN = "/m/02mscn"
                    const val CLASSICAL = "/m/0ggq0m"
                    const val COUNTRY = "/m/01lyv"
                    const val ELECTRONIC = "/m/02lkt"
                    const val HIP_HOP = "/m/0glt670"
                    const val INDEPENDENT = "/m/05rwpb"
                    const val JAZZ = "/m/03_d0"
                    const val ASIAN = "/m/028sqc"
                    const val LATIN_AMERICAN = "/m/0g293"
                    const val POP = "/m/064t9"
                    const val REGGAE = "/m/06cqb"
                    const val RHYTHM_AND_BLUES = "/m/06j6l"
                    const val ROCK = "/m/06by7"
                    const val SOUL = "/m/0gywn"
                }

            }

            interface Gaming {
                companion object {

                    const val ACTION = "/m/025zzc"
                    const val ACTION_ADVENTURE = "/m/02ntfj"
                    const val CASUAL = "/m/0b1vjn"
                    const val MUSIC = "/m/02hygl"
                    const val PUZZLE = "/m/04q1x3q"
                    const val RACING = "/m/01sjng"
                    const val ROLE_PLAYING = "/m/0403l3g"
                    const val SIMULATION = "/m/021bp2"
                    const val SPORTS = "/m/022dc6"
                    const val STRATEGY = "/m/03hf_rm"
                }

            }

            interface Sports {
                companion object {

                    const val AMERICAN_FOOTBALL = "/m/0jm_"
                    const val BASEBALL = "/m/018jz"
                    const val BASKETBALL = "/m/018w8"
                    const val BOXING = "/m/01cgz"
                    const val CRICKET = "/m/09xp_"
                    const val FOOTBALL = "/m/02vx4"
                    const val GOLF = "/m/037hz"
                    const val ICE_HOCKEY = "/m/03tmr"
                    const val MIXEL_MARTIAL_ARTS = "/m/01h7lh"
                    const val MOTORSPORT = "/m/0410tth"
                    const val TENNIS = "/m/07bs0"
                    const val VOLLEYBALL = "/m/07_53"
                }

            }

            interface Entertainment {
                companion object {

                    const val HUMOR = "/m/09kqc"
                    const val MOVIES = "/m/02vxn"
                    const val PERFORMING_ARTS = "/m/05qjc"
                    const val PROFESSIONAL_WRESTLING = "/m/066wd"
                    const val TV_SHOWS = "/m/0f2f9"
                }

            }

            interface LifeStyle {
                companion object {

                    const val FASHION = "/m/032tl"
                    const val FITNESS = "/m/027x7n"
                    const val FOOD = "/m/02wbm"
                    const val HOBBY = "/m/03glg"
                    const val PETS = "/m/068hy"
                    const val BEAUTY = "/m/041xxh"
                    const val TECHNOLOGY = "/m/07c1v"
                    const val TOURISM = "/m/07bxq"
                    const val VEHICLES = "/m/07yv9"
                }

            }

            interface Society {
                companion object {

                    const val BUSINESS = "/m/09s1f"
                    const val HEALTH = "/m/0kt51"
                    const val MILITARY = "/m/01h6rj"
                    const val POLITICS = "/m/05qt0"
                    const val RELIGION = "/m/06bvp"
                }

            }

            companion object {

                const val MUSIC = "/m/04rlf"
                const val GAMING = "/m/0bzvm2"
                const val SPORTS = "/m/06ntj"
                const val ENTERTAINMENT = "/m/02jjt"
                const val LIFESTYLE = "/m/019_rr"
                const val SOCIETY = "/m/098wr"

                const val KNOWLEDGE = "/m/01k8wb"
            }

        }

    }

    interface Key {
        companion object {

            const val API_KEY = "AIzaSyAf95ydwL8dJb-yUmvFKAC9AzDiwbzlrVU"
            const val TEST_AD_KEY = "ca-app-pub-3940256099942544/6300978111"
            const val AD_KEY = "ca-app-pub-5272639228074525~9828413436"
        }

    }

    interface Fragment {

        interface Argument {
            companion object {

                const val PLAYLIST_NAME = "playlistName"
                const val IS_CLICKABLE = "isClickable"
                const val IS_SORTABLE = "isSortable"
            }

        }

        companion object {

            const val MAIN = 0
            const val TRENDING = 1
            const val DISCOVER = 2
            const val LIBRARY = 3
        }

    }

    interface VideoData {
        companion object {

            const val TITLE = "title"
            const val ID = "id"
            const val CHANNEL = "channel"
            const val DESCRIPTION = "description"
            const val PUBLISHED_ON = "publishedOn"
            const val THUMBNAIL_URL = "thumbnailUrl"
            const val VIEWS = "views"
            const val LIKES = "likes"
            const val DISLIKES = "dislikes"
        }

    }

    interface Json {

        interface Key {
            companion object {

                const val ID = "id"
                const val PLAYLISTS = "playlists"
                const val PLAYLIST = "playlist"
                const val NAME = "name"
            }

        }

        interface Playlist {
            companion object {

                const val FAVORITES = "Favorites"
                const val HISTORY = "History"
            }

        }

        companion object {

            const val STORAGE_FILE_NAME = "playlists.json"
        }

    }

}
