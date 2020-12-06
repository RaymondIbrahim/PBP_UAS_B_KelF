package com.tubesb.tubespbp.UnitTestingRegister;

import com.tubesb.tubespbp.api.ApiClient;
import com.tubesb.tubespbp.api.ApiInterface;
import com.tubesb.tubespbp.api.UserResponse;
import com.tubesb.tubespbp.dao.UserDAO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterService {

    public void register(final RegisterView view, String email, String password, String nama,
                         String alamat,String noTelp, final RegisterCallback callback){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> register = apiService.createUser(email, password, nama, alamat, noTelp);
        register.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call,
                                   Response<UserResponse> response) {
                if(response.body().getMessage().equalsIgnoreCase("berhasil register"
                )){
                    callback.onSuccess(true, response.body().getUsers().get(0));
                }
                else{
                    callback.onError();
                    view.showRegisterError(response.body().getMessage());
                }
            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                view.showErrorResponse(t.getMessage());
                callback.onError();
            }
        });
    }
    public Boolean getValid(final RegisterView view, String email, String password, String nama,
                            String alamat, String noTelp){
        final Boolean[] bool = new Boolean[1];
        register(view, email, password, nama, alamat, noTelp, new RegisterCallback() {
            @Override
            public void onSuccess(boolean value, UserDAO user) {
                bool[0] = true;
            }
            @Override
            public void onError() {
                bool[0] = false;
            }
        });
        return bool[0];
    }

}
