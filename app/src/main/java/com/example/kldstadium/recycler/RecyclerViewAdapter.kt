package com.example.kldstadium.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.kldstadium.R
import com.example.kldstadium.models.User
import com.example.kldstadium.views.Data
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.user_card.view.*

class RecyclerViewAdapter(): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    var button1: ( (Pair<String, Int>) -> Unit)? = null
    var button2: ( (Pair<String, Int>) -> Unit)? = null

    private var users = emptyList<User>()
    private var keys = emptyList<String>()

    fun update(data: Data) {
        this.users = data.users
        this.keys = data.keys
        notifyDataSetChanged()
    }

     class ViewHolder(v: View,
                      private val button1: ( (Pair<String, Int>) -> Unit)?,
                     private val button2: ( (Pair<String, Int>) -> Unit)?
     ): RecyclerView.ViewHolder(v) {
        private var view: View = v
        private var user: User? = null
        companion object {
            //5
            private val USER_KEY = "USER"
        }


        fun bindUser(user: User, userKey: String) {

            val db = Firebase.firestore
            this.user = user
            view.avatar_image.load("https://ohcat.ru/assets/images/img_gallery/115.jpg"){
                transformations(RoundedCornersTransformation(300f))
            }
            view.fullName_text.text = user.name
            view.dateOfVisit_text.text = user.date.toString()
            view.description_text.text = user.purpose

            view.action_button_1.setOnClickListener {

                val newState:Int = user.state!! + 1;
                button1?.invoke(userKey to newState)

            }

            view.action_button_2.setOnClickListener {
                button2?.invoke(userKey to 666)

            }



        }





    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_card, parent, false)
        return ViewHolder(view, this.button1, this.button2)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemUser = users[position]
        val userKey = keys[position]
        holder.bindUser(itemUser, userKey)
    }

    override fun getItemCount(): Int {
        return users.size
    }


}