package nippon_hack.picture_view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class TopActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public ProgressDialog progressDialogLogin;
    private static final int REQUEST_GALLERY = 0;
    TextView editTextNameNavProgram;
    TextView editTextEmailAddressNavProgram;


    //----------------------------------------------
    //基本処理
    //----------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // プログレスダイアログの生成
        this.progressDialogLogin = new ProgressDialog(this);
        this.progressDialogLogin.setCanceledOnTouchOutside(false); //タップを無効化
        this.progressDialogLogin.setCancelable(false); //バックボタンを無効化
        this.progressDialogLogin.setMessage("起動中...");  //メッセージをセット
        this.progressDialogLogin.show();

        //すでにログインしているか確認
        //シェアプリファレンスの設定
        SharedPreferences pref = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        //サーバーにpost送信する
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email", "" + pref.getString("EmailAddress", "NoData"));
        params.put("password", "" + pref.getString("PassWord", "NoData"));
        client.post("https://nippon-hack.com/accounts/login_application.php", params, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String res) {
                        //レスポンスが帰ってきた場合
                        if (res.contains("@ERR@")) {
                            //パスワードまたはメールアドレスが間違っている、初めて起動した場合MainActivityを起動（レスポンスが@ERR@）
                            Intent intent = new Intent(TopActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            //正常にログインできた場合
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
                            //プログレスダイアログ表示中の場合
                            if (progressDialogLogin.isShowing()) {
                                //プログレスダイアログを閉じる
                                progressDialogLogin.dismiss();
                            }

                            editTextNameNavProgram = (TextView) findViewById(R.id.editText_Name_Nav);
                            editTextEmailAddressNavProgram = (TextView) findViewById(R.id.editText_EmailAddress_Nav);

                            editTextNameNavProgram.setText("" + userDatas[1] + userDatas[2]);
                            editTextEmailAddressNavProgram.setText("" + userDatas[0]);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        //サーバーに繋がらなかった場合（メンテナンス、機内モードなど）
                        Toast.makeText(getApplicationContext(), "サーバーに接続できませんでした。", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
        );

        //ユーザー情報が設定されているか
        if (pref.getString("Gender", "") == "") {
            Intent intent = new Intent(TopActivity.this, UserOptionActivity.class);
            startActivity(intent);
        }

        setContentView(R.layout.activity_top);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top, menu);
        return true;
    }

    //----------------------------------------------
    //ナビゲーション選択関係
    //----------------------------------------------
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Option) {
            Intent intent = new Intent(TopActivity.this, OptionActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_Logout) {
            //ログアウト
            //ログアウトを確認するダイアログ
            new AlertDialog.Builder(TopActivity.this)
                    .setTitle("ログアウトしますか？")
                    .setPositiveButton(
                            "はい",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences pref = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                                    pref.edit().clear().commit();
                                    Intent intent = new Intent(TopActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                    .setNegativeButton(
                            "いいえ",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                    .show();
        } else if (id == R.id.nav_copyright) {
            //著作権表記
            new AlertDialog.Builder(TopActivity.this)
                    .setTitle("著作権表記")
                    .setMessage("【アイコン素材】\nhttp://icon-rainbow.com/")
                    .setPositiveButton("OK", null)
                    .show();
        } else if (id == R.id.nav_send) {
            //お問い合わせ
            //ブラウザを起動する確認ダイアログ
            new AlertDialog.Builder(TopActivity.this)
                    .setTitle("ブラウザを起動しますがよろしいですか？")
                    .setPositiveButton(
                            "はい",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Uri uri = Uri.parse("https://nippon-hack.com/support/mail.php");
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                }
                            })
                    .setNegativeButton(
                            "いいえ",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                    .show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

