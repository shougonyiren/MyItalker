package net.liuhao.italker.myitalker4;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import net.liuhao.italker.common.app.Activity;
import net.liuhao.italker.common.widget.recycler.PortraitView;
import net.liuhao.italker.myitalker4.frags.main.ActiveFragment;
import net.liuhao.italker.myitalker4.frags.main.ContactFragment;
import net.liuhao.italker.myitalker4.frags.main.GroupFragment;
import net.liuhao.italker.myitalker4.helper.NavHelper;
import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends Activity implements BottomNavigationView.OnNavigationItemSelectedListener, NavHelper.OnTabChangedListener<Integer> {

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


    private NavHelper<Integer> mNavhelper;

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

        //初始化底部工具类
        mNavhelper = new NavHelper<>(this, R.id.lay_container, getSupportFragmentManager(), this);
        mNavhelper.add(R.id.action_home, new NavHelper.Tab<>(ActiveFragment.class, R.string.title_home))
                .add(R.id.action_group, new NavHelper.Tab<>(GroupFragment.class, R.string.title_group))
                .add(R.id.action_contact, new NavHelper.Tab<>(ContactFragment.class, R.string.title_contact));
        //添加对底部按钮点击监听
        mNavigation.setOnNavigationItemSelectedListener(this);
        Glide.with(this).load(R.drawable.bg_src_morning)
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
        //从底部导航中接管Menu,然后进行手动的触发第一次点击
        Menu menu=mNavigation.getMenu();
        //执行与给定菜单标识符关联的菜单项操作。
        //参数标志附加选项标志或0。
        //触发首次选中Home
        menu.performIdentifierAction(R.id.action_home,0);
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
    // boolean
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //转接事件流到工具类中
        return mNavhelper.performClickMenu(item.getItemId());
    }

    /**
     * Called when pointer capture is enabled or disabled for the current window.
     *
     * @param hasCapture True if the window has pointer capture.
     */
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    /**
     * NavHelper 处理后回调的方法
     *
     * @param newTab 新的Tab
     * @param oldTab 旧的Tab
     */
    @Override
    public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab oldTab) {
        //从额外字段中取出我们的Title资源Id
        mTitle.setText(newTab.extra);
        //对浮动按钮进行隐藏和显示的动画
        float transY=0;
        float rotation=0;
        if(Objects.equals( newTab.extra,R.string.title_home)){
            //主界面时隐藏
            transY= Ui.dipToPx(getResources(),76);
        }else {
            //transY默认为0 则显示
            if(Objects.equals( newTab.extra,R.string.title_group)){
                //群
                mAction.setImageResource(R.drawable.ic_group_add);
                rotation=-360;
            }else {
                //联系人
                mAction.setImageResource(R.drawable.ic_contact_add);
                rotation=360;
            }

        }
        //开始动画
        //旋转，Y轴位移，弹性差值器，时间
        mAction.animate()
                .rotation(rotation)
                .translationY(transY)
                .setInterpolator(new AnticipateInterpolator(1))
                .setDuration(500)
                .start();

    }
}
