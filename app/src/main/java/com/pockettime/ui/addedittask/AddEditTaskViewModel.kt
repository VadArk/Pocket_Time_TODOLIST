package com.pockettime.ui.addedittask

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pockettime.data.Task
import com.pockettime.data.TaskDao
import com.pockettime.ui.ADD_TASK_RES_OK
import com.pockettime.ui.EDIT_TASK_RES_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.sql.Date
import java.text.DateFormat

class AddEditTaskViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val task = state.get<Task>("task")

    var taskName = state.get<String>("taskName") ?: task?.name ?: ""
        set(value) {
            field = value
            state.set("taskName", value)
        }
    var taskDescription = state.get<String>("taskDescription") ?: task?.description ?: ""
        set(value) {
            field = value
            state.set("taskDescription", value)
        }
//    var taskCategory = state.get<String>("taskCategory") ?: task?.category ?: ""
//        set(value) {
//            field = value
//            state.set("taskCategory", value)
//        }
//    var taskDate = state.get<Long>("taskDate") ?: task?.date ?: ""
//        set(value) {
//            field = value
//            state.set("taskDate", value)
//        }
    var taskImportance = state.get<Boolean>("taskImportance") ?: task?.important ?: false
        set(value) {
            field = value
            state.set("taskImportance", value)
        }
    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()

    fun onSaveClick() {
        if (taskName.isBlank()) {
            showInvalidInputMessage("Name cannot be empty")
            return
        }
        if (task != null) {
            val updatedTask = task.copy(
                name = taskName,
                description = taskDescription,
                important = taskImportance,
//                date = taskDate as Long,
//                category = taskCategory

            )
            updateTask(updatedTask)
        } else {
            val newTask =
                Task(name = taskName, description = taskDescription, important = taskImportance/*, date = taskDate as Long, category = taskCategory*/)
            createTask(newTask)
        }
    }

    private fun createTask(task: Task) = viewModelScope.launch {
        taskDao.insert(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavBackWithPosRes(ADD_TASK_RES_OK))
    }

    private fun updateTask(task: Task) = viewModelScope.launch {
        taskDao.update(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavBackWithPosRes(EDIT_TASK_RES_OK))
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addEditTaskEventChannel.send(AddEditTaskEvent.ShowInvalidInputMessage(text))
    }

    sealed class AddEditTaskEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditTaskEvent()
        data class NavBackWithPosRes(val result: Int) : AddEditTaskEvent()
    }

}