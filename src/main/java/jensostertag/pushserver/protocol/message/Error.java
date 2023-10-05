package jensostertag.pushserver.protocol.message;

import com.google.gson.annotations.Expose;

public class Error {
    @Expose
    public String errorDetails;

    public Error(String errorDetails) {
        this.errorDetails = errorDetails;
    }
}
