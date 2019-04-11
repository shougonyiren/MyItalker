package net.liuhao.italker.myitalker4;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import net.liuhao.italker.common.app.Activity;
import net.liuhao.italker.common.widget.recycler.PortraitView;
import net.liuhao.italker.myitalker4.frags.main.ActiveFragment;
import net.liuhao.italker.myitalker4.frags.main.GroupFragment;
import net.qiujuer.genius.ui.widget.FloatActionButton;
import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends Activity implements  BottomNavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.appbar)
    View mLayAppbar;

    @BindView(R.id.im_portrait)
    PortraitView mPortrait;

    @BindView(R.id.txt_title)
    TextView mTitle;

    @BindView(R.id.lay_container)
    FrameLayout mContainer;

    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    @BindView(R.id.btn_action)
    FloatActionButton mAction;


    /**
     * MainActivity 显示的入口
     *
     * @param context 上下文
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mNavigation.setOnNavigationItemSelectedListener(this);
        Glide.with(this)
                .load(R.drawable.bg_src_morning)
                .centerCrop()
                .into(new ViewTarget<View, GlideDrawable>(mLayAppbar) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                });
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @OnClick(R.id.im_search)
    void onSearchMenuClick() {

    }


    /**
     * Called when an item in the bottom navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item and false if the item should not
     * be selected. Consider setting non-selectable items as disabled preemptively to
     * make them appear non-interactive.
     */
    boolean
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_home:{
                mTitle.setText(R.string.title_home);
                ActiveFragment activeFragment =new ActiveFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.lay_container,activeFragment)
                        .commit();
                break;
            }
            case R.id.action_group:{
                mTitle.setText(R.string.title_home);
                ActiveFragment activeFragment =new ActiveFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.lay_container,activeFragment)
                        .commit();
                break;
            }

        }
        mTitle.setText(item.getTitle());
        return true;//返回是否处理 true表示已处理
    }

    /**
     * Called when pointer capture is enabled or disabled for the current window.
     *
     * @param hasCapture True if the window has pointer capture.
     */
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
