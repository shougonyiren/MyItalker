package net.liuhao.italker.myitalker4.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import net.liuhao.italker.common.app.Activity;
import net.liuhao.italker.myitalker4.R;
import net.liuhao.italker.myitalker4.frags.account.UpdateInfoFragment;

/**
 * Created by hasee on 2019-04-27.
 */
public class AcountActivity  extends Activity{
    /**
     * 账户Activity显示的入口
     * @param context
     */
    public  static void show(Context context){
        context.startActivity(new Intent(context,AcountActivity.class));
    }
    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initWidget() {
        super.initWidget();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container,new UpdateInfoFragment())
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
