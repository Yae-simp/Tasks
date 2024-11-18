package com.example.tasks.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseManager(context: Context) : SQLiteOpenHelper(context, "tasks_db.db", null, 1 ) {

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "TasksDB.db"

        private const val SQL_CREATE_TABLE =
            "CREATE TABLE Task (" + "id INTEGER PRIMARY KEY AUTOINCREMENT," + "name TEXT," + "done INTEGER)"

        private const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS Task"
    }

    override fun onCreate (db: SQLiteDatabase?) {
        TODO("Not yet implemented")
    }

    override fun onUpgrade (db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun onDestroy (db: SQLiteDatabase?) {
        TODO("Not yet implemented")
    }
}