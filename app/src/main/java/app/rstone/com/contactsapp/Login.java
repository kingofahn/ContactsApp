package app.rstone.com.contactsapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static app.rstone.com.contactsapp.Main.*;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Context ctx = Login.this;
        findViewById(R.id.loginBtn).setOnClickListener(
                (View view)->{
                    ItemExist query = new ItemExist(ctx);
                    EditText x = findViewById(R.id.inputID);
                    EditText y = findViewById(R.id.inputPW);
                    query.id = x.getText().toString();
                    query.pw = y.getText().toString();
                    new Main.StatusService() {
                        @Override
                        public void perform() {
                            if(query.execute()){
                                Toast.makeText(ctx,"로그인 성공", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ctx,MemberList.class));
                            }else{
                                Toast.makeText(ctx,"로그인 실q패", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ctx,Login.class));
                            }
                        }
                    }.perform();
                }
        );
    }
    private class LoginQuery extends Main.QueryFactory {
        SQLiteOpenHelper helper;
        public LoginQuery(Context ctx) {
            super(ctx);
            helper = new Main.SQLIteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class ItemExist extends LoginQuery{
        String id,pw;
        public ItemExist(Context ctx) {
            super(ctx);
        }

        public boolean execute() {
            return super
                    .getDatabase()
                    .rawQuery(String.format(
                            " SELECT * FROM %s " +
                                    " WHERE %s LIKE '%s' AND %s LIKE '%s' ",
                            MEMTAB,
                            MEMSEQ,
                            id,
                            MEMPW,
                            pw
                    ), null)
                    .moveToNext();
        }
    }
}
