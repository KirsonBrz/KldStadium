package com.example.kldstadium

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.kldstadium.recycler.RecyclerViewAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

import com.google.firebase.firestore.QuerySnapshot
import com.example.kldstadium.models.User

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var users: ArrayList<User>
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        userRecyclerView = findViewById(R.id.recyclerView)

        db = Firebase.firestore

        queryAllUsers()




        adapter = RecyclerViewAdapter(users);


//        userList.withModels {
//        }
    }

    private fun queryAllUsers() {
        users = ArrayList()
        //I've already tried to make a local list and return that, however,
        //the compiler would force me to declare the list as final here, and it wouldn't solve anything.
        db.collection("tasks")
            .get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot?> { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        // Log.d(TAG, document.id + " => " + document.data)
                        val user: User = document.toObject(User::class.java)
                        users.add(user)
                        adapter.notifyDataSetChanged()
                    }
//                    Log.d(TAG, "After for loop: " + users.toString())
                    //Here the list is OK. Filled with projects.
                    //I'd like to save the state of the list from here
                } else {
                    //Log.d(TAG, "Error getting document: ", task.exception)
                    //Toast.makeText(applicationContext, R.string.error, Toast.LENGTH_SHORT).show()
                }
            })

        //Log.d(TAG, "End of method: " + projects.toString())
        //Oddly enough, this log displays before the other logs.
        //Also, the list is empty here, which is probably what I'm unintentionally feeding into the Recycler View's adapter
    }


//    fun EpoxyRecyclerView.withModels(buildModelsCallback: EpoxyController.() -> Unit) {
//        setControllerAndBuildModels(object : EpoxyController() {
//            override fun buildModels() {
//                buildModelsCallback()
//            }
//        })
//
//    }

}

