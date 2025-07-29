
package com.examples.mahasiswasqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "mahasiswa.db";
    private static final String TABLE_NAME = "data_mahasiswa";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME +
                " (nim TEXT PRIMARY KEY, nama TEXT, jk TEXT, jurusan TEXT, kelas TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String nim, String nama, String jk, String jurusan, String kelas) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nim", nim);
        cv.put("nama", nama);
        cv.put("jk", jk);
        cv.put("jurusan", jurusan);
        cv.put("kelas", kelas);
        long result = db.insert(TABLE_NAME, null, cv);
        return result != -1;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public boolean updateData(String nim, String nama, String jk, String jurusan, String kelas) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nama", nama);
        cv.put("jk", jk);
        cv.put("jurusan", jurusan);
        cv.put("kelas", kelas);
        return db.update(TABLE_NAME, cv, "nim = ?", new String[]{nim}) > 0;
    }

    public boolean deleteData(String nim) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "nim = ?", new String[]{nim}) > 0;
    }

    //Tambahkan Method untuk mengambil daftar jurusan:
    
    public ArrayList<String> getAllJurusan() {
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT DISTINCT jurusan FROM " + TABLE_NAME + " WHERE jurusan IS NOT NULL AND jurusan != ''", null);
        while (res.moveToNext()) {
            list.add(res.getString(0));
        }
        res.close();
        return list;
    }
}
