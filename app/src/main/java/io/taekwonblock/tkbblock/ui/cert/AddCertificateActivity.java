package io.taekwonblock.tkbblock.ui.cert;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.taekwonblock.tkbblock.ApplicationClass;
import io.taekwonblock.tkbblock.CreateCertificateMutation;
import io.taekwonblock.tkbblock.ImageFilePath;
import io.taekwonblock.tkbblock.R;

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.FileUpload;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddCertificateActivity extends AppCompatActivity {

    private static final String TAG = "AddCertificateActivity";

    private ApplicationClass applicationClass;
    private ApolloClient apolloClient;

    Button btnPickImages;
    Button btnSubmit;

    EditText editCertName;

    LinearLayout layoutNoImages;
    RecyclerView gallery;
    CertificateImagesAdaptor certificateImagesAdaptor;

    List<Uri> uriList;
    List<FileUpload> fileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_certificate);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        applicationClass = (ApplicationClass) getApplicationContext();
        apolloClient = applicationClass.apolloClient();

        editCertName = findViewById(R.id.add_cert_name);

        btnPickImages = findViewById(R.id.add_cert_pick);
        btnPickImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
            }
        });

        layoutNoImages = findViewById(R.id.add_cert_layout_no_cert);


        gallery = findViewById(R.id.add_cert_gallery);

        btnSubmit = findViewById(R.id.add_cert_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String certName = editCertName.getText().toString();

                SharedPreferences sharedpreferences = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
                String uuid = sharedpreferences.getString("uuid", "");

                Log.v(TAG, certName + "," + uuid + "," + fileList.toString());

                CreateCertificateMutation createCertificateMutation = CreateCertificateMutation.builder().cert_name(certName).uuid(uuid).images(fileList).build();
                apolloClient.mutate(createCertificateMutation).enqueue(new ApolloCall.Callback<CreateCertificateMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<CreateCertificateMutation.Data> response) {
                        Log.v(TAG, response.toString());

                        finish();
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        e.printStackTrace();
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        uriList = new ArrayList<Uri>();
        fileList = new ArrayList<FileUpload>();

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Uri selectedImageUri = data.getData();
            if (null != selectedImageUri) {
                // Get the path from the Uri
                uriList.add(selectedImageUri);

                ContentResolver contentResolver = getContentResolver();

//                fileList.add(new FileUpload("image/*", new File(getRealPathFromURI(selectedImageUri))));
                fileList.add(new FileUpload("image/*", new File(ImageFilePath.getPath(this, selectedImageUri))));
                Log.v(TAG, "if");
            } else {
                if (data.getClipData() != null) {
                    Log.v(TAG, "else");

                    ClipData mClipData = data.getClipData();

                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        uriList.add(uri);
                        fileList.add(new FileUpload("image/*", new File(getRealPathFromURI(uri))));

                        if (i == 0) {
                            Log.v(TAG, uri.toString());
//                            imageTest.setImageURI(uri);
                        }
                    }
                    Log.v("LOG_TAG", "Selected Images" + uriList.size());
                }
            }

            gallery.setLayoutManager(new GridLayoutManager(this, 1));
            certificateImagesAdaptor = new CertificateImagesAdaptor(uriList);
            gallery.setAdapter(certificateImagesAdaptor);
//
            layoutNoImages.setVisibility(View.GONE);
            gallery.setVisibility(View.VISIBLE);

        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        if (contentUri.getPath().startsWith("/storage")) {
            return contentUri.getPath();
        }
        return contentUri.getPath().split(":")[1];
    }

}
