package com.uphar.bussinesss.domain.Utils.networkException;

import java.io.IOException;

public class AccessDeniedException extends IOException {

    @Override
    public String getMessage() {
        return "Access denied";
    }

}