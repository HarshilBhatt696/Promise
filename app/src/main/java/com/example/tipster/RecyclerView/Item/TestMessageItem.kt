package com.example.tipster.RecyclerView.Item

import android.app.ActionBar
import android.content.Context
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.GravityCompat
import com.example.tipster.FireabaseClass
import com.example.tipster.Model.TextMessage
import com.example.tipster.R
import com.google.firebase.auth.FirebaseAuth
import com.xwray.groupie.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_text_message.*
import kotlinx.android.synthetic.main.main_page_layout.*
import kotlinx.android.synthetic.main.main_page_layout.view.*
import java.text.SimpleDateFormat


class TestMessageItem(val message : TextMessage , cont : Context) : com.xwray.groupie.kotlinandroidextensions.Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
                viewHolder.textView_message_text.text = message.text
        setTime(viewHolder)
        setMessageRootGravity(viewHolder)
    }


    private fun setTime(viewHolder: ViewHolder) {
        val dateformat = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)
        viewHolder.textView_message_time.text = dateformat.format(message.time)
    }


    private fun setMessageRootGravity(viewHolder: ViewHolder) {
            if (message.senderId == FirebaseAuth.getInstance().currentUser?.displayName) {
                viewHolder.message_root.apply {
                    setBackgroundResource(R.drawable.rect_round_primary_color)



                    viewHolder.LinearLayoutGravity.gravity = Gravity.RIGHT
                   // val Lparams = FrameLayout.LayoutParams(wra)
                }



            } else {
                viewHolder.message_root.apply {
                    setBackgroundResource(R.drawable.rect_oval_white)
                     viewHolder.message_root.gravity = Gravity.LEFT



                }
            }



    }
    override fun getLayout()= R.layout.item_text_message

    override fun isSameAs(other: Item<*>?): Boolean {

        if (other !is TestMessageItem) {
            return false
        }
        if (this.message != other.message) {
            return false
        }
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? TestMessageItem)
    }
    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + this.hashCode()
        return result
    }

}