package com.arman.queuetube.util.exceptions;

import java.io.IOException;

public class WifiException extends IOException {

    @Override
    public String getMessage() {
        return "You don't have an internet connection";
    }

}
