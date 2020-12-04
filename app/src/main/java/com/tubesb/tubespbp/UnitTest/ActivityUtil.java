package com.tubesb.tubespbp.UnitTest;

import android.content.Context;
import android.content.Intent;

import com.tubesb.tubespbp.MainActivity;
import com.tubesb.tubespbp.ProfileActivity;
import com.tubesb.tubespbp.dao.UserDAO;

public class ActivityUtil {

    private Context context;
    public ActivityUtil(Context context) {
        this.context = context;
    }
    public void startMainActivity() {
        context.startActivity(new Intent(context, MainActivity.class));
    }
    public void startUserProfile(UserDAO user) {
        Intent i = new Intent(context, ProfileActivity.class);
        i.putExtra("id",user.getId());
        i.putExtra("name",user.getNama());
        i.putExtra("alamat",user.getAlamat());
        i.putExtra("noTelp",user.getNoTelp());
        i.putExtra("image",user.getImage());
        context.startActivity(i);
    }

}
