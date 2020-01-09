package com.example.tipster

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tipster.RecyclerView.Item.PersonClass
import com.example.tipster.Util.FireStoreUtil
import com.example.tipster.Util.StorageUtil
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Item
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_messaging.*
import kotlinx.android.synthetic.main.fragment_profile.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Messaging.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Messaging.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Messaging : Fragment() {

    private lateinit var userListenerRegistration: ListenerRegistration

    private var shouldInitRecyclerView = true

    private lateinit var peopleSection: Section

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        // Get users only  who he matchmake
        FireStoreUtil.getCurrentUser { user ->
            userListenerRegistration =
                FireStoreUtil.GetDateChats(user.numberOfPeopleDating , this.activity!!, this::updateRecyclerView)


        }



        return inflater.inflate(R.layout.fragment_messaging, container, false)
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        FireStoreUtil.removeListener(userListenerRegistration)
//        shouldInitRecyclerView = true
//    }

    private fun updateRecyclerView(items: List<com.xwray.groupie.kotlinandroidextensions.Item>) {

        fun init() {
            RecylerMessagingView.apply {
                layoutManager = LinearLayoutManager(this@Messaging.context)
                adapter = GroupAdapter<ViewHolder>().apply {
                    peopleSection = Section(items)
                    add(peopleSection)
                    setOnItemClickListener(onItemClick)
                }
            }

            shouldInitRecyclerView = false
        }

        fun updateItems() = peopleSection.update(items)

        if (shouldInitRecyclerView) {
            init()
        }
        else {
            updateItems()
        }
    }


    private val onItemClick = OnItemClickListener { item, view ->
        if (item is PersonClass) {
            USER_NAME = item.Person.name
            USER_ID = item.UserId
            FireabaseClass.myRef.child(References.ExtraDelete2.toString()).setValue(item.Person.name)

            startActivity(Intent(this.context , ChatActivity::class.java))






        }
    }


}



var  USER_NAME = "USER_NAME"
var USER_ID = "USER_ID"