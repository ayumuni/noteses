package ayu.noteses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by ayumuni on 5/29/2017.
 */

public class DBDataSource {
    private SQLiteDatabase database;

    //inisialisasi kelas DBHelper
    private DBHelper dbHelper;

    //ambil semua nama kolom
    private String[] allColumns = {DBHelper.COLUMN_ID,
            DBHelper.COLUMN_TITLE, DBHelper.COLUMN_CONTENT};
    private String[] title = {DBHelper.COLUMN_TITLE};

    //DBHelper diinstantiasi pada constructor
    public DBDataSource(Context context)
    {
        dbHelper = new DBHelper(context);
    }

    //membuka/membuat sambungan baru ke database
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    //menutup sambungan ke database
    public void close() {
        dbHelper.close();
    }

    //method untuk create/insert barang ke database
    public notes createNotes(String title, String content) {

        // membuat sebuah ContentValues, yang berfungsi
        // untuk memasangkan data dengan nama-nama
        // kolom pada database
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_TITLE, title);
        values.put(DBHelper.COLUMN_CONTENT, content);

        // mengeksekusi perintah SQL insert data
        // yang akan mengembalikan sebuah insert ID
        long insertId = database.insert(DBHelper.TABLE_NAME, null,
                values);

        // setelah data dimasukkan, memanggil
        // perintah SQL Select menggunakan Cursor untuk
        // melihat apakah data tadi benar2 sudah masuk
        // dengan menyesuaikan ID = insertID
        Cursor cursor = database.query(DBHelper.TABLE_NAME,
                allColumns, DBHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        // pindah ke data paling pertama
        cursor.moveToFirst();

        // mengubah objek pada kursor pertama tadi
        // ke dalam objek barang
        notes newNotes = cursorToNotes(cursor);

        // close cursor
        cursor.close();

        // mengembalikan barang baru
        return newNotes;
    }

    private notes cursorToNotes(Cursor cursor)
    {
        // buat objek barang baru
        notes notes = new notes();
        // debug LOGCAT
        Log.v("info", "The getLONG "+cursor.getLong(0));
        Log.v("info", "The setLatLng "+cursor.getString(1)+","+cursor.getString(2));

        /* Set atribut pada objek barang dengan
         * data kursor yang diambil dari database*/
        notes.setId(cursor.getLong(0));
        notes.setTitle(cursor.getString(1));
        notes.setContent(cursor.getString(2));

        //kembalikan sebagai objek barang
        return notes;
    }
    public ArrayList<String> getAllNotes() {
        ArrayList<notes> daftarNotes = new ArrayList<notes>();
        ArrayList<String> mylist = new ArrayList<String>();

        // select all SQL query
        Cursor cursor = database.query(DBHelper.TABLE_NAME,
                allColumns, null, null, null, null, null);

        // pindah ke data paling pertama
        cursor.moveToFirst();

        // jika masih ada data, masukkan data barang ke
        // daftar barang
        int j = 0;
        while (!cursor.isAfterLast()) {
            notes notes = cursorToNotes(cursor);
            Long l = cursor.getLong(0);
            mylist.add(cursor.getString(1));
            Log.v("delete-tes", l.toString());
            daftarNotes.add(notes);
            cursor.moveToNext();
            j++;
        }
        // Make sure to close the cursor
        cursor.close();
        return mylist;
    }
    public notes getNotes(long id)
    {
        notes notes = new notes(); //inisialisasi barang
        //select query
        Cursor cursor = database.query(DBHelper.TABLE_NAME, allColumns, "_id ="+id, null, null, null, null);
        //ambil data yang pertama
        cursor.moveToFirst();
        //masukkan data cursor ke objek barang
        notes = cursorToNotes(cursor);
        //tutup sambungan
        cursor.close();
        //return barang
        return notes;
    }
    public void updateNotes(notes b)
    {
        //ambil id note
        String strFilter = "_id=" + b.getId();
        //memasukkan ke content values
        ContentValues args = new ContentValues();
        //masukkan data sesuai dengan kolom pada database
        args.put(DBHelper.COLUMN_TITLE, b.getTitle());
        args.put(DBHelper.COLUMN_CONTENT, b.getContent());
        //update query
        database.update(DBHelper.TABLE_NAME, args, strFilter, null);
    }
    // delete note sesuai ID
    public void deleteNotes(long id)
    {
        String strFilter = "_id=" + id;
        database.delete(DBHelper.TABLE_NAME, strFilter, null);
    }
}
