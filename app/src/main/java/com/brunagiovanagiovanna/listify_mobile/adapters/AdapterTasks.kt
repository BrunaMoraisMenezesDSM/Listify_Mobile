package com.brunagiovanagiovanna.listify_mobile.adapters

import android.content.Context
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brunagiovanagiovanna.listify_mobile.R
import com.brunagiovanagiovanna.listify_mobile.models.Task

class AdapterTasks(var context: Context, var listTasks: List<Task>) : RecyclerView.Adapter<AdapterTasks.MyViewHolder>() {
    var clickedPosition: Int = -1

    class MyViewHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView),
        View.OnCreateContextMenuListener {
        init {
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            var menuInflater: MenuInflater = MenuInflater(context)
            menuInflater.inflate(R.menu.menu_task, menu)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(context)
        var view = inflater.inflate(R.layout.task_item, parent, false)
        return MyViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task: Task = listTasks.elementAt(position)
        val txtName: TextView = holder.itemView.findViewById(R.id.txtName)
        val txtDescription: TextView = holder.itemView.findViewById(R.id.txtDescription)
        val txtPriority: TextView = holder.itemView.findViewById(R.id.txtPriority)
        val txtSelected: TextView = holder.itemView.findViewById(R.id.selectedDateLimit)
        val txtStatus: TextView = holder.itemView.findViewById(R.id.txtStatus)

        txtName.text = task.name
        txtDescription.text = task.description
        txtPriority.text = task.priority
        txtSelected.text = task.dateLimit
        txtStatus.text = task.status

        holder.itemView.setOnLongClickListener { v ->
            clickedPosition = holder.adapterPosition
            false
        }
    }

    override fun getItemCount(): Int {
        return this.listTasks.size
    }
}