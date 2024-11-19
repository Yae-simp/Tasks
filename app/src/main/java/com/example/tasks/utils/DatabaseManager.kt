package com.example.tasks.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tasks.data.Task

//class DatabaseManager is defining a custom class that inherits from SQLiteOpenHelper class.
//The : symbol is used to inherit from a class or implement an interface (: SQLiteOpenHelper(bla bla)).
//SQLiteOpenHelper is a special helper class provided by Android to simplify working with SQLite databases. It requires four parameters in its constructor.
class DatabaseManager(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    //We use a companion object to hold values and functions that are shared across all instances of a class.
    companion object {
        //If you change the database schema, you must increment the database version.
        //Constants are values that never change.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "ToDoListDatabase.db"

        //SQL string containing the command to create the table
        private const val SQL_CREATE_TABLE =
            //"CREATE TABLE" is the command that tells the database to create a new table.
            //"${Task.TABLE_NAME}" inserts the name of the table into the command.
            "CREATE TABLE ${Task.TABLE_NAME} (" +
                    //This creates a column called "COLUMN_ID" in the table that will hold a unique ID for each task. This ID will automatically increase each time a new task is added (because of AUTOINCREMENT).
                    "${Task.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    //This creates a column called "COLUMN_NAME" in the table, which will hold the name of the task.
                    "${Task.COLUMN_NAME} TEXT," +
                    //This creates a column called "COLUMN_DONE", which will store whether the task is finished or not. This column will hold an integer value (like 0 for not done, and 1 for done).
                    "${Task.COLUMN_DONE} INTEGER)"

        //SQL string containing the command to delete the table
        //DROP TABLE is the SQL command to delete a table from the database.
        //IF EXISTS ensures that the table is only deleted if it exists.
        private const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS ${Task.TABLE_NAME}"
    }

    //Called when the database is created for the first time
    override fun onCreate(db: SQLiteDatabase) {
        //This command runs the SQL statement stored in SQL_CREATE_TABLE (which creates a table).
        db.execSQL(SQL_CREATE_TABLE)
    }

    //Called when the database version is upgraded
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //Before upgrading the database, this function deletes the old table using the SQL_DELETE_TABLE command.
        onDestroy(db)
        //After the table is deleted, the onCreate(db) function is called to recreate the table (with the new structure) using SQL_CREATE_TABLE.
        onCreate(db)
    }

    ////Called when the database version is deleted
    private fun onDestroy(db: SQLiteDatabase) {
        //This command deletes the table from the database.
        db.execSQL(SQL_DELETE_TABLE)
    }
}