package io.taekwonblock.tkbblock.ui.dojo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import io.taekwonblock.tkbblock.DojosQuery;
import io.taekwonblock.tkbblock.R;
import io.taekwonblock.tkbblock.SearchDojoNameQuery;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class DojoFragment extends Fragment {

    private DojoViewModel dojoViewModel;
    private ApplicationClass applicationClass;
    private ApolloClient apolloClient;

    private RecyclerView recyclerView;
    private DojoListAdaptor recyclerAdaptor;
    private SearchedDojoListAdaptor searchedDojoListAdaptor;

    Context mContext;

    EditText editKeyword;
    ImageView btnSearch;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        applicationClass = (ApplicationClass)getContext().getApplicationContext();

        DojosQuery dojosQuery = DojosQuery.builder().build();

        apolloClient = applicationClass.apolloClient();

        apolloClient.query(dojosQuery).enqueue(new ApolloCall.Callback<DojosQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<DojosQuery.Data> dataResponse) {

                final StringBuffer buffer = new StringBuffer();
                final List<DojosQuery.Dojo> dojoList = dataResponse.data().dojos();


                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override public void run() {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(applicationClass);

                        recyclerAdaptor = new DojoListAdaptor(mContext, dojoList);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(recyclerAdaptor);

                    }
                });
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        });

        View root = inflater.inflate(R.layout.fragment_dojo, container, false);


        recyclerView = root.findViewById(R.id.dojo_list);

        editKeyword = root.findViewById(R.id.dojo_keyword);

        btnSearch = root.findViewById(R.id.dojo_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editKeyword.getText().toString().trim().length() > 0) {
                    SearchDojoNameQuery searchDojoNameQuery = SearchDojoNameQuery.builder().keyword(editKeyword.getText().toString()).build();
                    apolloClient.query(searchDojoNameQuery).enqueue(new ApolloCall.Callback<SearchDojoNameQuery.Data>() {
                        @Override
                        public void onResponse(@NotNull Response<SearchDojoNameQuery.Data> dataResponse) {

                            Log.v(TAG, editKeyword.getText().toString());

                            final List<SearchDojoNameQuery.SearchDojoName> dojoList = dataResponse.data().searchDojoName();


                            ((Activity)mContext).runOnUiThread(new Runnable() {
                                @Override public void run() {
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(applicationClass);

                                    searchedDojoListAdaptor = new SearchedDojoListAdaptor(mContext, dojoList);
                                    recyclerView.setLayoutManager(layoutManager);
                                    recyclerView.setAdapter(searchedDojoListAdaptor);

                                }
                            });
                        }

                        @Override
                        public void onFailure(@NotNull ApolloException e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    });
                } else {
                    DojosQuery dojosQuery = DojosQuery.builder().build();
                    apolloClient.query(dojosQuery).enqueue(new ApolloCall.Callback<DojosQuery.Data>() {
                        @Override
                        public void onResponse(@NotNull Response<DojosQuery.Data> dataResponse) {

                            final StringBuffer buffer = new StringBuffer();
                            final List<DojosQuery.Dojo> dojoList = dataResponse.data().dojos();


                            ((Activity)mContext).runOnUiThread(new Runnable() {
                                @Override public void run() {
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(applicationClass);

                                    recyclerAdaptor = new DojoListAdaptor(mContext, dojoList);
                                    recyclerView.setLayoutManager(layoutManager);
                                    recyclerView.setAdapter(recyclerAdaptor);

                                }
                            });
                        }

                        @Override
                        public void onFailure(@NotNull ApolloException e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    });

                }
            }
        });

        return root;
    }
}