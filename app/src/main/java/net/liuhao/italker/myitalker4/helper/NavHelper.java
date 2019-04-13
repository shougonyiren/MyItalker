package net.liuhao.italker.myitalker4.helper;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.util.SparseIntArray;

import net.liuhao.italker.common.app.Fragment;

/**
 * 解决对Fragment的调度与重用问题
 * 达到最优的Fragment的切换
 * Created by hasee on 2019-04-11.
 */
public class NavHelper <T>{
    //所有的Tab集合
    private final SparseArray<Tab<T>> tabs=new SparseArray();
    //用于初始化的必须参数
    private  final Context context;
    private  final int containerId;
    private final FragmentManager fragmentManager;
    private final OnTabChangedListener<T> listener;

    //当前一个选中的Tab
    private Tab<T> currentTab;

    public NavHelper( Context context,FragmentManager fragmentManager, int containerId, OnTabChangedListener<T> listener) {
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
        this.context = context;
        this.listener = listener;
    }

    /**
     * 添加Tab
     * @param menuId Tab对应的菜单Id
     * @param tab
     */
    public NavHelper<T>  add(int menuId,Tab<T>tab){
        tabs.put(menuId,tab);
        return this;
    }

    /**
     * 获取当前显示的Tab
     * @return 当前的Tab
     */
    public Tab<T> getCurrentTab() {
        return currentTab;
    }

    /**
     * 执行点击菜单的操作
     * @param menuId  菜单的Id
     * @return  是否能够处理这个点击
     */
    public  boolean performClickMenu(int menuId){
        //集合中寻找点击的菜单对应的Tab,
        //如果有则进行处理
        Tab<T> tab =tabs.get(menuId);
        if(tab!=null){
            doSelect(tab);
            return  true;
        }
          return false;
    }

    /**
     * 进行真实的选择操作
     * @param tab Tab
     */
    private void doSelect(Tab<T> tab) {
        Tab<T> oldTab=null;
        if(currentTab!=null){
            oldTab=currentTab;
            if(oldTab==tab){//重复点击 刷新
                notifyReslect(tab);
                return;
            }
        }
    }
    private  void  doTabChanged(Tab<T> newTab,Tab<T> oldTab){
        FragmentTransaction ft=fragmentManager.beginTransaction();
        if(oldTab!=null){
            if(oldTab.fragment!=null){
                //从界面移除，但是还在Fragment的缓存空间中
                ft.detach(oldTab.fragment);
            }
        }
    }
    private void notifyReslect(Tab<T> tab) {
       //TODO 二次点击Tab所做的操作

    }

    /**
     * 我们所有的Tab基础属性
     * @param <T>
     */
    public static class Tab<T>{
        //Ftagment对应的Class信息
        public Class<? > clx;
        //额外的字段，用户自己设定需要使用
        public T extra;
        //内部缓存对应的Fragment;
        //Package权限，外部无法使用
         Fragment fragment;
    }

    /**
     * 定义事件处理完成后的回调接口
     * @param <T>
     */
    public interface OnTabChangedListener<T>{
        void onTabChanged(Tab<T> newTab,Tab oldTab);

    }
}







