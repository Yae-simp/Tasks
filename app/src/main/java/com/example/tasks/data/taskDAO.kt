package com.example.tasks.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.tasks.utils.DatabaseManager

//This class is your Data Access Object (DAO), which helps you interact with the Task table in the database. It provides methods to insert, update, delete, and find tasks from the database.
class TaskDAO (private val context: Context) {

    private lateinit var db: SQLiteDatabase

    //This method opens a connection to the database using the DatabaseManager.
    //It calls writableDatabase, which gives you access to the database with write permissions (so you can insert, update, or delete data).
    private fun open() {
        db = DatabaseManager(context).writableDatabase
    }

    //This method closes the database connection after performing the operation.
    private fun close() {
        db.close()
    }

    //This method inserts a new task into the database.
    fun insert (task: Task) {
        //It first opens the database connection using the "open()" method.
        open()

        //ContentValues is a special class used to store key-value pairs, where each key is the column name in the database, and the value is the value you want to insert into that column.
        val values = ContentValues().apply {
            //put() method inside the ContentValues object maps these column names to their corresponding values.
            //keys (column names) and values (data to insert).
            put(Task.COLUMN_NAME, task.name) //"Task.COLUMN_NAME" is associated to "task.name"
            put(Task.COLUMN_DONE, task.done) //"Task.COLUMN_DONE" is associated to "task.done"
        }

        try {
            //Inserts the new row, returning primary key value of the new row.
            //Task.TABLE_NAME specifies the name of the table where you want to insert the data.
            val id = db.insert(Task.TABLE_NAME, null, values)
        } catch (e: Exception) {
            //A stack trace is a detailed log of the sequence of method calls that lead to an error.
            Log.e("DB", e.stackTraceToString())
        } finally {
            close()
        }
    }

    //This method updates an existing task in the database.
    fun update (task: Task) {
        open()

        //Creates a new map of values, where column names are the keys.
        val values = ContentValues().apply {
            put(Task.COLUMN_NAME, task.name)
            put(Task.COLUMN_DONE, task.done)
        }

        try {
            //db.update() method is called to update the existing task in the database:
            //Task.TABLE_NAME specifies the table where you want to update the data (in this case, the table is "Task").
            //values is the ContentValues object that holds the data to be updated.
            //"${Task.COLUMN_ID} = ${task.id}" specifies the condition for selecting the row(s) to be updated. In this case, the task is being updated where the id column equals task.id.
            val updatedRows = db.update(Task.TABLE_NAME, values, "${Task.COLUMN_ID} = ${task.id}", null)
        } catch (e: Exception) {
            Log.e("DB", e.stackTraceToString())
        } finally {
            close()
        }
    }

    //This method deletes an existing task in the database.
    fun delete (task: Task) {
        open()

        try {
            //db.delete() method is responsible for deleting a row from the database.
            //Task.TABLE_NAME is the name of the table from which the row will be deleted.
            //${Task.COLUMN_ID} = ${task.id}, WHERE clause of the DELETE SQL, deletes the row where the id column matches the task.id value.
            val deletedRows = db.delete(Task.TABLE_NAME, "${Task.COLUMN_ID} = ${task.id}", null)
        } catch (e: Exception) {
            Log.e("DB", e.stackTraceToString())
        } finally {
            close()
        }
    }

    //This method retrieves a task from the database by its ID.
    //@SuppressLint() is used to suppress warnings.
    @SuppressLint("Recycle")
    fun findById (id: Long) : Task? {
        open()

        //'projection' specifies the columns you want to retrieve from the database.
        //Tells the database to only return the columns Task.COLUMN_ID, Task.COLUMN_NAME, and Task.COLUMN_DONE.
        val projection = arrayOf(Task.COLUMN_ID, Task.COLUMN_NAME, Task.COLUMN_DONE)

        try {
            //The query returns a Cursor object, which points to the result set. Cursor allows you to navigate through the results and retrieve data from the columns.
            val cursor = db.query( //db.query() is used to perform the query and retrieve data from the database.
                Task.TABLE_NAME,                     //Specifies table to query.
                projection,                          //Defines array of columns to return (pass null to get all)
                "${Task.COLUMN_ID} = $id",  //The columns for the WHERE clause, filters the rows to find the one that has a matching id.
                null,                    //The values for the WHERE clause.
                null,                        //Don't group the rows.
                null,                         //Don't filter by row groups.
                null                         //Sort order.
            )

            //cursor.moveToNext() moves cursor to next row in the result set.
            if (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(Task.COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME))
                val done = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_DONE)) != 0

                //If data is retrieved successfully, a new Task object is created using retrieved id, name, and done values.
                return Task(id, name, done)
            }
        } catch (e: Exception) {
            Log.e("DB", e.stackTraceToString())
        } finally {
            close()
        }
        //If no results were found (meaning the cursor.moveToNext() didn’t find any matching row), or if there was an error, the function returns null, indicating that no task was found for the provided id.
        return null
    }

    @SuppressLint("Recycle")
    fun findAll() : List<Task> {
        open()

        //Creates a mutable (modifiable) list of 'Task' objects named 'list'. This list will store all the tasks retrieved from the database.
        val list: MutableList<Task> = mutableListOf()

        val projection = arrayOf(Task.COLUMN_ID, Task.COLUMN_NAME, Task.COLUMN_DONE)

        try {
            val cursor = db.query(
                Task.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
            )

            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(Task.COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME))
                val done = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_DONE)) != 0

                val task = Task(id, name, done)
                list.add(task)
            }
        } catch (e: Exception) {
            Log.e("DB", e.stackTraceToString())
        } finally {
            close()
        }
        return list
    }

}