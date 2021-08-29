package com.example.Hacker_News.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.Hacker_News.model.InsertTopNews
import java.util.*

class DatabaseHandler(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "HNDatabase"
        private val TABLE_NEWS = "NewsTable"
        private val KEY_TITLE = "name"
        private val KEY_URL = "url"
        private val KEY_TIME = "time"
        private val KEY_BY = "by"
        private val KEY_TYPE = "type"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_NEWS + "("
                + KEY_TITLE + " TEXT," + KEY_URL + " TEXT,"
                + KEY_TIME + " TEXT" + "," + KEY_BY + " TEXT," + KEY_TYPE + " TEXT)")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS)
        onCreate(db)
    }

    fun addNews(news: InsertTopNews):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, news.title)
        contentValues.put(KEY_TIME, news.time)
        contentValues.put(KEY_BY, news.by)
        contentValues.put(KEY_URL, news.url)
        contentValues.put(KEY_TYPE, news.type)
        // Inserting Row
        val success = db.insert(TABLE_NEWS, null, contentValues)
        db.close() // Closing database connection
        return success
    }

    fun viewNews(strType:String):List<InsertTopNews>{
        val newsList: ArrayList<InsertTopNews> = ArrayList<InsertTopNews>()
        val selectQuery = "SELECT  * FROM "+TABLE_NEWS+" WHERE type = '"+strType+"'"
        val db = this.writableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var title: String
        var by: String
        var time: String
        var url: String
        var type: String
        if (cursor.moveToFirst()) {
            do {
                title = cursor.getString(cursor.getColumnIndex(KEY_TITLE))
                by = cursor.getString(cursor.getColumnIndex(KEY_BY))
                time = cursor.getString(cursor.getColumnIndex(KEY_TIME))
                url = cursor.getString(cursor.getColumnIndex(KEY_URL))
                type = cursor.getString(cursor.getColumnIndex(KEY_TYPE))
                val news= InsertTopNews(by,time,title,url,type)
                newsList.add(news)
            } while (cursor.moveToNext())
        }
        return newsList
    }

    fun deleteNews(strType: String):Int{
        val db = this.writableDatabase
        // Deleting Row
        val success = db.delete(TABLE_NEWS, KEY_TYPE + " = ?", arrayOf(strType))
        db.close() // Closing database connection
        return success
    }
}