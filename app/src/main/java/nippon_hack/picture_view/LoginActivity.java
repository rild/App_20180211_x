package nippon_hack.picture_view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {
    public ProgressDialog progressDialogLogin;

    //----------------------------------------------
    //基本処理
    //----------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    //----------------------------------------------
    //ログインボタン押した時の動作
    //----------------------------------------------
    public void Login(View v) {
        //関連付けを行う
        EditText editTextEmailAddressProgram = (EditText) findViewById(R.id.editTextEmailAddress);
        EditText editTextPasswordProgram = (EditText) findViewById(R.id.editTextPassword);
        Button buttonLoginProgram = (Button) findViewById(R.id.buttonLogin);

        //ボタンを押せなくする
        buttonLoginProgram.setEnabled(false);

        //チェックボックス
        final CheckBox CheckBox_Agreement_Program = (CheckBox) findViewById(R.id.CheckBoxAgreement);

        //チェックボックスにチェックが入っているか（利用規約に同意したか）
        if (CheckBox_Agreement_Program.isChecked() == true) {
            //同意した
            if (editTextEmailAddressProgram.getText().toString().contains("<") || editTextPasswordProgram.getText().toString().contains("<")) {
                Toast.makeText(getApplicationContext(), "使用できない文字が含まれています", Toast.LENGTH_SHORT).show();
            } else {
                // プログレスダイアログの生成
                this.progressDialogLogin = new ProgressDialog(this);
                this.progressDialogLogin.setCanceledOnTouchOutside(false); //タップを無効化
                this.progressDialogLogin.setCancelable(false); //バックボタンを無効化
                this.progressDialogLogin.setMessage("ログイン中...");  //メッセージをセット
                this.progressDialogLogin.show();

                //パスワードを保存する
                SharedPreferences pref = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("PassWord", editTextPasswordProgram.getText().toString());
                editor.commit();

                //サーバーにpost送信する
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("email", "" + editTextEmailAddressProgram.getText().toString());
                params.put("password", "" + editTextPasswordProgram.getText().toString());

                client.post("https://nippon-hack.com/accounts/login_application.php", params, new TextHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String res) {
                                if (res.contains("@ERR@")) {
                                    //パスワードまたはメールアドレスが間違っている場合（レスポンスが@ERR@）
                                    Toast.makeText(getApplicationContext(), "ログインに失敗しました", Toast.LENGTH_SHORT).show();

                                    //プログレスダイアログ表示中の場合
                                    if (progressDialogLogin.isShowing()) {
                                        //プログレスダイアログを閉じる
                                        progressDialogLogin.dismiss();
                                    }
                                } else {
                                    //レスポンスを保存する（ID,名前、ステータス等）
                                    SharedPreferences pref = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    String[] userDatas = res.split(",");    //レスポンスを「,」で分ける
                                    editor.putString("EmailAddress", userDatas[0]);
                                    editor.putString("UserName1", userDatas[1]);
                                    editor.putString("UserName2", userDatas[2]);
                                    editor.putString("UserName3", userDatas[3]);
                                    editor.putString("UserName4", userDatas[4]);
                                    editor.putString("ID", userDatas[5]);
                                    editor.putString("STATUS", userDatas[6]);
                                    editor.commit();

                                    //トーストを表示してLoginActivityを閉じる
                                    Toast.makeText(getApplicationContext(), "ログインに成功しました", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, TopActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                                //サーバーに繋がらなかった場合（メンテナンス、機内モードなど）
                                Toast.makeText(getApplicationContext(), "サーバーに接続できませんでした。", Toast.LENGTH_SHORT).show();
                                //プログレスダイアログ表示中の場合
                                if (progressDialogLogin.isShowing()) {
                                    //プログレスダイアログを閉じる
                                    progressDialogLogin.dismiss();
                                }
                            }
                        }
                );

            }

        } else {
            //同意していない
            Toast.makeText(getApplicationContext(), "利用規約に同意していません", Toast.LENGTH_SHORT).show();
        }

        //ボタンを押せるようにする
        buttonLoginProgram.setEnabled(true);
    }
}
