package io.taekwonblock.tkbblock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    Intent intent;
    String openID, unionID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //Test
        openID = "asvinaskuGFAVk2bE5D_7JyAw";
        unionID = "asvinaskuGFAVk2bE5D_TATA";
        //

        SharedPreferences sharedpreferences = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);



//        수련생용
//        SharedPreferences.Editor editor = sharedpreferences.edit();
//        editor.putString("openid", openID);
//        editor.putString("name", "New Name" );
//        editor.putInt("type", 1 );
//        editor.putString("uuid", "f66dcaf08b9711da5f93e61bc6ca9a3d" );
//        editor.commit();


//        사범용
//        SharedPreferences.Editor editor = sharedpreferences.edit();
//        editor.putString("openid", openID);
//        editor.putString("name", "사범");
//        editor.putInt("type", 2 );
//        editor.putString("uuid", "16090d85beb9bb4aac282bd1231d0657" );
//        editor.commit();


        Log.v("TEST", sharedpreferences.contains("openid") + "");


        if(sharedpreferences.contains("openid") == true) {
            intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            intent = new Intent(MainActivity.this, FirebaseLoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        }

    }
}
