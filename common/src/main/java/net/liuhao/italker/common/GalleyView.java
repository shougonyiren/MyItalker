package net.liuhao.italker.common;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.liuhao.italker.common.GalleyView.Image;
import net.liuhao.italker.common.widget.recycler.RecyclerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * TODO: document your custom view class.
 */
public class GalleyView extends RecyclerView {

    private static final  int LOADER_ID=0x100;
    private static final  int MAX_IMAGE_COUNT=3;//最大选中图片数量
    private static final  int MIN_IMAGE_FILE_SIZE=10*1024;//最小图片大小
    private LoaderCallback mloaderCallback=new LoaderCallback();
    private Adapter mAdapter=new Adapter();
    private List<Image>mSelectedImage=new LinkedList<>();
    private SelectedChangeListener mlistener;
    public GalleyView(Context context) {
        super(context);
        init();
    }

    public GalleyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GalleyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayoutManager(new GridLayoutManager(getContext(),4));
        setAdapter(mAdapter);
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Image>() {
            //点击
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Image image) {
                  //Cell点击操作，点击允许，就更新对应的Cell的状态
                //然后更新界面，若不允许（达到上限），那么就不刷新界面
                if(onItemSelectClick(image)){
                    holder.updateData(image);
                }
            }
        });
    }

    /**
     * 初始化方法
     * @param loaderManager Loader管理器
     * @return  返回一个LOADER_ID，可用于销毁Loader
     */
    public  int setup(LoaderManager loaderManager,SelectedChangeListener listener){
        mlistener=listener;
        loaderManager.initLoader(LOADER_ID,null,mloaderCallback);
        return LOADER_ID;
    }

    /**
     * 得到选中的图片的全部地址
     * @return 一个数组
     */
    public  String[] getSelectedPath(){
        String [] paths=new String [mSelectedImage.size()];
        int index=0;
        for(Image image:mSelectedImage){
            paths[index++]=image.path;
        }
        return  paths;
    }

    /**
     * 可以进行清空选中的图片
     */
    public void  clear(){
        for(Image image:mSelectedImage){
            //一定要先重置状态
            image.isSelect=false;
        }
        mSelectedImage.clear();
        //通知更新
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 通知Adapter数据更改方法
     * @param images 新的数据
     */
    private  void updateSource(List<Image>images){
        mAdapter.replace(images);
    }

    private  class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor>{
        private final   String[] IMAGE_PROJECTION =new String[]{
                MediaStore.Images.Media._ID,//id
                MediaStore.Images.Media.DATA,//路径
                MediaStore.Images.Media.DATE_ADDED//时间
        };
        /**
         * Instantiate and return a new Loader for the given ID.
         *
         * @param id   The ID whose loader is to be created.
         * @param args Any arguments supplied by the caller.
         * @return Return a new Loader instance that is ready to start loading.
         */
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            //创建一个Loader
            if(id==LOADER_ID){
                //如果是我们的ID则进行初始化
                return  new android.support.v4.content.CursorLoader(
                        getContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,//图片    外部URI
                        IMAGE_PROJECTION,
                        null,
                        null,
                        IMAGE_PROJECTION[2]+" DESC");//倒叙查询 必须有空格

            }
            return null;
        }

        /**
         * Called when a previously created loader has finished its load.  Note
         * that normally an application is <em>not</em> allowed to commit fragment
         * transactions while in this call, since it can happen after an
         * activity's state is saved.  See {@link FragmentManager#beginTransaction()
         * FragmentManager.openTransaction()} for further discussion on this.
         * <p>
         * <p>This function is guaranteed to be called prior to the release of
         * the last data that was supplied for this Loader.  At this point
         * you should remove all use of the old data (since it will be released
         * soon), but should not do your own release of the data since its Loader
         * owns it and will take care of that.  The Loader will take care of
         * management of its data so you don't have to.  In particular:
         * <p>
         * <ul>
         * <li> <p>The Loader will monitor for changes to the data, and report
         * them to you through new calls here.  You should not monitor the
         * data yourself.  For example, if the data is a {@link Cursor}
         * and you place it in a {@link CursorAdapter}, use
         * the {@link CursorAdapter#CursorAdapter(Context, * Cursor, int)} constructor <em>without</em> passing
         * in either {@link CursorAdapter#FLAG_AUTO_REQUERY}
         * or {@link CursorAdapter#FLAG_REGISTER_CONTENT_OBSERVER}
         * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
         * from doing its own observing of the Cursor, which is not needed since
         * when a change happens you will get a new Cursor throw another call
         * here.
         * <li> The Loader will release the data once it knows the application
         * is no longer using it.  For example, if the data is
         * a {@link Cursor} from a {@link CursorLoader},
         * you should not call close() on it yourself.  If the Cursor is being placed in a
         * {@link CursorAdapter}, you should use the
         * {@link CursorAdapter#swapCursor(Cursor)}
         * method so that the old Cursor is not closed.
         * </ul>
         *
         * @param loader The Loader that has finished.
         * @param data   The data generated by the Loader.
         */
        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
              //当Loader加载完成时
            List<Image>images=new ArrayList<>();
            //判断是否有数据
            if(data!=null){
                int count=data.getCount();
                if(count>0){
                    //移动游标到开始
                    data.moveToFirst();
                    //得到对应列的Index坐标
                    int indexId=data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]);
                    int indexPath=data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]);
                    int indexDate=data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]);
                    do{
                        //循环读取，直到没有下一条数据
                        int id=data.getInt(indexId);
                        String path= data.getString(indexPath);
                        long dateTime= data.getLong(indexDate);
                        File file=new File(path);
                        if(!file.exists()||file.length()<MIN_IMAGE_FILE_SIZE){
                            //如果没有图片，或者图片大小太小，则跳过
                            continue;
                        }
                        //添加一条新的数据
                        Image image=new Image();
                        image.id=id;
                        image.path=path;
                        image.data=dateTime;
                        images.add(image);
                    }while (data.moveToNext());
                }
            }
            updateSource(images);//为什么放在最外面
        }

        /**
         * Called when a previously created loader is being reset, and thus
         * making its data unavailable.  The application should at this point
         * remove any references it has to the Loader's data.
         *
         * @param loader The Loader that is being reset.
         */
        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            //当Loader销毁或重置了，进行界面清空
            updateSource(null);
        }
    }

    /**
     * 通知选中状态改变
     */
    private  void  notifSelectChanged(){
        //得到监听者，并判断是否有监听者，然后进行回调数量变化
        SelectedChangeListener listener=mlistener;
        if(listener!=null){
            listener.onSelectedCountChanged(mSelectedImage.size());
        }
    }
    /**
     * Cell点击的具体逻辑
     * @param image   Image
     * @return True,代表进行数据更改，要刷新
     */
    private boolean onItemSelectClick(Image image){
        //是否需要进行刷新
        boolean notifyRefresh;
        if(mSelectedImage.contains(image)){
            //如果已经被选定，就移除
            mSelectedImage.remove(image);
            image.isSelect=false;
            //状态改变需要更新
            notifyRefresh=true;
        }else {
            if(mSelectedImage.size()>=MAX_IMAGE_COUNT){
                //得到提示文字
                String str=getResources().getString(R.string.label_gallery_select_max_size);
                //格式化填充
                str=String.format(str,MAX_IMAGE_COUNT);
                Toast.makeText(getContext(),str,Toast.LENGTH_SHORT).show();
                notifyRefresh=false;
            }else {
                mSelectedImage.add(image);
                image.isSelect=true;
                notifyRefresh=true;
            }
           // image.isSelect=true;
        }
        //如果数据有更改，通知监听者数据改变了
        if(notifyRefresh){
            notifSelectChanged();
        }
        return true;
    }
    /**
     * 内部的数据结构
     */
    static  class  Image{
        int id;//Id
        String path;//路径
        long data;//图片的创建日期
        boolean isSelect;//是否选中

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Image image = (Image) o;
            return Objects.equals(path, image.path);
        }

        @Override
        public int hashCode() {

            return Objects.hash(path);
        }
    }

    /**
     * 适配器
     */
    private class Adapter extends RecyclerAdapter<Image>{

        /**
         * 得到布局的类型
         *
         * @param position 坐标
         * @param image    当前的数据
         * @return XML文件的ID，用于创建ViewHolder
         */
        @Override
        protected int getItemViewType(int position, Image image) {
            return R.layout.cell_galley;
        }

        /**
         * 得到一个新的ViewHolder
         *
         * @param root     根布局
         * @param viewType 布局类型，其实就是XML的ID
         * @return ViewHolder
         */
        @Override
        protected ViewHolder<Image> onCreateViewHolder(View root, int viewType) {
            return new GalleyView.ViewHolder(root);
        }

    }

    /**
     * Cell 对应的Holder
     */
    private  class ViewHolder extends RecyclerAdapter.ViewHolder<Image>{
        private ImageView mPIC;
        private View mShade;
        private CheckBox mSelected;

        public ViewHolder(View itemView) {
            super(itemView);
            mPIC=(ImageView)itemView.findViewById(R.id.im_image);
            mShade=itemView.findViewById(R.id.view_shade);
            mSelected=(CheckBox) itemView.findViewById(R.id.cb_select);

        }

        /**
         * 当触发绑定数据的时候的回掉；必须复写
         *
         * @param image 绑定的数据
         */
        @Override
        protected void onBind(Image image) {
            Glide.with(getContext())
                    .load(image.path)  //加载路径
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//不使用缓存，直接从原图加载
                    .centerCrop() //居中剪切
                    .placeholder(R.color.green_200)//默认图片颜色 占位符
                    .into(mPIC);
            mShade.setVisibility(image.isSelect?VISIBLE:INVISIBLE);
            mSelected.setChecked(image.isSelect);//根据是否选中
            mSelected.setVisibility(VISIBLE);
        }
    }

    /**
     * 对外监听器
     */
    public interface  SelectedChangeListener{
        void  onSelectedCountChanged(int count);
    }
}











