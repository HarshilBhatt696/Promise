package com.example.tipster.RecyclerView.Item

import android.annotation.SuppressLint
import android.content.Context
import com.bumptech.glide.Glide
import com.example.tipster.R
import com.example.tipster.Util.StorageUtil
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.messaging_cell.*
import kotlinx.android.synthetic.main.request_cell.*

class RequestClass(val Person : com.example.tipster.Model.User , val UserId : String , private val Cont : Context) : Item() {

    @SuppressLint("RestrictedApi")
    override fun bind(viewHolder: ViewHolder, position: Int) {

//        FireabaseClass.myRef.child(Person.Name).setValue(Person.Gender)

        viewHolder.UserNameRequestText.text = Person.name
       // viewHolder.BioRequest.text = Person.Bio

        if (Person.profilePicturePath != null) {
            Glide.with(Cont)
                .load(StorageUtil.pathToReference(Person.profilePicturePath))
                .into(viewHolder.ReqeustImage)
        }

    }

    override fun getLayout() = R.layout.request_cell

}