package com.arman.queuetube.config

import com.arman.queuetube.model.Category

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

                    val CHRISTIAN = Category("/m/02mscn", "Christian")
                    val CLASSICAL = Category("/m/0ggq0m", "Classical")
                    val COUNTRY = Category("/m/01lyv", "Country")
                    val ELECTRONIC = Category("/m/02lkt", "Electronic")
                    val HIP_HOP = Category("/m/0glt670", "Hip Hop")
                    val INDEPENDENT = Category("/m/05rwpb", "Independent")
                    val JAZZ = Category("/m/03_d0", "Jazz")
                    val ASIAN = Category("/m/028sqc", "Asian")
                    val LATIN_AMERICAN = Category("/m/0g293", "Latin American")
                    val POP = Category("/m/064t9", "Pop")
                    val REGGAE = Category("/m/06cqb", "Reggae")
                    val RHYTHM_AND_BLUES = Category("/m/06j6l", "Rhythm and Blues")
                    val ROCK = Category("/m/06by7", "Rock")
                    val SOUL = Category("/m/0gywn", "Soul")

                    val ALL = mutableListOf(CHRISTIAN, CLASSICAL, COUNTRY, ELECTRONIC, HIP_HOP, INDEPENDENT, JAZZ, ASIAN, LATIN_AMERICAN, POP, REGGAE, RHYTHM_AND_BLUES, ROCK, SOUL)
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

                    val ALL = mutableListOf(ACTION, ACTION_ADVENTURE, CASUAL, MUSIC, PUZZLE, RACING, ROLE_PLAYING, SIMULATION, SPORTS, STRATEGY)
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
                    const val MIXED_MARTIAL_ARTS = "/m/01h7lh"
                    const val MOTORSPORT = "/m/0410tth"
                    const val TENNIS = "/m/07bs0"
                    const val VOLLEYBALL = "/m/07_53"

                    val ALL = mutableListOf(AMERICAN_FOOTBALL, BASEBALL, BASKETBALL, BOXING, CRICKET, FOOTBALL, GOLF, ICE_HOCKEY, MIXED_MARTIAL_ARTS, MOTORSPORT, TENNIS, VOLLEYBALL)
                }

            }

            interface Entertainment {
                companion object {

                    const val HUMOR = "/m/09kqc"
                    const val MOVIES = "/m/02vxn"
                    const val PERFORMING_ARTS = "/m/05qjc"
                    const val PROFESSIONAL_WRESTLING = "/m/066wd"
                    const val TV_SHOWS = "/m/0f2f9"

                    val ALL = mutableListOf(HUMOR, MOVIES, PERFORMING_ARTS, PROFESSIONAL_WRESTLING, TV_SHOWS)
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

                    val ALL = mutableListOf(FASHION, FITNESS, FOOD, HOBBY, PETS, BEAUTY, TECHNOLOGY, TOURISM, VEHICLES)
                }

            }

            interface Society {
                companion object {

                    const val BUSINESS = "/m/09s1f"
                    const val HEALTH = "/m/0kt51"
                    const val MILITARY = "/m/01h6rj"
                    const val POLITICS = "/m/05qt0"
                    const val RELIGION = "/m/06bvp"

                    val ALL = mutableListOf(BUSINESS, HEALTH, MILITARY, POLITICS, RELIGION)
                }

            }

            companion object {

                val MUSIC = Category("/m/04rlf", "Music")
                val GAMING = Category("/m/0bzvm2", "Gaming")
                val SPORTS = Category("/m/06ntj", "Sports")
                val ENTERTAINMENT = Category("/m/02jjt", "Entertainment")
                val LIFESTYLE = Category("/m/019_rr", "Lifestyle")
                val SOCIETY = Category("/m/098wr", "Society")

                val KNOWLEDGE = Category("/m/01k8wb", "Knowledge")

                val ALL = mutableListOf(MUSIC, GAMING, SPORTS, ENTERTAINMENT, LIFESTYLE, SOCIETY, KNOWLEDGE)
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
                const val IS_DRAGGABLE = "isDraggable"
                const val IS_SHUFFLABLE = "isShufflable"
                const val IS_SORTABLE = "isSortable"
                const val IS_EDITABLE = "isEditable"
                const val IS_REFRESHABLE = "isRefreshable"
                const val LOAD_ON_START = "loadOnStart"
                const val TOPIC = "topic"
                const val CATEGORY = "category"

            }

        }

        companion object {

            const val HOME = 0
            const val SEARCH = 1
            const val LIBRARY = 2
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
