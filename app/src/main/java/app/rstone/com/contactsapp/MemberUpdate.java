package app.rstone.com.contactsapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import static app.rstone.com.contactsapp.Main.*;

public class MemberUpdate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Main.Member m = new Main.Member();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_update);
        Context ctx = MemberUpdate.this;
        Intent intent = this.getIntent();
        String[] spec = intent.getExtras().getString("spec").split(",");
        ImageView profile = findViewById(R.id.profile);
/*        String image = intent.getExtras().getString("profile");*/
        profile.setImageDrawable(
                getResources()
                        .getDrawable(
                                getResources()
                                        .getIdentifier(this.getPackageName()+":drawable/"+spec[6],null,null)
                                ,ctx.getTheme()));
        EditText name = findViewById(R.id.textName);
        name.setText(spec[1]);
        EditText email = findViewById(R.id.changeEmail);
        email.setText(spec[3]);
        EditText phone = findViewById(R.id.changePhone);
        phone.setText(spec[4]);
        EditText addr = findViewById(R.id.changeAddress);
        addr.setText(spec[5]);

        findViewById(R.id.confirmBtn).setOnClickListener(
                (View view)->{
                        ItemUpdate query = new ItemUpdate(ctx);

                        query.m.name = (name.getText().toString().equals(""))? spec[1]:name.getText().toString() ;
                        query.m.addr = (addr.getText().toString().equals(""))? spec[5]:addr.getText().toString();
                        query.m.email = (email.getText().toString().equals(""))? spec[3]:email.getText().toString();
                        query.m.phone = (phone.getText().toString().equals(""))? spec[4]:phone.getText().toString();
                        query.m.seq = Integer.parseInt(spec[0]);
                        new StatusService(){
                            @Override
                            public void perform() {
                                query.execute();
                            }
                        }.perform();
                        Intent moveDetail = new Intent(ctx, MemberDetail.class);
                        moveDetail.putExtra("seq", spec[0]);
                        startActivity(moveDetail);
                }
        );
        findViewById(R.id.cancelBtn).setOnClickListener(
                (View view)->{
                    Intent moveDetail = new Intent(ctx,MemberDetail.class);
                    startActivity(moveDetail);
                }
        );
    }
    private  class MemberUpdateQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;
        public MemberUpdateQuery(Context ctx) {
            super(ctx);
            helper = new Main.SQLIteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase();
        }
    }

    private  class ItemUpdate extends MemberUpdateQuery{
        Main.Member m;
        public ItemUpdate(Context ctx) {
            super(ctx);
            m = new Main.Member();
        }
        public void execute(){
            getDatabase().execSQL(
                          String.format(
                                    " UPDATE %s " +
                                    " SET %s = '%s' , " +
                                    " %s = '%s' , " +
                                    " %s = '%s' , " +
                                    " %s = '%s' " +
                                    " WHERE %s LIKE '%s' ",
                                    MEMTAB,
                                    MEMNAME, m.name,
                                    MEMEMAIL, m.email,
                                    MEMPHONE,m.phone,
                                    MEMADDR,m.addr,
                                    MEMSEQ, m.seq)
                            );
        };
    }
}
