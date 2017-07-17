package com.becdoor.teanotes.tables;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.becdoor.teanotes.db.Bean;
import com.becdoor.teanotes.model.AtticleCate;
import com.becdoor.teanotes.model.NoteDetailBean;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 13-4-9
 * Time: 下午2:45
 * To change this template use File | Settings | File Templates.
 */
public class ArticleTable extends Bean {

    private static final String TABLE_NAME = "article";

    private static final String _id = "_id";
    private static final String cat_id = "cat_id";
    private static final String data = "obj_data";


    private SQLiteDatabase dataBase;

    private static class CategoryTableHolder {
        private static ArticleTable instance = new ArticleTable();
    }

    private ArticleTable() {
        super();
    }

    public synchronized static ArticleTable getInstance() {
        return CategoryTableHolder.instance;
    }

    @Override
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME + " ("
                + _id + " TEXT PRIMARY KEY, "
                + cat_id + " INTEGER, "
                + data + " TEXT);";
        db.execSql(sql);
    }

    public void init() {
        initDatabase();
    }

    private void initDatabase() {
        if (dataBase == null) {
            dataBase = db.getDb();
        }
        if (!dataBase.isOpen()) {
            Log.d("db", "database 没有打开");
        }
    }

    //暂时没用
    public void saveList(List<NoteDetailBean> list) {
        initDatabase();
        try {
            dataBase.beginTransaction();
            String sql = "REPLACE INTO " + TABLE_NAME + " (" + _id + ", " + cat_id + ", " + data + ") values (?, ?, ?)";
            for (NoteDetailBean item : list) {
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = null;
                objectOutputStream = new ObjectOutputStream(arrayOutputStream);
                objectOutputStream.writeObject(item);
                objectOutputStream.flush();
                byte objData[] = arrayOutputStream.toByteArray();
                objectOutputStream.close();
                arrayOutputStream.close();
                dataBase.execSQL(sql, new Object[]{item.getArticle_id(), item.getCat_id(), objData});
            }
            dataBase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dataBase.endTransaction();
        }
    }

    public boolean save(NoteDetailBean item) {
        initDatabase();
        try {
            dataBase.beginTransaction();
            String sql = "";
            sql = "REPLACE INTO " + TABLE_NAME + " (" + _id + ", " + cat_id + ", " + data + ") values (?, ?, ?)";
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = null;
            objectOutputStream = new ObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(item);
            objectOutputStream.flush();
            byte objData[] = arrayOutputStream.toByteArray();
            objectOutputStream.close();
            arrayOutputStream.close();
            dataBase.execSQL(sql, new Object[]{item.getArticle_id(), item.getCat_id(), objData});
            dataBase.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dataBase.endTransaction();
        }
        return false;
    }


    //获取全部文章
    public List<NoteDetailBean> getList() {
        initDatabase();
        List<NoteDetailBean> list = Collections.emptyList();
        if (dataBase != null) {
            Cursor cursor = dataBase.query(TABLE_NAME, null, null, null, null, null, null);
            list = paser(cursor);
        }
        return list;
    }

    /*//获取全部文章
    public List<NoteDetailBean> getAllList () {
        initDatabase();
        List<NoteDetailBean> list = Collections.emptyList();
        String sql="select * from "+TABLE_NAME;
        if (dataBase != null) {
            Cursor cursor = dataBase.rawQuery(sql, new String[]{});
            list = paser(cursor);
        }

        return list;
    }
*/
    //获取该类别下的文章
    public List<NoteDetailBean> getListByCateId(int catID) {
        initDatabase();
        List<NoteDetailBean> list = Collections.emptyList();
        String sql = "select * from " + TABLE_NAME + " where " + cat_id + "=?";
        if (dataBase != null) {
            Cursor cursor = dataBase.rawQuery(sql, new String[]{String.valueOf(catID)});
            list = paser(cursor);
        }
        return list;
    }


    private List<NoteDetailBean> paser(Cursor cursor) {
        List<NoteDetailBean> result = Collections.emptyList();
        if (cursor == null || !cursor.moveToFirst()) {
            if (cursor != null) {
                cursor.close();
            }
            return result;
        }

        result = new LinkedList<NoteDetailBean>();
        NoteDetailBean item=null;
        try {
            do {
                byte data[] = cursor.getBlob(cursor.getColumnIndex(this.data));
                ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
                ObjectInputStream inputStream = null;
                try {
                    inputStream = new ObjectInputStream(arrayInputStream);
                    item = (NoteDetailBean) inputStream.readObject();
                    inputStream.close();
                    arrayInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                item.setArticle_id(cursor.getString(cursor.getColumnIndex(_id)));
                result.add(item);
            } while (cursor.moveToNext());
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return result;
    }

    public boolean delete(List<NoteDetailBean> list){
        initDatabase();
        try {
            dataBase.beginTransaction();
            String sql = "DELETE FROM " + TABLE_NAME + " where " + _id + " = ? ;" ;
            for (NoteDetailBean item : list) {
                dataBase.execSQL(sql, new Object[]{item.getArticle_id()});
            }
            dataBase.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dataBase.endTransaction();
        }
        return false;
    }

    public boolean delete(NoteDetailBean item){
        initDatabase();
        try {
            dataBase.beginTransaction();
            String sql = "DELETE FROM " + TABLE_NAME + " where " + _id + " = ? ;" ;
            dataBase.execSQL(sql, new Object[]{item.getArticle_id()});
            dataBase.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dataBase.endTransaction();
        }
        return false;
    }


    public boolean cleanTable() {
        initDatabase();
        String sql = "DELETE FROM " + TABLE_NAME;
        return db.execSql(sql);
    }

    private int getCount() {
        initDatabase();
        int n = 0;
        if (dataBase != null) {
            String sql = "SELECT count(1) FROM " + TABLE_NAME;
            Cursor cursor = dataBase.rawQuery(sql, null);
            cursor.moveToFirst();
            n = cursor.getInt(0);
            if (cursor != null)
                cursor.close();
        }

        return n;
    }
}
