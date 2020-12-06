package com.tubesb.tubespbp.UnitTestingRegister;

import android.content.Context;
import android.content.Intent;

import com.tubesb.tubespbp.FrontEnd.ShowProfileActivity;
import com.tubesb.tubespbp.dao.UserDAO;

public class ActivityUtil {

    private Context context;
    public ActivityUtil(Context context) {
        this.context = context;
    }
    public void startMainActivity() {
        context.startActivity(new Intent(context, RegisterActivity.class));
    }
    public void startUserProfile(UserDAO user) {
        Intent i = new Intent(context, ShowProfileActivity.class);
        i.putExtra("email",user.getEmail());
        i.putExtra("password",user.getPassword());
        i.putExtra("name",user.getNama());
        i.putExtra("alamat",user.getAlamat());
        i.putExtra("noTelp",user.getNoTelp());
        context.startActivity(i);
    }

}
