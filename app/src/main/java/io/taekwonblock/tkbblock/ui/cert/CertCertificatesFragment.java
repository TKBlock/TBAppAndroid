package io.taekwonblock.tkbblock.ui.cert;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.taekwonblock.tkbblock.ApplicationClass;
import io.taekwonblock.tkbblock.CertificatesQuery;
import io.taekwonblock.tkbblock.R;

public class CertCertificatesFragment extends Fragment {

    private static final String TAG = "CertCertificates";
    private ApplicationClass applicationClass;
    private ApolloClient apolloClient;

    Button btnAddCert;
    Context mContext;
    Fragment mFragment;

    RecyclerView certListview;
    CertListAdaptor certListAdaptor;

    TextView tvNoCerts;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mFragment = this;

        applicationClass = (ApplicationClass) mContext.getApplicationContext();
        apolloClient = applicationClass.apolloClient();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_cert_certificates, container, false);

        btnAddCert = root.findViewById(R.id.cert_add_cert);
        btnAddCert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddCertificateActivity.class);
                startActivityForResult(intent, 300);

            }
        });

        tvNoCerts = root.findViewById(R.id.cert_no_certs);

        certListview = root.findViewById(R.id.cert_list);

        SharedPreferences sharedpreferences = mContext.getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        String uuid = sharedpreferences.getString("uuid", "");

        CertificatesQuery certificatesQuery = CertificatesQuery.builder().uuid(uuid).build();
        apolloClient.query(certificatesQuery).enqueue(new ApolloCall.Callback<CertificatesQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<CertificatesQuery.Data> response) {

                Log.v(TAG, response.data().toString());

                final List<CertificatesQuery.Certificate> list = response.data().certificates();

                if(list.size() > 0) {
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            certListAdaptor = new CertListAdaptor(mContext, mFragment, list);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(applicationClass);

                            certListview.setLayoutManager(layoutManager);
                            certListview.setAdapter(certListAdaptor);

                            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(certListview.getContext(),
                                    layoutManager.getOrientation());
                            certListview.addItemDecoration(dividerItemDecoration);

                            tvNoCerts.setVisibility(View.GONE);
                            certListview.setVisibility(View.VISIBLE);

                        }
                    });
                }


            }

            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.v(TAG, "onActivityResult");

        SharedPreferences sharedpreferences = mContext.getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        String uuid = sharedpreferences.getString("uuid", "");

        CertificatesQuery certificatesQuery = CertificatesQuery.builder().uuid(uuid).build();
        apolloClient.query(certificatesQuery).enqueue(new ApolloCall.Callback<CertificatesQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<CertificatesQuery.Data> response) {
                final List<CertificatesQuery.Certificate> list = response.data().certificates();

                if(list.size() > 0) {
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            certListAdaptor = new CertListAdaptor(mContext, mFragment, list);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(applicationClass);

                            certListview.setLayoutManager(layoutManager);
                            certListview.setAdapter(certListAdaptor);

                            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(certListview.getContext(),
                                    layoutManager.getOrientation());
                            certListview.addItemDecoration(dividerItemDecoration);

                            tvNoCerts.setVisibility(View.GONE);
                            certListview.setVisibility(View.VISIBLE);

                        }
                    });
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });

    }
}
