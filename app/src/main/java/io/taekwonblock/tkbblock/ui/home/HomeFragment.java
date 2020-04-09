package io.taekwonblock.tkbblock.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.taekwonblock.tkbblock.ApplicationClass;
import io.taekwonblock.tkbblock.JoinedDojoQuery;
import io.taekwonblock.tkbblock.R;
import io.taekwonblock.tkbblock.RegistratedDojoQuery;
import io.taekwonblock.tkbblock.ui.dojo.DojoListAdaptor;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private HomeViewModel homeViewModel;
    String username = "username";

    private ApplicationClass applicationClass;
    private ApolloClient apolloClient;

    TextView tvNoDojos;
    RecyclerView regDojoList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        SharedPreferences sharedpreferences = getContext().getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);

        username = sharedpreferences.getString("name", "");
        String uuid = sharedpreferences.getString("uuid", "");
        final int accountType = sharedpreferences.getInt("type", 0);

        Log.v(TAG, Integer.toString(accountType) );

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView textView = root.findViewById(R.id.home_tv_username);
        textView.setText(username);

        tvNoDojos = root.findViewById(R.id.home_no_dojo);

        regDojoList = root.findViewById(R.id.home_dojo_list);

        applicationClass = (ApplicationClass)getContext().getApplicationContext();
        apolloClient = applicationClass.apolloClient();

        if(accountType == 1) {
            RegistratedDojoQuery registratedDojoQuery = RegistratedDojoQuery.builder().mobile_user_uuid(uuid).build();
            apolloClient.query(registratedDojoQuery).enqueue(new ApolloCall.Callback<RegistratedDojoQuery.Data>() {
                @Override
                public void onResponse(@NotNull Response<RegistratedDojoQuery.Data> response) {
                    Log.v(TAG, response.data().toString());

                    final List<RegistratedDojoQuery.RegistratedDojo> regDojo = response.data().registratedDojo();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(regDojo == null || regDojo.size() <= 0 || regDojo.isEmpty()) {
                                tvNoDojos.setVisibility(View.VISIBLE);
                                regDojoList.setVisibility(View.INVISIBLE);

                                tvNoDojos.setText("현재 등록중인 도장이 없습니다");
                            } else {
                                tvNoDojos.setVisibility(View.INVISIBLE);
                                regDojoList.setVisibility(View.VISIBLE);

                                LinearLayoutManager layoutManager = new LinearLayoutManager(applicationClass);

                                RegisteredDojoListAdaptor registeredDojoListAdaptor = new RegisteredDojoListAdaptor(getActivity(), regDojo, accountType);
                                regDojoList.setLayoutManager(layoutManager);
                                regDojoList.setAdapter(registeredDojoListAdaptor);
                            }
                        }
                    });
                }
                @Override
                public void onFailure(@NotNull ApolloException e) {

                    Toast.makeText(applicationClass, "서버와의 접속이 원활하지 않습니다. 잠시 후 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            });

        } else if( accountType == 2) {
            JoinedDojoQuery joinedDojoQuery = JoinedDojoQuery.builder().mobile_user_uuid(uuid).build();
            apolloClient.query(joinedDojoQuery).enqueue(new ApolloCall.Callback<JoinedDojoQuery.Data>() {
                @Override
                public void onResponse(@NotNull Response<JoinedDojoQuery.Data> response) {
                    Log.v(TAG, response.data().toString());

                    final List<JoinedDojoQuery.JoinedDojo> regDojo = response.data().joinedDojo();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(regDojo == null || regDojo.size() <= 0 || regDojo.isEmpty()) {
                                tvNoDojos.setVisibility(View.VISIBLE);
                                regDojoList.setVisibility(View.INVISIBLE);

                                tvNoDojos.setText("현재 지도중인 도장이 없습니다");
                            } else {
                                tvNoDojos.setVisibility(View.INVISIBLE);
                                regDojoList.setVisibility(View.VISIBLE);

                                LinearLayoutManager layoutManager = new LinearLayoutManager(applicationClass);

                                JoinedDojoListAdaptor registeredDojoListAdaptor = new JoinedDojoListAdaptor(getActivity(), regDojo, accountType);
                                regDojoList.setLayoutManager(layoutManager);
                                regDojoList.setAdapter(registeredDojoListAdaptor);
                            }
                        }
                    });
                }
                @Override
                public void onFailure(@NotNull ApolloException e) {
                    SharedPreferences sharedpreferences = getContext().getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.clear();
                    editor.commit();

                    e.printStackTrace();
                }
            });

        }

        return root;
    }
}