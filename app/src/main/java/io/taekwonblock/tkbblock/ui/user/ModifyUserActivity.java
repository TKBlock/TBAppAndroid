package io.taekwonblock.tkbblock.ui.user;

import androidx.appcompat.app.AppCompatActivity;
import io.taekwonblock.tkbblock.ApplicationClass;
import io.taekwonblock.tkbblock.MobileUserQuery;
import io.taekwonblock.tkbblock.R;
import io.taekwonblock.tkbblock.UpdateMobileUserInfoMutation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.jetbrains.annotations.NotNull;

public class ModifyUserActivity extends AppCompatActivity {

    private static final String TAG = "ModifyUserActivity";
    private ApplicationClass applicationClass;
    private ApolloClient apolloClient;

    EditText editName;
    EditText editAge;
    EditText editAddress;
    EditText editPhone;

    Button btnSubmit;

    int user_type;
    String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        applicationClass = (ApplicationClass) getApplicationContext();
        apolloClient = applicationClass.apolloClient();

        editName = findViewById(R.id.mod_user_name);

        editAge = findViewById(R.id.mod_user_age);

        editAddress = findViewById(R.id.mod_user_address);

        editPhone = findViewById(R.id.mod_user_phone);

        btnSubmit = findViewById(R.id.mod_user_submit);

        SharedPreferences sharedpreferences = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);


        user_type = sharedpreferences.getInt("type", 0);
        uuid = sharedpreferences.getString("uuid", "");

        MobileUserQuery mobileUserQuery = MobileUserQuery.builder().type(user_type).uuid(uuid).build();
        apolloClient.query(mobileUserQuery).enqueue(new ApolloCall.Callback<MobileUserQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<MobileUserQuery.Data> response) {

                final MobileUserQuery.MobileUser mobileUser = response.data().mobileUser();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(mobileUser != null) {

                            Log.v(TAG, mobileUser.toString());

                            editName.setText(mobileUser.name());

                            editAddress.setText(mobileUser.address());
                            editPhone.setText(mobileUser.phone());
                            editAge.setText(mobileUser.age().toString());
                        }

                        Log.v(TAG, "" + mobileUser.toString());

                    }

                });




            }

            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newName = editName.getText().toString();
                int newAge = Integer.parseInt(editAge.getText().toString());
                String newAddress = editAddress.getText().toString();
                String newPhone = editPhone.getText().toString();

                UpdateMobileUserInfoMutation updateMobileUserInfoMutation =
                        UpdateMobileUserInfoMutation.builder().account_type(user_type).uuid(uuid).name(newName).age(newAge).address(newAddress).phone(newPhone).build();
                apolloClient.mutate(updateMobileUserInfoMutation).enqueue(new ApolloCall.Callback<UpdateMobileUserInfoMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<UpdateMobileUserInfoMutation.Data> response) {
                        finish();
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {

                    }
                });

            }
        });

    }
}
