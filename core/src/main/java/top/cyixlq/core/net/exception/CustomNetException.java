package top.cyixlq.core.net.exception;

import java.io.IOException;

public class CustomNetException extends IOException {

    private int code;

    public CustomNetException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
