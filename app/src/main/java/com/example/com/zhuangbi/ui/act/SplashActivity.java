package com.example.com.zhuangbi.ui.act;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.bumptech.glide.Glide;
import com.example.com.zhuangbi.R;
import com.example.com.zhuangbi.base.BaseActivity;
import com.example.com.zhuangbi.imp.Constant;
import com.example.com.zhuangbi.utils.HttpUtils;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutionException;



public class SplashActivity extends BaseActivity {

    private SimpleDraweeView simpleDraweeView = null;

    DraweeController draweeController = null;

    private Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        simpleDraweeView = (SimpleDraweeView) this.findViewById(R.id.ivStart);
        try {
            initImage();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化图片
     */
    private void initImage() throws FileNotFoundException {
        File fileDir = getFilesDir();//获取文件目录
        //在这个目录下放置 一张图片叫做什么名字的图片
        final File imgFile = new File(fileDir, "start.jpg");//
        //第三个===判断这个文件下面是否有图片
        if (imgFile.exists()) {

           /* FileInputStream fis=new FileInputStream(imgFile);
            draweeController = (DraweeController) Fresco.newDraweeControllerBuilder()

                    .setUri(Uri.parse("res://zhihu.example.com.zhihuapp"+R.mipmap.ic_launcher));
            ;
            simpleDraweeView.setController(draweeController);*/
            FileInputStream fileInputStream = new FileInputStream(imgFile);
            Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);//讲输入流转化为位图对象
            Bitmap bitmap1 = BitmapFactory.decodeFile(imgFile.getAbsolutePath());//将文件转化为位图对象
            simpleDraweeView.setImageBitmap(bitmap);//设置位图对象

        }

        //动画教程 http://www.jb51.net/article/32340.htm
        final ScaleAnimation scaleAnim = new ScaleAnimation(0.0f, 1.4f, 0.0f, 1.4f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(3000);
        scaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //判断网络
                if (HttpUtils.isNetworkConnected(context)) {
                    //发送okhttp请求
                    //开启子线程jia加载网络图片到本地
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Bitmap bitmap = Glide.with(getApplicationContext()).load(Constant.START_IMG).asBitmap().into(300, 300).get();
                              //ImagePipelineFactory imagePipelineFactory=Fresco.getImagePipelineFactory();
                                //将图片保存到本地
                                if (bitmap != null) {
                                    FileOutputStream fos = new FileOutputStream(imgFile);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                }
                                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //开启动画
        simpleDraweeView.startAnimation(scaleAnim);


    }


}
