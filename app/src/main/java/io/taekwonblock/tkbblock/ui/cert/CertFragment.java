package io.taekwonblock.tkbblock.ui.cert;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import io.taekwonblock.tkbblock.R;

public class CertFragment extends Fragment {

    private CertViewModel certViewModel;
    TabLayout tabLayout;
    ViewPager viewPager;
    CertPagerAdaptor adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        adapter = new CertPagerAdaptor(getChildFragmentManager(), 2);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_cert, container, false);

        viewPager = root.findViewById(R.id.cert_viewpager);

        tabLayout = root.findViewById(R.id.cert_tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("이력 조회"));
        tabLayout.addTab(tabLayout.newTab().setText("자격증"));

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.v("POSITION", tab.getPosition() + "");
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return root;
    }

}