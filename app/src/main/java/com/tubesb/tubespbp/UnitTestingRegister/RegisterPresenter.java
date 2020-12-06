package com.tubesb.tubespbp.UnitTestingRegister;

import com.tubesb.tubespbp.dao.UserDAO;

public class RegisterPresenter {

    private RegisterView view;
    private RegisterService service;
    private RegisterCallback callback;
    public RegisterPresenter(RegisterView view, RegisterService service) {
        this.view = view;
        this.service = service;
    }
    public void onRegisterClicked() {
        if (view.getEmail().isEmpty()) {
            view.showEmailError("Email tidak boleh kosong");
            return;
        }else if (!view.getEmail().matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$")) {
            view.showEmailError("Email harus sesuai struktur");
            return;
        }else if (view.getPassword().isEmpty()) {
            view.showPasswordError("Password tidak boleh kosong");
            return;
        } else if (view.getPassword().length()<6) {
            view.showPasswordError("Password tidak boleh kurang dari 6 karakter");
            return;
        }else if (view.getNama().isEmpty()) {
            view.showNamaError("Nama tidak boleh kosong");
            return;
        }else if (view.getAlamat().isEmpty()) {
            view.showAlamatError("Alamat tidak boleh kosong");
            return;
        }else if (view.getnoTelp().isEmpty()) {
            view.shownoTelpError("No Telepon tidak boleh kosong");
            return;
        }else if (view.getnoTelp().length()<12) {
            view.shownoTelpError("No Telepon tidak boleh kurang dari 12 digit");
            return;
        }else {
            service.register(view, view.getEmail(), view.getPassword(), view.getNama(),view.getAlamat(),view.getnoTelp(), new
                    RegisterCallback() {
                        @Override
                        public void onSuccess(boolean value, UserDAO user) {
                            if (!user.getEmail().isEmpty() && !user.getPassword().isEmpty() &&!user.getNama().isEmpty() &&
                                    !user.getAlamat().isEmpty() && !user.getNoTelp().isEmpty() ) {
                                view.startMainActivity();
                            } else {
                                view.startUserProfileActivity(user);
                            }
                        }

                        @Override
                        public void onError() {
                        }
                    });
            return;
        }
    }
}
