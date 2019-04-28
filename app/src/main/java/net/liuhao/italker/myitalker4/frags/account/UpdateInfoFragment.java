package net.liuhao.italker.myitalker4.frags.account;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.liuhao.italker.common.app.Fragment;
import net.liuhao.italker.common.widget.PortraitView;
import net.liuhao.italker.myitalker4.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpdateInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpdateInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateInfoFragment extends Fragment {
    @BindView(R.id.im_portrait)
    PortraitView mPortrait;
    public UpdateInfoFragment() {
        // Required empty public constructor
    }


    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_info;
    }
    @OnClick(R.id.im_portrait)
    void onPortraitClick(){

    }

}
