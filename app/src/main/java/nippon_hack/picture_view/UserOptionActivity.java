package nippon_hack.picture_view;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class UserOptionActivity extends AppCompatActivity {
    TextView editTextUserNameProgram;
    RadioGroup radioGroupHeightProgram;

    String gender;

    //----------------------------------------------
    //基本処理
    //----------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_option);

        //シェアプリファレンスの設定
        SharedPreferences pref = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        //関連付けを行う
        editTextUserNameProgram = (TextView) findViewById(R.id.editTextUserName);
        RadioButton radioButtonManProgram = (RadioButton) findViewById(R.id.radioButtonMan);
        RadioButton radioButtonWomanProgram = (RadioButton) findViewById(R.id.radioButtonWoman);

        //名前をEditTextに表示
        editTextUserNameProgram.setText(pref.getString("UserName1", "NoData") + pref.getString("UserName2", "NoData"));

        //性別を選択済みにする
        gender = pref.getString("Gender", "男");
        if (gender.contains("男")) {
            radioButtonManProgram.setChecked(true);
        } else {
            radioButtonWomanProgram.setChecked(true);
        }

        //ラジオボタンが変更されたら
        radioGroupHeightProgram = (RadioGroup) findViewById(R.id.radioGroupHeight);
        radioGroupHeightProgram.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1) {
                    RadioButton radioButton = (RadioButton) findViewById(checkedId);
                    gender = radioButton.getText().toString();
                }
            }
        });
    }

    //戻るボタンを押したときの処理
    @Override
    public void onBackPressed() {
        SharedPreferences pref = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        if (pref.getString("Gender", "") == "") {
            Toast.makeText(this, "ユーザー情報を入力してください", Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }

    //保存ボタン押した時の動作
    public void Save(View v) {

        SharedPreferences pref = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Gender", gender);
        editor.commit();
        finish();

    }

}
