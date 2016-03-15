package com.figli.shopingassistance;

import android.app.Activity;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by Figli on 26.02.2016.
 */
public class Ads {

    public static void showBanner(final Activity activity) {
        final AdView banner = (AdView) activity.findViewById(R.id.banner);
//        AdRequest adRequest = new AdRequest.Builder().build();
       /* AdRequest.Builder builder = new AdRequest.Builder();
        builder.addTestDevice("DC563A4A774E49FE7DBC59E5606EFCF6");*/
        AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("DC563A4A774E49FE7DBC59E5606EFCF6" + "75F7E5CE0054A9B502B9BFAFAA017D6B").build();
        banner.loadAd(adRequest);

        banner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                setupContentPadding(activity, banner.getHeight());
            }
        });
    }

    public static void setupContentPadding(Activity activity, int padding) {
        View view = activity.findViewById(R.id.coordinator);
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), padding);
    }
}
