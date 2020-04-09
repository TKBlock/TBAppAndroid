package io.taekwonblock.tkbblock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private ApplicationClass applicationClass;
    private ApolloClient apolloClient;

    EditText edit_name;
    EditText edit_age;
    EditText edit_address;
    EditText edit_phone;

    RadioGroup rg_type;
    RadioButton rg_selected;

    Button btn_submit;

    String openID, unionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //Test
        // openID = "12345678abcdef";
        // unionID = "987654zxcv";
        //

        applicationClass = (ApplicationClass) getApplicationContext();
        apolloClient = applicationClass.apolloClient();

        SharedPreferences sp= getSharedPreferences("userInfo", MODE_PRIVATE);
        String responseInfo= sp.getString("responseInfo", "");

        try {
            JSONObject jsonObject = new JSONObject(responseInfo);
            Log.v("JSONObject", jsonObject.toString());

            openID = jsonObject.getString("openid");
            unionID = jsonObject.getString("unionid");

            Log.v("JSONObject", "open/unionid : " + openID + "/" + unionID);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences.Editor editor= getSharedPreferences("userInfo", MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();


        edit_name = findViewById(R.id.register_edit_name);
        edit_age = findViewById(R.id.register_edit_age);
        edit_address = findViewById(R.id.register_edit_address);
        edit_phone = findViewById(R.id.register_edit_phone);

        rg_type = findViewById(R.id.register_radio_type);
        btn_submit = findViewById(R.id.register_btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = edit_name.getText().toString();
                int age = Integer.parseInt(edit_age.getText().toString() );
                String address = edit_address.getText().toString();
                String phone = edit_phone.getText().toString();
                int type = 0;

                int rg_selectedId = rg_type.getCheckedRadioButtonId();

                switch (rg_selectedId) {
                    case R.id.register_radio_student:
                        type = 1;
                        break;
                    case R.id.register_radio_instructor:
                        type = 2;
                        break;
                }


                try {
                    SignInMobileMutation signInMobileMutation =  SignInMobileMutation.builder().openId(openID).unionId(unionID).name(name).age(age).address(address).phone(phone).account_type(type).build();
                    apolloClient.mutate(signInMobileMutation).enqueue(new ApolloCall.Callback<SignInMobileMutation.Data>() {
                        @Override
                        public void onResponse(@NotNull Response<SignInMobileMutation.Data> response) {

                            Log.v(TAG, response.data().signInMobile.message().toString());

                            try {
                                JSONObject jsonObject = new JSONObject( response.data().signInMobile.message().toString() );
                                Log.v(TAG,  jsonObject.getString("name") );

                                SharedPreferences sharedpreferences = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("openid", openID);
                                editor.putString("name", jsonObject.getString("name") );
                                editor.putInt("type", jsonObject.getInt("type") );
                                editor.putString("uuid", jsonObject.getString("uuid") );
                                editor.commit();

                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                startActivity(intent);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(@NotNull ApolloException e) {
                            Toast.makeText(applicationClass, "Register Failed", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage(), e);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
