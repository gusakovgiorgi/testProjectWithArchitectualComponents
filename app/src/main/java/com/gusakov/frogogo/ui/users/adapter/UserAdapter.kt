package com.gusakov.frogogo.ui.users.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.gusakov.frogogo.R
import com.gusakov.frogogo.model.User
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter(private val userCLicked: (user: User) -> Unit) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    var listItem: List<User> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int = listItem.size


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = listItem[position]
        holder.bind(user)
    }


    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameAndSurnameLabel: TextView = itemView.nameAndSurnameLabel
        val emailLabel: TextView = itemView.emilLabel
        val avatarImg: AppCompatImageView = itemView.avatarImg
        val imageLoadingProgressBar: ProgressBar = itemView.imageLoadingPrb

        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    userCLicked(listItem[adapterPosition])
                }
            }
        }

        fun bind(user: User) {
            emailLabel.text = user.email
            nameAndSurnameLabel.text = "${user.firstName} ${user.lastName}"
            if (Uri.EMPTY != user.avatarUri) {
                avatarImg.setImageDrawable(null)
                imageLoadingProgressBar.visibility = View.VISIBLE
                Picasso.get().load(user.avatarUri)
                    .into(avatarImg, object : Callback {
                        override fun onSuccess() {
                            imageLoadingProgressBar.visibility = View.INVISIBLE
                        }

                        override fun onError(e: Exception?) {
                            imageLoadingProgressBar.visibility = View.INVISIBLE
                            avatarImg.setImageResource(R.drawable.ic_default_profile)
                        }
                    })
            } else {
                avatarImg.setImageResource(R.drawable.ic_default_profile)
            }
        }

    }
}