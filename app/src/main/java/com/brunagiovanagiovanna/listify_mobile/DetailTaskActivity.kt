package com.brunagiovanagiovanna.listify_mobile

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.brunagiovanagiovanna.listify_mobile.api.RetrofitClient
import com.brunagiovanagiovanna.listify_mobile.models.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class DetailTaskActivity : AppCompatActivity() {
    var task: Task? = null
    lateinit var txtName: TextView
    lateinit var txtDescription: TextView
    lateinit var txtPriority: TextView
    lateinit var txtSelected: TextView
    lateinit var txtStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_task)

        txtName = findViewById(R.id.txtName)
        txtDescription = findViewById(R.id.txtDescription)
        txtPriority = findViewById(R.id.txtPriority)
        txtSelected = findViewById(R.id.txtSelected)
        txtStatus = findViewById(R.id.txtStatus)

        if (this.intent.getSerializableExtra("task") != null) {
            this.task =
                this.intent.getSerializableExtra("task") as Task
            fillData()
        }
    }

    private fun fillData() {
        txtName.setText(task?.name)
        txtDescription.setText(task?.description)
        txtPriority.setText(task?.priority)
        txtSelected.text = task?.dateLimit
        txtStatus.setText(task?.status)
    }

    private fun getTask() {
        if (!txtName.text.toString().isNullOrEmpty())
        {
            val retrofitCli = RetrofitClient()
            retrofitCli.taskService.getTask(task!!._id)
                .enqueue(object: Callback<Task>{
                    override fun onResponse(call: Call<Task>, response: Response<Task>) {
                        Log.i("DetailsTasks", "onResponse: " + response.body())
                        this@DetailTaskActivity.finish()
                    }

                    override fun onFailure(call: Call<Task>, t: Throwable) {
                        Log.e("DetailsTasks", "onFailure: ", t)
                    }
                })
        }
    }
}