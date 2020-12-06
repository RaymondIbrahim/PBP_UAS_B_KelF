package com.tubesb.tubespbp.UnitTestingRegister;

import com.tubesb.tubespbp.dao.UserDAO;

public interface RegisterView {
    String getEmail();
    void showEmailError(String message);
    String getPassword();
    void showPasswordError(String message);
    String getNama();
    void showNamaError(String message);
    String getAlamat();
    void showAlamatError(String message);
    String getnoTelp();
    void shownoTelpError(String message);
    void startMainActivity();
    void startUserProfileActivity(UserDAO user);
    void showRegisterError(String message);
    void showErrorResponse(String message);
}
