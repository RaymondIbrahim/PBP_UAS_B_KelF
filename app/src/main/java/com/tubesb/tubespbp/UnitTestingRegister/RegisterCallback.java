package com.tubesb.tubespbp.UnitTestingRegister;

import com.tubesb.tubespbp.dao.UserDAO;

public interface RegisterCallback {
    void onSuccess(boolean value, UserDAO user);
    void onError();
}
