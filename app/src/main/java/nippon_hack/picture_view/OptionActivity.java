package nippon_hack.picture_view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class OptionActivity extends AppCompatActivity {
    ListView listViewOptionProgram;

    //リストビューの内容
    private static final String[] optionContent = {
            "ユーザー情報の変更", "未設定_01", "未設定_02", "未設定_03"
    };

    //----------------------------------------------
    //基本処理
    //----------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        //関連付けを行う
        listViewOptionProgram = (ListView) findViewById(R.id.listViewOption);

        //リストビューの中身をセットする
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, optionContent);
        listViewOptionProgram.setAdapter(arrayAdapter);

        //リストビューがタップされた時
        listViewOptionProgram.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    //ユーザー情報の変更
                    Intent intent = new Intent(OptionActivity.this, UserOptionActivity.class);
                    startActivity(intent);
                }else if(position==1){

                }else if(position==2){

                }
            }
        });
    }
}
