package com.example.tasks.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tasks.data.Task
import com.example.tasks.data.TaskDAO
import com.example.tasks.databinding.ActivityTaskBinding

class TaskActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TASK_ID = "TASK_ID"
    }

    private lateinit var binding: ActivityTaskBinding
    private lateinit var taskDAO: TaskDAO
    lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        taskDAO = TaskDAO(this)

        binding.saveButton.setOnClickListener {
            val taskName = binding.nameTextField.editText?.text.toString()
            if (taskName.isEmpty()) {
                binding.nameTextField.error = "Write your task."
                return@setOnClickListener
            }
            if (taskName.length > 50) {
                binding.nameTextField.error = "Error."
                return@setOnClickListener
            }
            task.name = taskName

            // Si la tarea existe la actualizamos si no la insertamos
            if (task.id != -1L) {
                taskDAO.update(task)
            } else {
                taskDAO.insert(task)
            }

            finish()
        }
    }
}