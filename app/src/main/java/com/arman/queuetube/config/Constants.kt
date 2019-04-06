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
            const val FAVORITED = "favorited"
            const val VIEWS = "views"
            const val LIKES = "likes"
            const val DISLIKES = "dislikes"
        }
    }

    interface History {
        companion object {
            const val MAX_SIZE_SHORT = 8
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
