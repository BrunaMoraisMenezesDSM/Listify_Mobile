package com.brunagiovanagiovanna.listify_mobile

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.brunagiovanagiovanna.listify_mobile.api.RetrofitClient
import com.brunagiovanagiovanna.listify_mobile.models.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class EditTaskActivity : AppCompatActivity() {
    var data: Date = Date()
    var task: Task? = null
    lateinit var btnSave: Button
    lateinit var txtName: TextView
    lateinit var txtDescription: EditText
    lateinit var spinnerPriority: Spinner
    lateinit var cvDateLimit: CalendarView
    lateinit var spinnerStatus: Spinner

    var selectedStatus: String = ""
    var statusArray = arrayOf("Pendente", "Em andamento", "Concluído")

    var selectedPriority: String = ""
    var priorityArray = arrayOf("Baixa", "Média", "Alta")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        btnSave = findViewById(R.id.btnSave)

        txtName = findViewById(R.id.txtName)
        txtDescription = findViewById(R.id.txtDescription)
        spinnerPriority = findViewById(R.id.spinnerPriority)
        cvDateLimit = findViewById(R.id.cvDateLimit)
        spinnerStatus = findViewById(R.id.spinnerStatus)

        cvDateLimit.setOnDateChangeListener { calendarView: CalendarView, year: Int, month: Int, day: Int ->
            run {
                data = Date(year - 1900, month, day)
            }
        }

        spinnerStatus.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, statusArray)
        spinnerPriority.adapter = ArrayAdapter(this,  android.R.layout.simple_spinner_dropdown_item, priorityArray)

        val spinnerItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                if (parent == spinnerStatus) {
                    selectedStatus = "Pendente"
                } else if (parent == spinnerPriority) {
                    selectedPriority = "Baixa"
                }
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent == spinnerStatus) {
                    selectedStatus = statusArray[position]
                } else if (parent == spinnerPriority) {
                    selectedPriority = priorityArray[position]
                }
            }
        }

        spinnerStatus.onItemSelectedListener = spinnerItemSelectedListener
        spinnerPriority.onItemSelectedListener = spinnerItemSelectedListener


        if (this.intent.getSerializableExtra("task") != null) {
            this.task =
                this.intent.getSerializableExtra("task") as Task
            fillData()
        }

        btnSave.setOnClickListener {
            if (task != null)
            {
                updateTask()
            }
            else
            {
                newTask()
            }
        }
    }

    fun fetchSelectedStatus(): String {
        return selectedStatus
    }

    fun fetchSelectedPriority(): String {
        return selectedPriority
    }

    private fun newTask() {
        if (!txtName.text.toString().isNullOrEmpty())
        {
            val dateLimitMillis = data?.time
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = sdf.format(dateLimitMillis?.let { Date(it) })
            val status = fetchSelectedStatus()
            val priority = fetchSelectedPriority()

            task = Task(
                _id = "-1",
                name = txtName.text.toString(),
                description = txtDescription.text.toString(),
                priority = priority,
                dateLimit = formattedDate,
                status = status
            )


            val retrofitCli = RetrofitClient()
            retrofitCli.taskService.createTask(task!!)
                .enqueue(object: Callback<Task>{
                    override fun onResponse(call: Call<Task>, response: Response<Task>) {
                        Log.i("CreateTasks", "onResponse: " + response.body())
                        Toast.makeText(this@EditTaskActivity, "Tarefa criada com sucesso", Toast.LENGTH_LONG).show()
                        this@EditTaskActivity.finish()
                    }

                    override fun onFailure(call: Call<Task>, t: Throwable) {
                        Log.e("CreateTasks", "onFailure: ", t)
                    }
                })
        }
    }

    private fun fillData() {
        txtName.setText(task?.name)
        txtDescription.setText(task?.description)

        val selectedStatusIndex = statusArray.indexOf(task?.status)
        if (selectedStatusIndex != -1) {
            spinnerStatus.setSelection(selectedStatusIndex)
        }

        val selectedPriorityIndex = priorityArray.indexOf(task?.priority)
        if (selectedPriorityIndex != -1) {
            spinnerPriority.setSelection(selectedPriorityIndex)
        }

        val apiDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateLimit = task?.dateLimit ?: ""

        try {
            val date = apiDateFormat.parse(dateLimit)
            val dateLimitMillis = date?.time ?: 0
            cvDateLimit.date = dateLimitMillis
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }


    private fun updateTask() {
        if (!txtName.text.toString().isNullOrEmpty()) {
            val dateLimitMillis = data?.time
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = sdf.format(dateLimitMillis?.let { Date(it) })

            val taskId = task?._id ?: ""
            val status = fetchSelectedStatus()

            val priority = fetchSelectedPriority()

            task = Task(
                _id = taskId,
                name = txtName.text.toString(),
                description = txtDescription.text.toString(),
                priority = priority,
                dateLimit = formattedDate,
                status = status
            )

            val retrofitCli = RetrofitClient()
            retrofitCli.taskService.updateTask(taskId, task!!)
                .enqueue(object: Callback<Task> {
                    override fun onResponse(call: Call<Task>, response: Response<Task>) {
                        Log.i("UpdateTasks", "onResponse: " + response.body())
                        Toast.makeText(this@EditTaskActivity, "Tarefa atualizada com sucesso", Toast.LENGTH_LONG).show()
                        this@EditTaskActivity.finish()
                    }

                    override fun onFailure(call: Call<Task>, t: Throwable) {
                        Log.e("UpdateTasks", "onFailure: ", t)
                    }
                })
        }
    }
}