package com.suo.image.activity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.FindListener;

import com.suo.demo.R;
import com.suo.image.ImageApp;
import com.suo.image.bean.UserInfo;
import com.suo.image.util.Log;
import com.suo.image.util.Preference;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

public class SplashActivity extends BaseActivity {

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        username = Preference.getString("username");
        password = Preference.getString("password");

        if (BmobUser.getCurrentUser(this) != null) {
            BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
            query.addWhereEqualTo("userId", BmobUser.getCurrentUser(this).getObjectId());
            query.findObjects(this, new FindListener<UserInfo>() {
                    @Override
                    public void onSuccess(List<UserInfo> object) {
                        if (object!=null && object.size()>0){
                            Log.e("查询到用户信息数据");
                            ImageApp.getInstance().setUserinfo(object.get(0));
                            launch(Main_new.class);
                            finish();
                        }
                    }
                    @Override
                    public void onError(int code, String msg) {
                        Log.e("BmobUser.getCurrentUser(this)不为空，但是查询用户信息数据失败后进入主页面");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                launch(Main_new.class);
                                finish();
                            }
                        }, 2000);
                    }
            });
        } else {
            if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                final BmobUser user = new BmobUser();
                user.setUsername(username);
                user.setPassword(password);
                user.login(this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Log.e("登录成功");
                        BmobUser bmobuser = BmobUser.getCurrentUser(SplashActivity.this);
                        if (bmobuser != null) {
                            BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
                            query.addWhereEqualTo("userId", user.getObjectId());
                            query.findObjects(SplashActivity.this, new FindListener<UserInfo>() {
                                @Override
                                public void onSuccess(List<UserInfo> object) {
                                    if (object != null && object.size() > 0) {
                                        Log.e("登录成功后，查询到用户信息数据");
                                        ImageApp.getInstance().setUserinfo(object.get(0));
                                        launch(Main_new.class);
                                        finish();
                                    }
                                }

                                @Override
                                public void onError(int code, String msg) {
                                    Log.e("登录成功，获取用户信息失败后进入主界面");
                                    launch(Main_new.class);
                                    finish();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(int arg0, String arg1) {
                        Log.e("登录失败后进入主界面");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                launch(Main_new.class);
                                finish();
                            }
                        }, 2000);
                    }
                });
            } else {
                Log.e("没有username和password的用户直接进入主页");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        launch(Main_new.class);
                        finish();
                    }
                }, 2000);
            }
        }

    }

}
