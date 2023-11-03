package com.brunagiovanagiovanna.listify_mobile.api

import com.brunagiovanagiovanna.listify_mobile.models.Task
import retrofit2.Call
import retrofit2.http.*

interface ServiceTask {
    // Rota para obter uma tarefa por ID
    @GET("tasks/{id}")
    fun getTask(@Path("id") id: String): Call<Task>

    // Rota para obter todas as tarefas
    @GET("tasks")
    fun getAllTasks(): Call<List<Task>>

    // Rota para obter tarefas pendentes
    @GET("tasks/pending")
    fun getTaskPending(): Call<List<Task>>

    // Rota para obter tarefas em andamento
    @GET("tasks/in-progress")
    fun getTaskInProgress(): Call<List<Task>>

    // Rota para obter tarefas concluídas
    @GET("tasks/completed")
    fun getTaskCompleted(): Call<List<Task>>

    // Rota para criar uma tarefa
    @POST("tasks")
    fun createTask(@Body task: Task): Call<Task>

    // Rota para atualizar uma tarefa por ID
    @PUT("tasks/{id}")
    fun updateTask(@Path("id") id: String, @Body task: Task): Call<Task>

    // Rota para excluir uma tarefa por ID
    @DELETE("tasks/{id}")
    fun deleteTask(@Path("id") id: String): Call<Void>
}