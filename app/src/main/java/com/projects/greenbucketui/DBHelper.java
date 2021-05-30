package com.projects.greenbucketui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String CART_TABLE_NAME = "cart";
    public static final String NAME="name";
    public static final String IMAGE="image";
    public static final String PRICE="price";
    public static final String QUANTITY="quantity";
    public static final String CATAGORY="catagory";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table cart " +
                        "(id integer primary key, name text,image text,quantity integer,catagory text,price integer) "
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS cart");
        onCreate(db);
    }

    public boolean insertData (int id,String name,String image,int price,int quantity,String catagory) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",id);
        contentValues.put("name", name);
        contentValues.put("price", price);
        contentValues.put("image",image);
        contentValues.put("quantity",quantity);
        contentValues.put("catagory",catagory);
        db.insert("cart", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from cart where id="+id+"", null );
        return res;
    }
    public int getQuantity(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from cart where id="+id+"", null );
        if (res.getCount()==0)
            return 0;
        res.moveToFirst();
        return res.getInt(res.getColumnIndex(QUANTITY));
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CART_TABLE_NAME);
        return numRows;
    }

    public boolean updateData (Integer id,String name,String image,int price,int quantity,String catagory ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("price", price);
        contentValues.put("image",image);
        contentValues.put("quantity",quantity);
        contentValues.put("catagory",catagory);
        db.update("cart", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteData (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("cart",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<CartItem> getAllData() {
        ArrayList<CartItem> array_list = new ArrayList<CartItem>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from cart", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            CartItem CartItem=new CartItem(res.getInt(res.getColumnIndex("id")),res.getString(res.getColumnIndex(NAME)),res.getString(res.getColumnIndex(CATAGORY)),res.getInt(res.getColumnIndex(PRICE)),res.getInt(res.getColumnIndex(QUANTITY)),res.getString(res.getColumnIndex(IMAGE)));
            array_list.add(CartItem);
            res.moveToNext();
        }
        return array_list;
    }
}