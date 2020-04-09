package io.taekwonblock.tkbblock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

public class FirebaseLoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 8787;
    private static final String TAG = "FirebaseLoginActivity";


    private ApplicationClass applicationClass;
    private ApolloClient apolloClient;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        applicationClass = (ApplicationClass) getApplicationContext();
        apolloClient = applicationClass.apolloClient();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                    // ...
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);


    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            Log.v(TAG, "openid: " + account.getEmail());
            Log.v(TAG, "unionid: "  + account.getId());
            // Signed in successfully, show authenticated UI.

            final String email =  account.getEmail();
            final String id = account.getId();


            GoogleLoginQuery googleLoginQuery = GoogleLoginQuery.builder().email( account.getEmail() ).innerID( account.getId() ).build();
            apolloClient.query(googleLoginQuery).enqueue(new ApolloCall.Callback<GoogleLoginQuery.Data>() {
                @Override
                public void onResponse(@NotNull Response<GoogleLoginQuery.Data> response) {

                    try {
                        Boolean hasAccount = response.data().googleLogin.hasAccount();

                        if(hasAccount == false) {
                            SharedPreferences.Editor editor= getSharedPreferences("userInfo", MODE_PRIVATE).edit();
                            editor.putString("responseInfo", "{ \"openid\": \"" + email + "\", \"unionid\": \"" + id + "\"}");
                            editor.commit();

                            Intent intent = new Intent(FirebaseLoginActivity.this, RegisterActivity.class);
                            startActivity(intent);
                        } else {
                            SharedPreferences sharedpreferences = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("openid", email);
                            editor.putString("name", response.data().googleLogin.name() );
                            editor.putInt("type",  response.data().googleLogin.type() );
                            editor.putString("uuid", response.data().googleLogin.uuid() );
                            editor.commit();

                            Intent mainIntent = new Intent(FirebaseLoginActivity.this, HomeActivity.class);
                            startActivity(mainIntent);
                        }

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NotNull ApolloException e) {
                    e.printStackTrace();
                }
            });


            SharedPreferences spUserInfo= getSharedPreferences("userInfo", MODE_PRIVATE);
            SharedPreferences.Editor eUserInfo = spUserInfo.edit();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());

//            Log.w(TAG, "signInResult:failed code=" + e.toString());
////            updateUI(null);
        }


    }
}
