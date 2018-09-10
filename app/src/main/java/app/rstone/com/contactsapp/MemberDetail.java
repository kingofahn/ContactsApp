package app.rstone.com.contactsapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import static app.rstone.com.contactsapp.Main.MEMADDR;
import static app.rstone.com.contactsapp.Main.MEMEMAIL;
import static app.rstone.com.contactsapp.Main.MEMNAME;
import static app.rstone.com.contactsapp.Main.MEMPHONE;
import static app.rstone.com.contactsapp.Main.MEMPHOTO;
import static app.rstone.com.contactsapp.Main.MEMSEQ;
import static app.rstone.com.contactsapp.Main.MEMTAB;

public class MemberDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_detail);
        final Context this__ = MemberDetail.this;
        Intent intent = this.getIntent();
        String seq = intent.getExtras().getString(MEMSEQ);

        ItemDetail query = new ItemDetail(this__);
        query.seq = seq;
        Main.Member m = (Main.Member) new Main.RetrieveService() {
            @Override
            public Object perform() {
                return query.execute();
            }
        }.perform();

        ImageView img = findViewById(R.id.profile);
        TextView name = findViewById(R.id.name);
        TextView phone = findViewById(R.id.phone);
        TextView addr = findViewById(R.id.addr);
        TextView email = findViewById(R.id.email);

        img.setImageDrawable(getResources().getDrawable(
                getResources().getIdentifier(
                        this.getPackageName()+":drawable/" + m.photo
                        , null, null), this__.getTheme()));
        name.setText(m.name);
        phone.setText(m.phone);
        addr.setText(m.addr);
        email.setText(m.email);

        findViewById(R.id.updateBtn).setOnClickListener(
                (View v) -> {
                    Intent i = new Intent(this__, MemberUpdate.class);
                    i.putExtra(MEMSEQ ,m.seq+"");
                    startActivity(i);
                }
        );
        findViewById(R.id.callBtn).setOnClickListener(
                (View v) -> {

                }
        );
        findViewById(R.id.dialBtn).setOnClickListener(
                (View v) -> {

                }
        );
        findViewById(R.id.smsBtn).setOnClickListener(
                (View v) -> {

                }
        );
        findViewById(R.id.emailBtn).setOnClickListener(
                (View v) -> {

                }
        );
        findViewById(R.id.albumBtn).setOnClickListener(
                (View v) -> {

                }
        );
        findViewById(R.id.movieBtn).setOnClickListener(
                (View v) -> {

                }
        );
        findViewById(R.id.mapBtn).setOnClickListener(
                (View v) -> {

                }
        );
        findViewById(R.id.musicBtn).setOnClickListener(
                (View v) -> {

                }
        );
        findViewById(R.id.listBtn).setOnClickListener(
                (View v) -> {

                }
        );
    }
    static class DetailQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;
        public DetailQuery(Context _this) {
            super(_this);
            helper = new Main.SQLIteHelper(_this);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }

    static class ItemDetail extends DetailQuery{
        String seq;
        public ItemDetail(Context _this) {
            super(_this);
        }

        public Main.Member execute(){
            Cursor c = this.getDatabase().rawQuery(
                    String.format(" SELECT * FROM %s WHERE %s LIKE '%s'"
                            , MEMTAB, MEMSEQ, seq),null);
            Main.Member m = null;
            if(c != null){
                if(c.moveToNext()){
                    m = new Main.Member();
                    m.seq = c.getInt(c.getColumnIndex(MEMSEQ));
                    m.name = c.getString(c.getColumnIndex(MEMNAME));
                    m.addr = c.getString(c.getColumnIndex(MEMADDR));
                    m.phone = c.getString(c.getColumnIndex(MEMPHONE));
                    m.email = c.getString(c.getColumnIndex(MEMEMAIL));
                    m.photo = c.getString(c.getColumnIndex(MEMPHOTO));
                }
            }else{
                Log.d("상세정보를 불러올 수 없습니다.", "");
            }
            return m;
        }
    }
}