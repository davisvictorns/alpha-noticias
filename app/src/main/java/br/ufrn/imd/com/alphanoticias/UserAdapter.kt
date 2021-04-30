package br.ufrn.imd.com.alphanoticias

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class UserAdapter(
    private val mCtx: Context,
    private val layoutResId: Int,
    private val userList: List<User>
) :
    ArrayAdapter<User>(mCtx, layoutResId, userList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val userName = view.findViewById<TextView>(R.id.userName)
        val userCategory = view.findViewById<TextView>(R.id.userCategory)

        val user = userList[position]

        userName.text = user.name
        userCategory.text = user.category

        return view
    }
}