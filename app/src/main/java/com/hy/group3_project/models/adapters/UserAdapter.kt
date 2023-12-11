package com.hy.group3_project.models.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.hy.group3_project.R
import com.hy.group3_project.models.users.User

class UserAdapter (
    private val userList:List<User>,
    private val addUserToDatabase: (Int) -> Unit,
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder (itemView) {
        init {
            itemView.findViewById<Button>(R.id.btn_sign_up).setOnClickListener {
                addUserToDatabase(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return userList.size
    }
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

    }
}