package net.liuhao.italker.myitalker4.frags.main;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.liuhao.italker.myitalker4.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveFragment extends net.liuhao.italker.common.app.Fragment {


    public ActiveFragment() {
        // Required empty public constructor
    }
    

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

}
