package com.example.kldstadium.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.example.kldstadium.Fr1
import com.example.kldstadium.Fr2
import com.example.kldstadium.R
import com.example.kldstadium.ViewPagerAdapter
import com.example.kldstadium.extensions.Extensions.toast

import com.example.kldstadium.recycler.RecyclerViewAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import com.google.firebase.firestore.QuerySnapshot
import com.example.kldstadium.models.User
import com.example.kldstadium.utils.FirebaseUtils.firebaseAuth

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val adapter: RecyclerViewAdapter = RecyclerViewAdapter()
    private lateinit var db: FirebaseFirestore
    private var state:Int = 0
    lateinit var viewPager: ViewPager
    lateinit var tabLayout: TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        viewPager = findViewById(R.id.pager)
        tabLayout = findViewById(R.id.tab_layout)
        setAdapters()
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerview.layoutManager = LinearLayoutManager(this)

        swipe_refresh_layout.setOnRefreshListener(this)


        val user: FirebaseUser? = firebaseAuth.currentUser

        state = when(user!!.email) {
            "nachalnik@stadium.ru" -> 0
            "director@stadium.ru" -> 1
            "sb@stadium.ru" -> 2
            "ohrana@stadium.ru" -> 3

            else -> 666
        }



        db = Firebase.firestore
        queryAllUsers(state)


        btnSignOut.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, CreateAccountActivity::class.java))
            toast("signed out")
            finish()
        }
        recyclerview.adapter = adapter

        adapter.button1 = { (userKey, type) ->
            db.collection("tasks").document(userKey).update("state", type)
            queryAllUsers(state)
        }
        adapter.button2 = { (userKey, type) ->
            db.collection("tasks").document(userKey).update("state", type)
            queryAllUsers(state)
        }

//        if(adapter.itemCount == 0)
//        {
//            recyclerview.visibility = View.GONE
//            viewEmptyUsers1.visibility = View.VISIBLE
//        }


    }
    private fun setAdapters() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(Fr1(), "Пешком")
        adapter.addFragment(Fr2(), "На авто")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(pager)
    }
    private fun queryAllUsers(state: Int) {
        //I've already tried to make a local list and return that, however,
        //the compiler would force me to declare the list as final here, and it wouldn't solve anything.
        db.collection("tasks").whereEqualTo("state", state)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val data = task.result?.documents.orEmpty().map { document ->
                        Log.d("TAG", document.id + " => " + document.data)
                        val user: User = document.toObject(User::class.java)!!
                        user to document.id
                    }.let { data ->
                        Data(
                            users = data.map { it.first },
                            keys = data.map { it.second }
                        )
                    }
                    Log.d("TAG", "After for loop: $data")
                    adapter.update(data)
                    //Here the list is OK. Filled with projects.
                    //I'd like to save the state of the list from here
                } else {
                    Log.d("TAG", "Error getting document: ", task.exception)
                    Toast.makeText(applicationContext, "GOPA", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onRefresh() {

//        if(adapter.itemCount == 0)
//        {
//            recyclerView.visibility = View.VISIBLE
//            viewEmptyUsers1.visibility = View.VISIBLE
//        }
//        else {
//            recyclerView.visibility = View.VISIBLE
//            viewEmptyUsers1.visibility = View.INVISIBLE
//
//        }

        queryAllUsers(state)
        swipe_refresh_layout.isRefreshing = false
    }


}

data class Data(
    val users: List<User>,
    val keys: List<String>
)

