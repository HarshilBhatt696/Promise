package com.example.tipster.Fragment

import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tipster.Model.TextMessage
import com.example.tipster.MyUser
import com.example.tipster.R
import com.example.tipster.RecyclerView.Item.RequestClass
import com.example.tipster.Util.FireStoreUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_request_dates.*
import com.xwray.groupie.OnItemClickListener
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.date_info.view.*
import kotlinx.android.synthetic.main.request_bottom_navigation.view.*
import kotlinx.android.synthetic.main.request_cell.view.*
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Request_dates.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Request_dates.newInstance] factory method to
 * create an instance of this fragment.
 *
 */

// Requests that I recieved.

class Request_dates : Fragment() {


    private lateinit var userListenerRegistration: ListenerRegistration

    private var shouldInitRecyclerView = true

    private lateinit var peopleSection: Section

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_request_dates, container, false)
        view.apply {

            Applied() // Reload Video

        }
        return view
    }


    fun Applied() {






                userListenerRegistration =
                    FireStoreUtil.addRequestListener(
                        MyUser.requests,
                        this.context!!,
                        this::updateRecyclerView
                    ) /// Todo : add the requests received required




    }


    private fun updateRecyclerView(items: List<com.xwray.groupie.kotlinandroidextensions.Item>) {

        fun init() {
            RequestRecycler.apply {
                layoutManager = LinearLayoutManager(this@Request_dates.context)
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
        } else {
            updateItems()
        }

    }


    private val onItemClick = OnItemClickListener { item, view ->

        val Contenty = BottomSheetDialog(this.context!!)
        val Dialog = LayoutInflater.from(context).inflate(R.layout.request_bottom_navigation, null)
        Contenty.setContentView(Dialog)

        Contenty.show()






        if (item is RequestClass) {
            Dialog.AcceptIt.setOnClickListener {
                FireStoreUtil.getCurrentUser { user ->


                    FireStoreUtil.AcceptIt(
                        item.Person.name,
                        item.Person.numberOfPeopleDating,
                        user.name,
                        user.requests,
                        user.numberOfPeopleDating
                    )

                    // Once you Accept it a automatic Message will be sent to the person you selected
                    FireStoreUtil.getOrCreateChatChannel(item.UserId) { channelId ->


                        SendItems.setOnClickListener {
                            val messageToSends = TextMessage(
                                EditTextMessages.text.toString(),
                                Calendar.getInstance().time,
                                FirebaseAuth.getInstance().currentUser!!.displayName!!,
                                item.UserId,
                                user.name
                            )
                            EditTextMessages.setText("")
                            FireStoreUtil.sendMessage(messageToSends, channelId)
                        }


                        // FireStoreUtil.AcceptRequests(item.Person.name , user.name, item.Person.numberOfPeopleDating, user.numberOfPeopleDating, user.requests)
                        userListenerRegistration =
                            FireStoreUtil.addRequestListener(
                                user.requests,
                                this.context!!,
                                this::updateRecyclerView
                            )


                    }


                }

            }

            // Decline Invitation
            Dialog.DeclineIt.setOnClickListener {
                FireStoreUtil.getCurrentUser { user ->
                    FireStoreUtil.DeclineRequest(item.Person.name, user.name, item.Person.NotToBeViwedAnywhere, user.numberOfPeopleDating, user.requests)
                }

        }



        }

    }
}





