package com.pockettime.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pockettime.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider


@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    class Callback @Inject constructor(
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().taskDao()


//            applicationScope.launch {
//                dao.insert(Task("Wash the dishes","hello", ))
//                dao.insert(Task("Call dad","hello", important = true))
//                dao.insert(Task("Call mam", "hello",completed = true))
//                dao.insert(Task("Call Elon","hello"))
//                dao.insert(Task("Sell used books","hello"))
//                dao.insert(Task("Buy printer catridges","hello"))
//                dao.insert(Task("Wash the dishes","hello", completed = true))
//            }


            //db operations
        }
    }
}