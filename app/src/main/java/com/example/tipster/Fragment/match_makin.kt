package com.example.tipster.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
//import android.support.design.widget.BottomSheetDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.example.tipster.*
import com.example.tipster.RecyclerView.Item.MatchMakinProfile
import com.example.tipster.RecyclerView.Item.PersonClass
import com.example.tipster.Util.FireStoreUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.date_info.view.*
import kotlinx.android.synthetic.main.fragment_match_makin.*
import kotlinx.android.synthetic.main.fragment_match_makin.view.*
import kotlinx.android.synthetic.main.fragment_messaging.*
import kotlinx.android.synthetic.main.match_make_cells.view.*
import com.xwray.groupie.OnItemClickListener
import kotlinx.android.synthetic.main.date_info.*

class match_makin : androidx.fragment.app.Fragment() {


    private lateinit var userListenerRegistration: ListenerRegistration

    private var shouldInitRecyclerView = true

    private lateinit var peopleSection: Section
    var NoOfPeopleMatched  = ArrayList<String>()// Usernames of people matched

    var DatesName = "" // TODO : ADD USER NAME


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?) : View? {

        val view = inflater.inflate(R.layout.fragment_match_makin, container, false)
        view.apply {

            MatchMake.setOnClickListener {


                MatchMakeClicker()

                UploadList()
            }


        }





        return view
    }


    fun UploadList() {

        userListenerRegistration =
            FireStoreUtil.addMatchMakers(this.activity!!, NoOfPeopleMatched, this::updateRecyclerView) /// Todo : add the dates required


    }

    fun MatchMakeClicker() {


      NoOfPeopleMatched = DstingClass.GetNumberOfPeopleMatched(3)




    }
//    override fun onDestroyView() {
//        super.onDestroyView()
//        FireStoreUtil.removeListener(userListenerRegistration)
//        shouldInitRecyclerView = true
//    }


//    fun ListViewUploader() { // Give List view its values
//
//        MainList.layoutManager =  LinearLayoutManager(this@match_makin.context) /* StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL) *///LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        MainList.adapter = MatchMakingAdopter(this@match_makin.context!!, R.layout.match_make_cells , NoOfPeopleMatched)
//
//
//    }

    private fun updateRecyclerView(items: List<com.xwray.groupie.kotlinandroidextensions.Item>) {

        fun init() {
            MainList.apply {
                layoutManager = LinearLayoutManager(this@match_makin.context)
                adapter = GroupAdapter<ViewHolder>().apply {
                    peopleSection = Section(items)
                    add(peopleSection)
                    setOnItemClickListener(onItemClick)
                }


            }

            shouldInitRecyclerView = false
        }

        fun updateItems() = peopleSection.update(items)

        if (shouldInitRecyclerView)
            init()
        else
            updateItems()

    }


    private val onItemClick = OnItemClickListener { item, view ->
      // if (item is PersonClass) { // TODO : BETTER ADD THIS COMMENT BACK ITS IMPORTANT

            val Contenty = BottomSheetDialog(this.context!!)


            val Dialog = LayoutInflater.from(context).inflate(R.layout.date_info , null)
            Contenty.setContentView(Dialog)

            Contenty.show()

            Dialog.Cancel.setOnClickListener {
                Contenty.dismiss()
            }

        if (item is MatchMakinProfile) {
           Dialog.UserDescribe.text = item.Person.Bio
            Dialog.UserNames.text = item.Person.name

            Dialog.MatchMakeChat.setOnClickListener {

                FireStoreUtil.AddToDatingSystem(item.Person.name)

                FireStoreUtil.getCurrentUser { user ->
                    // Sends request to the Date Chat User to chat with you and talk to you.
                    FireStoreUtil.UpdateOtherUsersInFO(item.Person.name , user.name , user.requests) // Todo check if it works
                }


            }

        }










        }




}


//
//class MatchMakingAdopter(val context: Context , val cell : Int , val LoversList : ArrayList<String>) : androidx.recyclerview.widget.RecyclerView.Adapter<MatchMakingAdopter.ViewHolder>() {
//
//    // THIS IS THE CONTENT
//
//
//    // Gets the number of animals in the list
//
//
//    override fun getItemCount(): Int {
//
//        return LoversList.size
//    }
//
//    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
//        return ViewHolder(LayoutInflater.from(context).inflate(cell, p0, false))
//    }
//
//    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
//
//        // Bottom Navigation
//       p0.LovePhoto.setOnClickListener {
//                val Contenty = BottomSheetDialog(context)
//
//
//            val Dialog = LayoutInflater.from(context).inflate(R.layout.date_info , null)
//            Contenty.setContentView(Dialog)
//            Dialog.UserDescribe.text = "For now its fine"
//
//
//
//            p0.Cancel.setOnClickListener {
//                Contenty.dismiss()
//            }
//        }
//
//    }
//
//
//    //    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
//////        UserN .text = items.get(p0)
////        print("Jey")
////    }
//    class ViewHolder (view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
//        val LovePhoto = view.imageView2
//        val Cancel = view.Cancel
//    }
//}




