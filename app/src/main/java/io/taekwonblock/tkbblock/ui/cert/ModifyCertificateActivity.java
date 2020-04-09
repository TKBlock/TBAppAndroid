package io.taekwonblock.tkbblock.ui.cert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.taekwonblock.tkbblock.ApplicationClass;
import io.taekwonblock.tkbblock.DeleteCertificateMutation;
import io.taekwonblock.tkbblock.R;
import io.taekwonblock.tkbblock.model.CertificateModel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.jetbrains.annotations.NotNull;

public class ModifyCertificateActivity extends AppCompatActivity {

    CertificateModel certificateModel;

    RecyclerView list;

    private ApplicationClass applicationClass;
    private ApolloClient apolloClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_certificate);

        applicationClass = (ApplicationClass) getApplicationContext();
        apolloClient = applicationClass.apolloClient();



        Intent intent = getIntent();
        certificateModel = (CertificateModel) intent.getSerializableExtra("certModel");

        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(certificateModel.getCert_name());
        }


        list = findViewById(R.id.cert_detail_list);

        LinearLayoutManager layoutManagerHorizontal = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        CertDetailImagesAdaptor certDetailImagesAdaptor = new CertDetailImagesAdaptor(this, certificateModel.getImages());
        list.setLayoutManager(layoutManagerHorizontal);
        list.setAdapter(certDetailImagesAdaptor);



    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_cert_detail, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_cert_delete :

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                // Add the buttons
                builder.setMessage(R.string.dialog_check_cert_delete);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        SharedPreferences sharedpreferences = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
                        String uuid = sharedpreferences.getString("uuid", "");

                        DeleteCertificateMutation deleteCertificate = DeleteCertificateMutation.builder().uuid(uuid).cert_idx(certificateModel.getIDX()).build();
                        apolloClient.mutate(deleteCertificate).enqueue(new ApolloCall.Callback<DeleteCertificateMutation.Data>() {
                            @Override
                            public void onResponse(@NotNull Response<DeleteCertificateMutation.Data> response) {
                                finish();
                            }

                            @Override
                            public void onFailure(@NotNull ApolloException e) {
                                e.printStackTrace();
                            }
                        });

                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                // Set other dialog properties

                // Create the AlertDialog
                AlertDialog dialog = builder.create();

                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
