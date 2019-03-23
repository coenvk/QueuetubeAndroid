package com.arman.queuetube.util.exceptions

import java.io.IOException

class WifiException : IOException() {

    override val message: String?
        get() = "You don't have an internet connection"

}
