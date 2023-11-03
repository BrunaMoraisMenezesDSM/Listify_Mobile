package com.brunagiovanagiovanna.listify_mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brunagiovanagiovanna.listify_mobile.adapters.AdapterTasks
import com.brunagiovanagiovanna.listify_mobile.api.RetrofitClient
import com.brunagiovanagiovanna.listify_mobile.api.ServiceTask
import com.brunagiovanagiovanna.listify_mobile.models.Task
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var recyclerTasks: RecyclerView
    lateinit var btnCreate: FloatingActionButton
    lateinit var chipGroupStatus: ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerTasks = findViewById(R.id.recyclerTasks)
        btnCreate = findViewById(R.id.fabCreate)
        chipGroupStatus = findViewById(R.id.chipGroupStatus)

        chipGroupStatus.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chipPendente -> filterTasksByStatus("Pendente")
                R.id.chipEmAndamento -> filterTasksByStatus("Em andamento")
                R.id.chipConcluido -> filterTasksByStatus("Concluído")
                R.id.chipTodos -> filterTasksByStatus("Todos")
            }
        }
        recyclerTasks.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        onResume()

        btnCreate.setOnClickListener { openRegister() }
    }

    override fun onResume() {
        super.onResume()

        var retrofitCli: RetrofitClient = RetrofitClient()
        retrofitCli.taskService.getAllTasks().enqueue(
            object: Callback<List<Task>> {
                override fun onResponse(
                    call: Call<List<Task>>,
                    response: Response<List<Task>>
                ) {
                    if (response.body() != null) {
                        var adapter: AdapterTasks =
                            AdapterTasks(this@MainActivity, response.body()!!)
                        recyclerTasks.adapter = adapter
                    }
                }

                override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                    Log.e("API", "Falha ao carregar tarefas", t)
                }
            }
        )
    }

    private fun openRegister() {
        var intent: Intent = Intent(this, EditTaskActivity::class.java)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1 && resultCode == 1)
        {
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val adapter: AdapterTasks = recyclerTasks.adapter as AdapterTasks
        val position: Int = adapter.clickedPosition
        var task: Task? = null

        if(position >= 0)
        {
            task = adapter.listTasks.get(position)
        }

        if(item.itemId == R.id.menu_item_edit)
        {
            if(task != null)
            {
                val intent: Intent = Intent(this, EditTaskActivity::class.java)
                intent.putExtra("task", task)
                startActivity(intent)
            }
        }
        else if(item.itemId == R.id.menu_item_delete)
        {
            if (task != null) {
                val retrofitCli = RetrofitClient()
                retrofitCli.taskService.deleteTask(task._id)
                    .enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            Log.i("DeleteTasks", "onResponse: " + response.body())
                            Toast.makeText(this@MainActivity, "Tarefa excluída com sucesso", Toast.LENGTH_LONG).show()
                            onResume()
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.e("DeleteTasks", "onFailure: ", t)
                        }
                    })
            }
        }
        else if(item.itemId == R.id.menu_item_detail)
        {
            if (task != null) {
                val intent: Intent = Intent(this, DetailTaskActivity::class.java)
                intent.putExtra("task", task)
                startActivity(intent)
            }
        }
        return true
    }

    private fun filterTasksByStatus(status: String) {
        val retrofitCli: RetrofitClient = RetrofitClient()
        val service: ServiceTask = retrofitCli.taskService

        val call: Call<List<Task>> = when (status) {
            "Pendente" -> service.getTaskPending()
            "Em andamento" -> service.getTaskInProgress()
            "Concluído" -> service.getTaskCompleted()
            "Todos" -> service.getAllTasks()
            else -> service.getAllTasks()
        }

        call.enqueue(object : Callback<List<Task>> {
            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                if (response.isSuccessful) {
                    val tasks: List<Task>? = response.body()
                    if (tasks != null) {
                        val adapter = AdapterTasks(this@MainActivity, tasks)
                        recyclerTasks.adapter = adapter
                    }
                } else {
                    Log.e("API", "Falha ao carregar tarefas com status $status")
                }
            }

            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                Log.e("API", "Falha ao carregar tarefas com status $status", t)
            }
        })
    }
}