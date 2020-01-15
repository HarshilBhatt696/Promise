package com.example.tipster.RecyclerView.Item




import android.annotation.SuppressLint
import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tipster.FireabaseClass
import com.example.tipster.Flide.GlideApp

import com.example.tipster.R
import com.example.tipster.Util.StorageUtil
import com.firebase.ui.auth.data.model.User
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_messaging.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.match_make_cells.*
import kotlinx.android.synthetic.main.messaging_cell.*
import kotlinx.android.synthetic.main.request_cell.*

class MatchMakinProfile(val Person : com.example.tipster.Model.User , val UserId : String , private val Cont : Context) : Item() {

    @SuppressLint("RestrictedApi")
    override fun bind(viewHolder: ViewHolder, position: Int) {
     //   viewHolder.UserName.text = Person.name
       // FireabaseClass.myRef.child(Person.name).setValue(Person.gender)


        viewHolder.textView7.text = Person.Bio
        if (Person.profilePicturePath != null) {


            GlideApp.with(Cont)
                .load(StorageUtil.pathToReference(Person.profilePicturePath))
                .apply(RequestOptions.circleCropTransform())
                .into(viewHolder.imageView2)
        }


    }

    override fun getLayout() = R.layout.match_make_cells

}