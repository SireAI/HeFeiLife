package com.sire.corelibrary.Networking.downlaod.downloadCore;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/09/06
 * Author:Sire
 * Description:
 * ==================================================
 */

public class ResourceException extends RuntimeException{
    public ResourceException() {
        super();
    }

    public ResourceException(String message) {
        super(message);
    }

    public ResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceException(Throwable cause) {
        super(cause);
    }


}
