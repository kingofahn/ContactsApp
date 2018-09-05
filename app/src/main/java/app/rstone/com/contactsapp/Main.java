package app.rstone.com.contactsapp;

import android.content.Context;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Context ctx = Main.this;

        findViewById(R.id.createDB).setOnClickListener(
                (View view)->{
                    SQLIteHelper helper = new SQLIteHelper(ctx);
                }
        );

       findViewById(R.id.login).setOnClickListener(
                (View view)->{
                    startActivity(new Intent(ctx,Login.class));
                }
        );
        findViewById(R.id.add).setOnClickListener(
                (View view)->{
                    startActivity(new Intent(ctx,MemberAdd.class));
                }
        );
    }
    static class Member{int seq;String name,pw,email,phone,addr,photo;}
    static interface StatusService{public void perform();}
    static interface ListService{public List<?> perform();}
    static interface RetrieveService{public Object perform();}
    static String DBNAME = "rstone.db";
    static String MEMTAB = "MEMBER";
    static String MEMSEQ = "SEQ";
    static String MEMNAME = "NAME";
    static String MEMPW = "PW";
    static String MEMEMAIL = "EMAIL";
    static String MEMPHONE = "PHONE";
    static String MEMADDR = "ADDR";
    static String MEMPHOTO= "PHOTO";
    static abstract class QueryFactory{
        Context ctx;

        public QueryFactory(Context ctx) {
            this.ctx = ctx;
        }
        public abstract SQLiteDatabase getDatabase();  //Toad사용 불가 (내장db임) read only
    }
    static  class SQLIteHelper extends SQLiteOpenHelper{

        public SQLIteHelper(Context context) {
            super(context, DBNAME, null,1);
            this.getWritableDatabase();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = String.format(
                    " CREATE TABLE IF NOT EXISTS %s "+
                            " (%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            " %s TEXT, " +
                            " %s TEXT, " +
                            " %s TEXT, " +
                            " %s TEXT, " +
                            " %s TEXT, " +
                            " %s TEXT) ",
                    MEMTAB,MEMSEQ,MEMNAME,MEMPW,MEMEMAIL,MEMPHONE,MEMADDR,MEMPHOTO
            );
            Log.d("실행할퀴리 ::",sql);
            db.execSQL(sql);
            Log.d("====================","create 쿼리실행완료");
            sql = String.format(
                    " INSERT INTO %s "+
                            "(%s " +
                            " %s " +
                            " %s " +
                            " %s " +
                            " %s " +
                            " %s " +
                            ") VALUES" +
                            " ('%s', " +
                            " '%s', " +
                            " '%s', " +
                            " '%s', " +
                            " '%s', " +
                            " '%s') ",
                            MEMTAB,MEMNAME,MEMPW,MEMEMAIL,
                            MEMPHONE,MEMADDR,MEMPHOTO,
                            MEMNAME,MEMPW,MEMEMAIL,
                            MEMPHONE,MEMADDR,MEMPHOTO
            );

            for(int i=1;i<=5;i++){
                db.execSQL(String.format(
                        " INSERT INTO %s "+
                                "(%s, " +
                                " %s, " +
                                " %s, " +
                                " %s, " +
                                " %s, " +
                                " %s " +
                                ") VALUES" +
                                " ('%s', " +
                                " '%s', " +
                                " '%s', " +
                                " '%s', " +
                                " '%s', " +
                                " '%s') ",
                        MEMTAB,MEMNAME,MEMPW,MEMEMAIL,
                        MEMPHONE,MEMADDR,MEMPHOTO,
                        "홍길동"+i,"1","hong"+i+"@test.com",
                        "010-5000-999"+i,"신촌 "+i+"길","hong"+i+".jpg"
                ));
            }
            Log.d("====================","insert쿼리실행완료");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+MEMTAB);
            onCreate(db);
        }
    }
}
