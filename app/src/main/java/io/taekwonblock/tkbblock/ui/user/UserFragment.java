package io.taekwonblock.tkbblock.ui.user;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.taekwonblock.tkbblock.ApplicationClass;
import io.taekwonblock.tkbblock.R;

public class UserFragment extends Fragment {
    RecyclerView menuRecyclerView;
    private ApplicationClass applicationClass;
    Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user, container, false);

        applicationClass = (ApplicationClass)getContext().getApplicationContext();
        menuRecyclerView = root.findViewById(R.id.user_menulist);

        List<String> menuList = new ArrayList<>();
        menuList.add("개인정보 수정");
        menuList.add("로그아웃");

        UserMenuListAdaptor userMenuListAdaptor = new UserMenuListAdaptor(mContext, menuList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(applicationClass);

        menuRecyclerView.setLayoutManager(layoutManager);
        menuRecyclerView.setAdapter(userMenuListAdaptor);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(menuRecyclerView.getContext(),
                layoutManager.getOrientation());
        menuRecyclerView.addItemDecoration(dividerItemDecoration);


        return root;
    }

}
