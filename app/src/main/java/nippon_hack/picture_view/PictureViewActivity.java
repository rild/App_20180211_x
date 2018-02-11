package nippon_hack.picture_view;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


public class PictureViewActivity extends AppCompatActivity {

    //----------------------------------------------
    //基本処理
    //----------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_view);

        //関連付けを行う
        ViewPager viewPagerProgram = findViewById(R.id.viewPager);

        //チュートリアル画像を設定
        int[] imageList = { R.drawable.cat01,
                            R.drawable.cat02,};

        //viewPagerにセットする
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getFragmentManager(), imageList);
        viewPagerProgram.setAdapter(pagerAdapter);

    }
}
