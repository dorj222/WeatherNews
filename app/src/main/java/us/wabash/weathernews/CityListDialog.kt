package us.wabash.weathernews



import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import us.wabash.weathernews.data.Todo
import kotlinx.android.synthetic.main.new_todo_dialog.view.*

class TodoDialog : DialogFragment() {

    interface TodoHandler {
        fun todoCreated(item: Todo)
        fun todoUpdated(item: Todo)
    }

    private lateinit var todoHandler: TodoHandler

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is TodoHandler) {
            todoHandler = context
        } else {
            throw RuntimeException(
                "The activity does not implement the TodoHandlerInterface")
        }
    }


    private lateinit var etTodoText: EditText

   

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("City Name")

        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.new_todo_dialog, null
        )


        etTodoText = rootView.etTodo
        builder.setView(rootView)

        builder.setPositiveButton("OK") {
                dialog, witch -> // empty
        }

        return builder.create()
    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (etTodoText.text.isNotEmpty()) {

                    handleCity()

                dialog.dismiss()
            } else {
                etTodoText.error = "This field can not be empty"
            }
        }
    }

    private fun handleCity() {
        todoHandler.todoCreated(
            Todo(
                null,
                "",
                etTodoText.text.toString(),
                false
            )
        )
    }


}

