package us.wabash.weathernews.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_weather_info.view.*
import us.wabash.weathernews.R
import us.wabash.weathernews.ScrollingActivity
import us.wabash.weathernews.data.AppDatabase
import us.wabash.weathernews.data.Todo
import us.wabash.weathernews.touch.TodoTouchHelperCallback
import kotlinx.android.synthetic.main.city_row.view.*
import us.wabash.weathernews.WeatherInfo
import us.wabash.weathernews.api.WeatherApi
import us.wabash.weathernews.data.WeatherResult
import java.util.*

class TodoAdapter : RecyclerView.Adapter<TodoAdapter.ViewHolder>, TodoTouchHelperCallback {

    var todoList = mutableListOf<Todo>()
    val context: Context
    constructor(context: Context, todos: List<Todo>){
        this.context = context

        todoList.addAll(todos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val todoRow = LayoutInflater.from(context).inflate(
            R.layout.city_row, parent, false
        )
        return ViewHolder(todoRow)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var todo = todoList.get(holder.adapterPosition)

        holder.tvTextCity.text = todo.todoText


        holder.btnDelete.setOnClickListener {
            deleteTodo(holder.adapterPosition)
        }


        holder.itemView.setOnClickListener {

            val intent = Intent(this.context, WeatherInfo::class.java)

            intent.putExtra("cityName", holder.tvTextCity.text.toString())


            context.startActivity(intent)

        }
    }


    fun updateTodo(todo: Todo) {
        Thread {
            AppDatabase.getInstance(context).todoDao().updateTodo(todo)
        }.start()
    }

    fun updateTodoOnPosition(todo: Todo, index: Int) {
        todoList.set(index, todo)
        notifyItemChanged(index)
    }


    fun deleteTodo(index: Int){
        Thread{
            AppDatabase.getInstance(context).todoDao().deleteTodo(todoList[index])

            (context as ScrollingActivity).runOnUiThread {
                todoList.removeAt(index)
                notifyItemRemoved(index)
            }
        }.start()
    }

    fun deleteAllTodos() {
        Thread {
            AppDatabase.getInstance(context).todoDao().deleteAllTodo()

            (context as ScrollingActivity).runOnUiThread {
                todoList.clear()
                notifyDataSetChanged()
            }
        }.start()
    }

    fun addTodo(todo: Todo) {
        todoList.add(todo)
        notifyItemInserted(todoList.lastIndex)
    }


    override fun onDismissed(position: Int) {
        deleteTodo(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(todoList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTextCity = itemView.etCityName

        val btnDelete = itemView.btnDelete

        val btnDetails = itemView.btnDetails
        
    }

}
