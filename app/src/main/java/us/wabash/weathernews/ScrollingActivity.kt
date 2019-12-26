package us.wabash.weathernews

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import us.wabash.weathernews.adapter.TodoAdapter
import us.wabash.weathernews.api.WeatherApi
import us.wabash.weathernews.data.AppDatabase
import us.wabash.weathernews.data.Todo
import us.wabash.weathernews.touch.TodoReyclerTouchCallback
import kotlinx.android.synthetic.main.activity_scrolling.*
import kotlinx.android.synthetic.main.city_row.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt

class ScrollingActivity : AppCompatActivity(), TodoDialog.TodoHandler {

    companion object {
        const val KEY_TODO = "KEY_TODO"
        const val KEY_STARTED = "KEY_STARTED"
        const val TAG_TODO_DIALOG = "TAG_TODO_DIALOG"
        const val TAG_TODO_EDIT = "TAG_TODO_EDIT"
    }

    lateinit var todoAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)

        setSupportActionBar(toolbar)

        initRecyclerView()

        fab.setOnClickListener {
            showAddTodoDialog()
        }

        fabDeleteAll.setOnClickListener {
            todoAdapter.deleteAllTodos()
        }

        if (!wasStartedBefore()) {
            MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.fab)
                .setPrimaryText("New item")
                .setSecondaryText("Click here to create new items")
                .show()
            saveWasStarted()
        }

    }

    fun saveWasStarted() {
        var sharedPref =
            PreferenceManager.getDefaultSharedPreferences(this)
        var editor = sharedPref.edit()
        editor.putBoolean(KEY_STARTED, true)
        editor.apply()
    }

    fun wasStartedBefore() : Boolean {
        var sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

        return sharedPref.getBoolean(KEY_STARTED, false)
    }



    private fun initRecyclerView() {
        Thread {
            var todos = AppDatabase.getInstance(this@ScrollingActivity).todoDao().getAllTodo()

            runOnUiThread {
                todoAdapter = TodoAdapter(this, todos)
                recyclerTodo.adapter = todoAdapter

                var itemDecorator = DividerItemDecoration(
                    this,
                    DividerItemDecoration.VERTICAL
                )
                recyclerTodo.addItemDecoration(itemDecorator)



                val callback = TodoReyclerTouchCallback(todoAdapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(recyclerTodo)
            }

        }.start()

    }

    fun showAddTodoDialog() {
        TodoDialog().show(supportFragmentManager, TAG_TODO_DIALOG)
    }

    var editIndex: Int = -1




    fun saveTodo(todo: Todo) {
        Thread {
            var newId =
                AppDatabase.getInstance(this@ScrollingActivity).todoDao().addTodo(todo)

            todo.todoId = newId

            runOnUiThread {
                todoAdapter.addTodo(todo)
            }

        }.start()
    }

    override fun todoCreated(item: Todo) {
        saveTodo(item)
    }

    override fun todoUpdated(item: Todo) {
        Thread {
            AppDatabase.getInstance(
                this@ScrollingActivity).
                todoDao().updateTodo(item)

            runOnUiThread {
                todoAdapter.updateTodoOnPosition(item, editIndex)
            }
        }.start()
    }
}
