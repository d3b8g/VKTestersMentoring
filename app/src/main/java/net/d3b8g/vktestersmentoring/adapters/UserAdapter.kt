package net.d3b8g.vktestersmentoring.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.db.UserData.UserData
import net.d3b8g.vktestersmentoring.interfaces.Login

class UserAdapter(val login:Login) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var dataUser: ArrayList<UserData> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun setUser(arrayList: ArrayList<UserData>){
        dataUser.clear()
        dataUser.addAll(arrayList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.cell_user, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UserViewHolder) holder.bind(dataUser[position])
    }

    override fun getItemCount(): Int = dataUser.size

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user:UserData){
            val name = itemView.findViewById<TextView>(R.id.user_name)
            val avatar = itemView.findViewById<CircleImageView>(R.id.user_image)
            val percent = itemView.findViewById<TextView>(R.id.user_percent)
            name.text = user.username
            Picasso.get()
                .load(user.avatar)
                .into(avatar)
            percent.text = user.scope.toString()
            itemView.setOnClickListener {
                login.loginUser(adapterPosition)
            }
        }
    }
}