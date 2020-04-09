package io.taekwonblock.tkbblock.wxapi;

import androidx.appcompat.app.AppCompatActivity;
import io.taekwonblock.tkbblock.ApplicationClass;
import io.taekwonblock.tkbblock.Constants;
import io.taekwonblock.tkbblock.R;
import io.taekwonblock.tkbblock.WeixinLoginQuery;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.exception.ApolloException;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {


    private static final String TAG = "WXEntryActivity";
    Context mContext;
    IWXAPI iwxapi;
    ProgressDialog mProgressDialog;

    private ApplicationClass applicationClass;
    private ApolloClient apolloClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        applicationClass = (ApplicationClass) getApplicationContext();
        apolloClient = applicationClass.apolloClient();

        getSupportActionBar().hide();
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //接收到分享以及登录的intent传递handleIntent方法，处理结果
        iwxapi = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        iwxapi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                String code = ((SendAuth.Resp) baseResp).code;
                //获取accesstoken
                getAccessToken(code);
                Log.d("fantasychongwxlogin", code.toString() + "");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝授权
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                finish();
                break;
            default:
                finish();
                break;
        }

    }


    private void getAccessToken(String code) {
        createProgressDialog();
        //获取授权


        WeixinLoginQuery weixinLoginQuery =  WeixinLoginQuery.builder().appid(Constants.APP_ID).code(code).build();
        apolloClient.query(weixinLoginQuery).enqueue(new ApolloCall.Callback<WeixinLoginQuery.Data>() {
            @Override
            public void onResponse(@NotNull com.apollographql.apollo.api.Response<WeixinLoginQuery.Data> response) {
//                String responseInfo= response.body().string();
//                Log.d("fan12", "onResponse: " +responseInfo);
                String access = null;
                String openId = null;
                Boolean hasAccount = false;

                Log.v(TAG, response.data().toString());

                if( response.data().weixinLogin() != null) {
                    Log.v(TAG, response.data().weixinLogin().toString());

                    access = response.data().weixinLogin().access_token();
                    openId = response.data().weixinLogin().openid();
                    hasAccount = response.data().weixinLogin().hasAccount();
                    Log.v(TAG, "hasAccount : " + response.data().weixinLogin().hasAccount());



                    if(hasAccount == false) {
                        getUserInfo(access, openId, hasAccount);
                    } else {
                        SharedPreferences.Editor editor= getSharedPreferences("userInfo", MODE_PRIVATE).edit();
                        Gson gson = new Gson();

                        editor.putString("accountinfo", gson.toJson(response.data().weixinLogin() ) );
                        editor.putBoolean("hasAccount", hasAccount);
                        editor.commit();
                        finish();
                        mProgressDialog.dismiss();
                    }
            }


            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        });

    }

    private void createProgressDialog() {
        mContext=this;
        mProgressDialog=new ProgressDialog(mContext);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//转盘
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setTitle("提示");
        mProgressDialog.setMessage("登录中，请稍后");
        mProgressDialog.show();
    }



    private void getUserInfo(String access, String openid, final Boolean hasAccount) {
        String getUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access + "&openid=" + openid;
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(getUserInfoUrl)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("fan12", "onFailure: ");
                mProgressDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseInfo= response.body().string();
                Log.d("fan123", "onResponse: " + responseInfo);
                SharedPreferences.Editor editor= getSharedPreferences("userInfo", MODE_PRIVATE).edit();
                editor.putString("responseInfo", responseInfo);
                editor.putBoolean("hasAccount", hasAccount);
                editor.commit();
                finish();
                mProgressDialog.dismiss();
            }
        });
    }

}
