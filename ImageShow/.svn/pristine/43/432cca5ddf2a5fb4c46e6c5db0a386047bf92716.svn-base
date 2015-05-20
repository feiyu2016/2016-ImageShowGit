/*
 * Copyright (C), 2014-2015, 联创车盟汽车服务有限公司
 * FileName: MyHomeActivity.java
 * Author:   wuhq
 * Date:     2015年1月28日 上午9:42:05
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.suo.image.activity.my;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.suo.demo.R;
import com.suo.image.ImageApp;
import com.suo.image.activity.BaseActivity;
import com.suo.image.activity.HotContentActivity;
import com.suo.image.activity.Main_new;
import com.suo.image.adapter.ArrayListAdapter;
import com.suo.image.adapter.ViewHolder;
import com.suo.image.bean.ContentBean;
import com.suo.image.bean.UserInfo;
import com.suo.image.util.Density;
import com.suo.image.util.ImageLoaderUtil;
import com.suo.image.util.Preference;
import com.suo.image.view.RoundImageView;

/**
 * 〈一句话功能简述〉<br> 
 * 〈功能详细描述〉
 *
 * @author wuhq
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class MyHomeActivity extends BaseActivity {

    private Button btn_back;
    private Button btn_logout;
    private TextView tv_uploads;
    private TextView tv_likes;
    private TextView tv_nickname;
    private RoundImageView iv_head;
    private ViewPager mPager;// 页卡内容
    private List<View> listViews; // Tab页面列表
    private int curPositon = 0;
    
    private ImageView cursor;// 动画图片
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    
    private UploadAdapter adapter;
    private LikeAdapter adapter2;
    private UserInfo user;
    private int uploadSize = 0;
    private int likeSize = 0;
    private String type;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_home);
        
        initLayout();
        prepareData();
        
    }
    
    private void initLayout(){
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        tv_uploads = (TextView) findViewById(R.id.tv_uploads);
        tv_likes = (TextView) findViewById(R.id.tv_likes);
        iv_head = (RoundImageView) findViewById(R.id.iv_head);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        btn_back.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
        tv_uploads.setOnClickListener(this);
        tv_likes.setOnClickListener(this);
        iv_head.setOnClickListener(this);
        
    }
    
    private void prepareData() {
        adapter = new UploadAdapter(this);
        adapter2 = new LikeAdapter(this);
        
        type = getIntent().getStringExtra("type");
        userId = getIntent().getStringExtra("userId");
        if (!TextUtils.isEmpty(type) && type.equals("others")){
            btn_logout.setVisibility(View.GONE);
            tv_uploads.setText("上传");
            tv_likes.setText("收藏");
            BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
            query.addWhereEqualTo("userId", userId);
            query.findObjects(MyHomeActivity.this, new FindListener<UserInfo>() {
                    @Override
                    public void onSuccess(List<UserInfo> object) {
                        if (object!=null && object.size()>0){
                            user = object.get(0);
                            tv_nickname.setText(""+object.get(0).getNickname());
                            ImageLoaderUtil.getInstance().displayImage(object.get(0).getHeadPhoto(), iv_head, R.drawable.default_headphoto);
                            if (user !=null ){
                                if (user.getUploadIds()!=null && user.getUploadIds().size()>0){
                                    uploadSize = user.getUploadIds().size();
                                }
                                if (user.getLikeIds()!=null && user.getLikeIds().size()>0){
                                    likeSize = user.getLikeIds().size();
                                }
                            }
                            InitImageView();
                            InitViewPager();
                        }
                    }
                    @Override
                    public void onError(int code, String msg) {
                        
                    }
            });
        }else{
            user = ImageApp.getInstance().getUserinfo();
            if (user !=null ){
                if (user.getUploadIds()!=null && user.getUploadIds().size()>0){
                    uploadSize = user.getUploadIds().size();
                }
                if (user.getLikeIds()!=null && user.getLikeIds().size()>0){
                    likeSize = user.getLikeIds().size();
                }
            }
            if (!TextUtils.isEmpty(user.getHeadPhoto())){
                ImageLoaderUtil.getInstance().displayImage(user.getHeadPhoto(), iv_head, R.drawable.icon);
            }
            tv_nickname.setText(""+user.getNickname());
            InitImageView();
            InitViewPager();
        }
    }

    /**
     * 初始化动画
     */
    private void InitImageView() {
        cursor = (ImageView) findViewById(R.id.cursor);
        cursor.setLayoutParams(new LinearLayout.LayoutParams(Density.getSceenWidth(this) / 2, LayoutParams.WRAP_CONTENT));
        bmpW = BitmapFactory.decodeResource(getResources(),
                R.drawable.my_home_line).getWidth();// 获取图片宽度
        offset = (Density.getSceenWidth(this) / 2 - bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);// 设置动画初始位置
    }

    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.vPager);
        listViews = new ArrayList<View>();
        LayoutInflater mInflater = getLayoutInflater();
        listViews.add(mInflater.inflate(R.layout.my_viewpager_upload, null));
        listViews.add(mInflater.inflate(R.layout.my_viewpager_like, null));
        mPager.setAdapter(new MyPagerAdapter(listViews));
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mPager.setCurrentItem(0);
    }
    
    /**
     * ViewPager适配器
     */
    public class MyPagerAdapter extends PagerAdapter {
        public List<View> mListViews;
        public int mCount;

        public MyPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
            mCount = mListViews.size();
        }

        public void destroyItem(View collection, int position, Object arg2) {
            ((ViewPager) collection).removeView(mListViews.get(position));
        }

        public void finishUpdate(View arg0) {
        }

        public int getCount() {
            return mListViews.size();
        }

        public Object instantiateItem(View collection, int position) {
            try {
                ((ViewPager) collection).addView(mListViews.get(position), 0);
            } catch (Exception e) {
            }
            
            if (position == 0){
                GridView gv_pic = (GridView)collection.findViewById(R.id.gv_pic);
                
                if (user!=null && user.getUploadIds()!= null && user.getUploadIds().size()>0){
                    if (uploadSize >=4){
                        gv_pic.setNumColumns(4);
                    }else{
                        gv_pic.setNumColumns(uploadSize);
                    }
                    
                    adapter.setList(user.getUploadIds());
                    gv_pic.setAdapter(adapter);
                }
            }else if (position == 1){
                GridView gv_pic = (GridView)collection.findViewById(R.id.gv_pic);
                
                if (user!=null && user.getLikeIds()!= null && user.getLikeIds().size()>0){
                    if (likeSize >=4){
                        gv_pic.setNumColumns(4);
                    }else{
                        gv_pic.setNumColumns(likeSize);
                    }
                    
                    adapter2.setList(user.getLikeIds());
                    gv_pic.setAdapter(adapter2);
                }
            }
            
            

            return mListViews.get(position);
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        public Parcelable saveState() {
            return null;
        }

        public void startUpdate(View arg0) {
        }
    }

    /**
     * 页卡切换监听
     */
    public class MyOnPageChangeListener implements OnPageChangeListener {

        public void onPageSelected(int arg0) {
            int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
            int two = one * 2;// 页卡1 -> 页卡3 偏移量
            Animation animation = null;

            switch (arg0) {
            case 0:
                curPositon = 0;
                if (currIndex == 1) {
                    animation = new TranslateAnimation(one, 0, 0, 0);
                } 
                break;
            case 1:
                curPositon = 1;
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, one, 0, 0);
                } 
                break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);

        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageScrollStateChanged(int arg0) {
        }
    }
    
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_uploads:
                mPager.setCurrentItem(0);
                break;
            case R.id.tv_likes:
                mPager.setCurrentItem(1);
                break;
            case R.id.btn_logout:
                new AlertDialog.Builder(this).setTitle("提示").setMessage("确定要注销吗?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        BmobUser.logOut(MyHomeActivity.this);
                        ImageApp.getInstance().setUserinfo(null);
                        Preference.putString("password", "");
                        Preference.putString("about", "");
                        Preference.putString("token", "");
                        Preference.putString("expires", "");
                        Preference.putString("openId", "");
                        Preference.putString("qqinfo", "");
                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                
                break;
            default:
                break;
        }
    }

    class UploadAdapter extends ArrayListAdapter<ContentBean> {

        private BaseActivity context;

        public UploadAdapter(BaseActivity context) {
            super(context);
            this.context = context;
        }

        public UploadAdapter(BaseActivity context, ListView listView) {
            super(context, listView);
            this.context = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.my_upload_item, parent, false);
            }

            ImageView image_grid = ViewHolder.get(convertView, R.id.image_grid);

            if (uploadSize>=4){
                int width = Density.getSceenWidth(MyHomeActivity.this)/4;
                image_grid.setLayoutParams(new LinearLayout.LayoutParams(width, width));
            }else{
                int width = Density.getSceenWidth(MyHomeActivity.this)/uploadSize;
                image_grid.setLayoutParams(new LinearLayout.LayoutParams(width, width));
            }
            
            final ContentBean bean = (ContentBean) getItem(position);
            ImageLoaderUtil.getInstance().displayImage(bean.getContentUrl(), image_grid, R.drawable.default_image);
//            fb.display(image_grid, bean.getContentUrl());
            
            image_grid.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyHomeActivity.this, HotContentActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("contentBean", bean);
                    intent.putExtra("contentBeanList", user.getUploadIds());
                    intent.putExtra("type", "myUploads");
                    launch(intent);
                }
            });
            return convertView;
        }

    }
    
    class LikeAdapter extends ArrayListAdapter<ContentBean> {

        private BaseActivity context;

        public LikeAdapter(BaseActivity context) {
            super(context);
            this.context = context;
        }

        public LikeAdapter(BaseActivity context, ListView listView) {
            super(context, listView);
            this.context = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.my_upload_item, parent, false);
            }

            ImageView image_grid = ViewHolder.get(convertView, R.id.image_grid);

            if (likeSize>=4){
                int width = Density.getSceenWidth(MyHomeActivity.this)/4;
                image_grid.setLayoutParams(new LinearLayout.LayoutParams(width, width));
            }else{
                int width = Density.getSceenWidth(MyHomeActivity.this)/likeSize;
                image_grid.setLayoutParams(new LinearLayout.LayoutParams(width, width));
            }
            
            final ContentBean bean = (ContentBean) getItem(position);
            ImageLoaderUtil.getInstance().displayImage(bean.getContentUrl(), image_grid, R.drawable.default_image);
            
            image_grid.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyHomeActivity.this, HotContentActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("contentBean", bean);
                    intent.putExtra("contentBeanList", user.getLikeIds());
                    intent.putExtra("type", "myLikes");
                    launch(intent);
                }
            });
            return convertView;
        }

    }
    
}
