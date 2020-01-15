package com.example.tipster.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
//import android.support.design.widget.BottomSheetDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.request.RequestOptions
import com.example.tipster.*
import com.example.tipster.Flide.GlideApp
import com.example.tipster.Model.User
import com.example.tipster.R
import com.example.tipster.RecyclerView.Item.MatchMakinProfile
import com.example.tipster.RecyclerView.Item.PersonClass
import com.example.tipster.Util.FireStoreUtil
import com.example.tipster.Util.StorageUtil
import com.google.android.gms.tasks.Tasks
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.*
import kotlinx.android.synthetic.main.date_info.view.*
import kotlinx.android.synthetic.main.fragment_match_makin.*
import kotlinx.android.synthetic.main.fragment_match_makin.view.*
import kotlinx.android.synthetic.main.fragment_messaging.*
import kotlinx.android.synthetic.main.match_make_cells.view.*
import kotlinx.android.synthetic.main.date_info.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.match_make_cells.*
import kotlin.reflect.KFunction

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
            HEs.playAnimation()
            HEs.visibility = View.GONE
            MatchMake.setOnClickListener {

                HEs.playAnimation()
                HEs.visibility = View.VISIBLE

                MatchMakeClicker(HEs)

                UploadList()
            }


        }





        return view
    }


    fun UploadList() {



        //userListenerRegistration =
//        for (n in 0..1) {
//            userListenerRegistration = FireStoreUtil.addMatchMakers(this.activity!!, NoOfPeopleMatched, this::updateRecyclerView) /// Todo : add the dates required
//        }









    }

    fun MatchMakeClicker(Anim : LottieAnimationView) {
     // NoOfPeopleMatched = DstingClass.GetNumberOfPeopleMatched(3 , Anim)

        for (SameGender in DstingClass.SameGCollected) {
            NoOfPeopleMatched.add(SameGender)
        }


        FireStoreUtil.addMatchMakers(this.activity!!,  DstingClass.GetNumberOfPeopleMatched(3 , Anim), this::updateRecyclerView) /// Todo : add the dates required
        FireStoreUtil.addMatchMakers(this.activity!!,  DstingClass.GetNumberOfPeopleMatched(3 , Anim), this::updateRecyclerView) /// Todo : add the dates required

val Upload = this::updateRecyclerView
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
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)// LinearLayoutManager(this.context) // StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                adapter = GroupAdapter<ViewHolder>().apply {
                    peopleSection = Section(items)
                    add(peopleSection)
                    setOnItemClickListener(onItemClick)
                    FireabaseClass.myRef.child("line 125 match").setValue(NoOfPeopleMatched)


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


            if (item.Person.profilePicturePath != null) {
                GlideApp.with(this)
                    .load(StorageUtil.pathToReference(item.Person.profilePicturePath!!))
                    .apply(RequestOptions.circleCropTransform())
                    .into(Dialog.UserImage)
            }



            Dialog.MatchMakeChat.setOnClickListener {

                FireStoreUtil.AddToDatingSystem(item.Person.name)

                FireStoreUtil.getCurrentUser { user ->
                    // Sends request to the Date Chat User to chat with you and talk to you.
                    FireStoreUtil.UpdateOtherUsersInFO(item.Person.name , user.name , user.requests , user.NotToBeViwedAnywhere) // Todo check if it works
                    FireStoreUtil.addMatchMakers(this.activity!!, ArrayList<String>(), this::updateRecyclerView) /// Todo : add the dates required
                    Contenty.dismiss()
                    Toast.makeText(this.context , "Request Sent" , Toast.LENGTH_SHORT).show()
                    // In a day only one match make
                    FireabaseClass.SetMatchMakingOff(user.name)
                }


            }

            SendRequests.setOnClickListener {
                FireStoreUtil.AddToDatingSystem(item.Person.name)

                FireStoreUtil.getCurrentUser { user ->
                    // Sends request to the Date Chat User to chat with you and talk to you.
                    FireStoreUtil.UpdateOtherUsersInFO(item.Person.name , user.name , user.requests , user.NotToBeViwedAnywhere) // Todo check if it works
                    FireabaseClass.SetMatchMakingOff(user.name)
                }
                FireStoreUtil.addMatchMakers(this.activity!!, ArrayList<String>(), this::updateRecyclerView) /// Todo : add the dates required
                Toast.makeText(this.context , "Request Sent" , Toast.LENGTH_SHORT).show()


                Contenty.dismiss()
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




