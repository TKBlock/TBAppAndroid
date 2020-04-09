package io.taekwonblock.tkbblock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import static io.taekwonblock.tkbblock.Constants.APP_ID;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    String nickname, headimgurl;
    TextView tv;
    ImageView btn;
    IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        SharedPreferences.Editor editor= getSharedPreferences("userInfo", MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();

        regToWx();


        btn = findViewById(R.id.btn_login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!api.isWXAppInstalled()) {
                    Toast.makeText(LoginActivity.this, "您的设备未安装微信客户端", Toast.LENGTH_SHORT).show();
                } else {
                    final SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "wechat_sdk_demo_test";
                    api.sendReq(req);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp= getSharedPreferences("userInfo", MODE_PRIVATE);
        Boolean hasAccount = sp.getBoolean("hasAccount", false);


        if(hasAccount == false) {
            String responseInfo= sp.getString("responseInfo", "");

            if (!responseInfo.isEmpty()) {
                Log.v(TAG, responseInfo.toString());
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        } else {
            String accountinfo = sp.getString("accountinfo", "");

            Log.v(TAG, accountinfo.toString());

            try {
                JSONObject accountInfoObject = new JSONObject(accountinfo);
                Log.v(TAG, "open id : " + accountInfoObject.getString("openid") );

                SharedPreferences sharedpreferences = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("openid", accountInfoObject.getString("openid"));
                editor.putString("name", accountInfoObject.getString("name") );
                editor.putInt("type",  accountInfoObject.getInt("type") );
                editor.putString("uuid", accountInfoObject.getString("uuid") );
                editor.commit();

                Intent mainIntent = new Intent(this, HomeActivity.class);
                startActivity(mainIntent);

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            SharedPreferences sharedpreferences = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedpreferences.edit();
//            editor.putString("openid", openID);
//            editor.putString("name", "New Name" );
//            editor.putInt("type", 1 );
//            editor.putString("uuid", "f66dcaf08b9711da5f93e61bc6ca9a3d" );
//            editor.commit();

//
//
//                Intent mainIntent = new Intent(this, HomeActivity.class);
//                startActivity(mainIntent);
        }

    }


    private void regToWx() {
        api = WXAPIFactory.createWXAPI(this, APP_ID, false);
        api.registerApp(APP_ID);
    }
}
