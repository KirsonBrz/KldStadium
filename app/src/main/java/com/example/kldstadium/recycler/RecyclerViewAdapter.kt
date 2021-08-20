package com.example.kldstadium.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.kldstadium.R
import com.example.kldstadium.models.User
import kotlinx.android.synthetic.main.user_card.view.*

class RecyclerViewAdapter(private val users: ArrayList<User>): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        private var view: View = v
        private var user: User? = null
        companion object {
            //5
            private val USER_KEY = "USER"
        }


        fun bindUser(user: User) {
            this.user = user
            view.avatar_image.load("https://ohcat.ru/assets/images/img_gallery/115.jpg");
            view.fullName_text.text = user.name.toString()
            view.dateOfVisit_text.text = user.date.toString()
            view.description_text.text = user.purpose.toString()
        }





    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemUser = users[position]
        holder.bindUser(itemUser)
    }

    override fun getItemCount(): Int {
        return users.size
    }


}