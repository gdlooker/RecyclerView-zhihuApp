package com.example.com.zhuangbi.base;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2017/2/21.
 */

public class BaseFragment extends Fragment {

    public static final String TAG="chenzb";
    public Context context=null;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

}
