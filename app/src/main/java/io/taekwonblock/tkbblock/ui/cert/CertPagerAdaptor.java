package io.taekwonblock.tkbblock.ui.cert;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class CertPagerAdaptor extends FragmentPagerAdapter {
    int mNumOfTabs;

    public CertPagerAdaptor(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);

        this.mNumOfTabs = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                CertHistoryFragment certHistoryFragment = new CertHistoryFragment();
                return certHistoryFragment;
            case 1:
                CertCertificatesFragment certCertificatesFragment = new CertCertificatesFragment();
                return certCertificatesFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
