package io.taekwonblock.tkbblock;

import android.app.Application;

import com.apollographql.apollo.ApolloClient;

import okhttp3.OkHttpClient;

public class ApplicationClass extends Application {

//    public static final String GRAPH_URL = "http://10.0.2.2:4000/";
//    public static final String FILE_URL = "http://10.0.2.2:8080/";

    public static final String GRAPH_URL = "http://49.50.165.230:6640/";
    public static final String FILE_URL = "http://49.50.165.230:16640/";

    private ApolloClient apolloClient;

    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        apolloClient = ApolloClient.builder()
                .serverUrl(GRAPH_URL)
                .okHttpClient(okHttpClient)
                .build();
    }

    public ApolloClient apolloClient() {
        return apolloClient;
    }



}
