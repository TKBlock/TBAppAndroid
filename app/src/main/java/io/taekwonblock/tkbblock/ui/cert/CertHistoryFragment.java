package io.taekwonblock.tkbblock.ui.cert;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.taekwonblock.tkbblock.ApplicationClass;
import io.taekwonblock.tkbblock.HistoriesForStudentQuery;
import io.taekwonblock.tkbblock.HistoriesForInstructorQuery;
import io.taekwonblock.tkbblock.R;
import io.taekwonblock.tkbblock.model.HistoryModel;

public class CertHistoryFragment extends Fragment {

    final static String TAG = "CertHistoryFragment";

    private ApplicationClass applicationClass;
    private ApolloClient apolloClient;

    TextView historyEmpty;
    RecyclerView historyList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences sharedpreferences = getContext().getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        String uuid = sharedpreferences.getString("uuid", "");
        int accountType = sharedpreferences.getInt("type", 0);
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_cert_history, container, false);

        applicationClass = (ApplicationClass)getContext().getApplicationContext();
        apolloClient = applicationClass.apolloClient();

        historyEmpty = root.findViewById(R.id.cert_history_empty);

        historyList = root.findViewById(R.id.cert_history_list);

        if(accountType == 1) {
            HistoriesForStudentQuery historiesForStudentQuery = HistoriesForStudentQuery.builder().user_uuid(uuid).build();
            apolloClient.query(historiesForStudentQuery).enqueue(new ApolloCall.Callback<HistoriesForStudentQuery.Data>() {
                @Override
                public void onResponse(@NotNull Response<HistoriesForStudentQuery.Data> response) {

                    final List<HistoriesForStudentQuery.HistoriesForStudent> histories = response.data().historiesForStudent();

                    final List<HistoryModel> modelList = new ArrayList<>();

                    for(int i = 0; i < histories.size(); i++) {
                       HistoryModel model = new HistoryModel( histories.get(i) );
                       modelList.add(model);
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if(histories.size() > 0) {
                                HistoryListAdaptor historyListAdaptor = new HistoryListAdaptor(applicationClass, modelList);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(applicationClass);

                                historyList.setLayoutManager(layoutManager);
                                historyList.setAdapter(historyListAdaptor);

                                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                                        layoutManager.getOrientation());
                                historyList.addItemDecoration(dividerItemDecoration);


                                historyEmpty.setVisibility(View.GONE);
                                historyList.setVisibility(View.VISIBLE);
                            } else {
                                historyEmpty.setVisibility(View.VISIBLE);
                                historyEmpty.setText("수강 이력이 없습니다");
                                historyList.setVisibility(View.GONE);
                            }

                        }
                    });
                }

                @Override
                public void onFailure(@NotNull ApolloException e) {
                    e.printStackTrace();
                }
            });

        } else if(accountType == 2) {
            HistoriesForInstructorQuery historiesForStudentQuery = HistoriesForInstructorQuery.builder().user_uuid(uuid).build();
            apolloClient.query(historiesForStudentQuery).enqueue(new ApolloCall.Callback<HistoriesForInstructorQuery.Data>() {
                @Override
                public void onResponse(@NotNull Response<HistoriesForInstructorQuery.Data> response) {

                    final List<HistoriesForInstructorQuery.HistoriesForInstructor> histories = response.data().historiesForInstructor();

                    final List<HistoryModel> modelList = new ArrayList<>();

                    for(int i = 0; i < histories.size(); i++) {
                        modelList.add(new HistoryModel( histories.get(i) ));
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if(histories.size() > 0) {
                                HistoryListAdaptor historyListAdaptor = new HistoryListAdaptor(applicationClass, modelList);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(applicationClass);

                                historyList.setLayoutManager(layoutManager);
                                historyList.setAdapter(historyListAdaptor);

                                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                                        layoutManager.getOrientation());
                                historyList.addItemDecoration(dividerItemDecoration);


                                historyEmpty.setVisibility(View.GONE);
                                historyList.setVisibility(View.VISIBLE);
                            } else {
                                historyEmpty.setVisibility(View.VISIBLE);
                                historyEmpty.setText("지도 이력이 없습니다");
                                historyList.setVisibility(View.GONE);
                            }

                        }
                    });
                }

                @Override
                public void onFailure(@NotNull ApolloException e) {
                    e.printStackTrace();
                }
            });
        }

        return root;

    }
}
