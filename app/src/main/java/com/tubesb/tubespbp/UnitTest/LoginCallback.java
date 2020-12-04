package com.tubesb.tubespbp.UnitTest;

import com.tubesb.tubespbp.dao.UserDAO;

public interface LoginCallback {
    void onSuccess(boolean value, UserDAO user);
    void onError();
}
