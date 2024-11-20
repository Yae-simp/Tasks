package com.example.tasks.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasks.R
import com.example.tasks.adapter.TaskAdapter
import com.example.tasks.data.Task
import com.example.tasks.data.TaskDAO
import com.example.tasks.databinding.ActivityMainBinding


@Suppress("UNREACHABLE_CODE")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TaskAdapter
    private lateinit var taskDAO: TaskDAO
    private var taskList: List<Task> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        taskDAO = TaskDAO(this)

        //Inserting some example tasks for testing
        taskDAO.delete(Task(-1, "Buy milk", false))
        taskDAO.delete(Task(-1, "Buy eggs", false))
        taskDAO.delete(Task(-1, "Buy laptop", false))

        adapter = TaskAdapter(taskList, {
            val task = taskList[it]
            task.done = !task.done
            taskDAO.update(task)
            taskList = taskDAO.findAll()
            adapter.updateItems(taskList)
        }, {
            AlertDialog.Builder(this)
                .setTitle("Delete task")
                .setMessage("Are you sure you want to delete this task?") // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    val task = taskList[it]
                    taskDAO.delete(task)
                    taskList = taskDAO.findAll()
                    adapter.updateItems(taskList)
                } // A null listener allows the button to dismiss the dialog and take no further action.

                .setNegativeButton(android.R.string.cancel, null)
                .setIcon(R.drawable.ic_delete_task)
                .show()
        },
            onItemDelete = TODO(),
            onItemLongClick = TODO()
        )

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.addTaskButton.setOnClickListener {
            val intent = Intent(this, TaskActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        taskList = taskDAO.findAll()
        adapter.updateItems(taskList)
    }
}
