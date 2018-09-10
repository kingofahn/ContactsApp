package app.rstone.com.contactsapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static app.rstone.com.contactsapp.Main.*;

public class MemberList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_list);
        Context this__ = MemberList.this;
        ItemList query = new ItemList(this__);
        ListView memberList = findViewById(R.id.memberList);
        memberList.setAdapter(new MemberAdapter(this__
                , (ArrayList<Main.Member>) new ListService(){
            @Override
            public List<?> perform() {
                return query.execute();
            }
        }.perform()));
        memberList.setOnItemClickListener(
                (AdapterView<?> p, View v, int i, long l) -> {
                    Intent intent = new Intent(this__, MemberDetail.class);
                    Main.Member m = (Member) memberList.getItemAtPosition(i);
                    intent.putExtra(MEMSEQ, m.seq+"");
                    startActivity(intent);
                }
        );
        memberList.setOnItemLongClickListener(
                (AdapterView<?> p, View v, int i, long l)->{
                    Toast.makeText(this__,"LONG CLICK",Toast.LENGTH_SHORT).show();
                    ItemDelete deleteQuery = new ItemDelete(this__);
                    Main.Member m = (Member) memberList.getItemAtPosition(i);
                    deleteQuery.seq = m.seq + "";
                    new AlertDialog.Builder(this__)
                            .setTitle("DELETE")
                            .setMessage("정말로 삭제하겠습니까 ?")
                            .setPositiveButton(android.R.string.yes
                                    , new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            new StatusService() {
                                                @Override
                                                public void perform() {
                                                    deleteQuery.execute();
                                                    Intent moveList = new Intent(this__, MemberList.class);
                                                    startActivity(moveList);
                                                }
                                            }.perform();
                                            Toast.makeText(this__,"삭제실행",Toast.LENGTH_SHORT).show();
                                        }
                                    })
                            .setNegativeButton(android.R.string.no
                                    , new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(this__,"삭제취소",Toast.LENGTH_SHORT).show();
                                        }
                                    })
                            .show();
                    return true;
                }
        );
        findViewById(R.id.addBtn).setOnClickListener(
                (View v)->{
                    startActivity(new Intent(this__, MemberAdd.class));
                }
        );
    }
    private class MemberDeleteQuery extends  Main.QueryFactory{
        SQLiteOpenHelper helper;
        public MemberDeleteQuery(Context _this) {
            super(_this);
            helper = new SQLIteHelper(_this);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase();
        }
    }
    private class ItemDelete extends MemberDeleteQuery{
        String seq;
        public ItemDelete(Context _this) {
            super(_this);
        }
        public void execute(){
            getDatabase().execSQL(
                    String.format(" DELETE FROM MEMBER WHERE SEQ LIKE '%s' ", seq ));
        }
    }
    private class ListQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;
        public ListQuery(Context _this) {
            super(_this);
            helper = new Main.SQLIteHelper(_this);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class ItemList extends ListQuery{

        public ItemList(Context _this) {
            super(_this);
        }

        public ArrayList<Main.Member> execute(){
            Cursor c = this.getDatabase().rawQuery(String.format(
                    " SELECT * FROM %s ", MEMTAB)
                    , null);
            ArrayList<Main.Member> list = new ArrayList<>();
            Main.Member m = null;
            if(c != null){
                while (c.moveToNext()) {
                    m = new Main.Member();
                    m.seq = c.getInt(c.getColumnIndex(MEMSEQ));
                    m.name = c.getString(c.getColumnIndex(MEMNAME));
                    m.addr = c.getString(c.getColumnIndex(MEMADDR));
                    m.email = c.getString(c.getColumnIndex(MEMEMAIL));
                    m.phone = c.getString(c.getColumnIndex(MEMPHONE));
                    m.photo = c.getString(c.getColumnIndex(MEMPHOTO));
                    list.add(m);
                } ;
                Log.d("등록된 회원 수 :: ", list.size() + "");
            }else{
                Log.d("등록된 회원이 없습니다.","");
            }
            return list;
        }
    }
    private class MemberAdapter extends BaseAdapter{
        ArrayList<Main.Member> list;
        LayoutInflater inflater;
        Context this__;

        public MemberAdapter(Context _this, ArrayList<Member> list) {
            this.list = list;
            this.inflater = LayoutInflater.from(_this);
            this.this__ = _this;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View v, ViewGroup g) {
            ViewHolder holder;
            if(v == null){
                v = inflater.inflate(R.layout.member_item, null);
                holder = new ViewHolder();
                holder.profile = v.findViewById(R.id.profile);
                holder.name = v.findViewById(R.id.name);
                holder.phone = v.findViewById(R.id.phone);
                v.setTag(holder);
            }else{
                holder = (ViewHolder) v.getTag();
            }
            ItemProfile query = new ItemProfile(this__);
            query.seq = list.get(i).seq+"";

            holder.profile.setImageDrawable(
                    getResources().getDrawable(
                            getResources().getIdentifier(
                                    this__.getPackageName()+":drawable/"
                                            + (new RetrieveService() {
                                        @Override
                                        public Object perform() {
                                            return query.execute();
                                        }
                                    }.perform())
                                    , null, null
                            ), this__.getTheme()
                    )
            );
            holder.name.setText(list.get(i).name);
            holder.phone.setText(list.get(i).phone);
            return v;
        }
    }
    static class ViewHolder{
        ImageView profile;
        TextView name, phone;
    }
    private class MemberProfileQuery extends QueryFactory{
        SQLiteOpenHelper helper;
        public MemberProfileQuery(Context _this) {
            super(_this);
            helper = new SQLIteHelper(_this);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class ItemProfile extends MemberProfileQuery{
        String seq;
        public ItemProfile(Context _this) {
            super(_this);
        }
        public String execute(){
            Cursor c = getDatabase()
                    .rawQuery(String.format(
                            " SELECT %s FROM %s WHERE %s LIKE '%s' "
                            , MEMPHOTO, MEMTAB, MEMSEQ, seq),null);
            String result = "";
            if(c != null){
                if(c.moveToNext()){
                    result = c.getString(c.getColumnIndex(MEMPHOTO));
                }
            }
            return result;
        }
    }
}