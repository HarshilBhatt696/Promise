package com.example.tipster.Util

import android.content.Context
import android.content.Intent
import android.util.Log
import bolts.Task
import com.airbnb.lottie.LottieAnimationView
import com.example.tipster.*
import com.example.tipster.Model.*
import com.example.tipster.RecyclerView.Item.MatchMakinProfile
import com.example.tipster.RecyclerView.Item.PersonClass
import com.example.tipster.RecyclerView.Item.RequestClass
import com.example.tipster.RecyclerView.Item.TestMessageItem
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.kotlinandroidextensions.Item
import java.lang.ref.Reference

var New = true
object FireStoreUtil {

    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    val FireabaseUserName = FireabaseClass.mAuth.currentUser!!.displayName
    private val currentUserDocRef: DocumentReference
        get() {

            try {
                val document: DocumentReference =
                    firestoreInstance.document("users/${FirebaseAuth.getInstance().currentUser!!.displayName}")
                return document
            } catch (E : Throwable) {
                print("ADG")
                val document: DocumentReference =
                    firestoreInstance.document("users/${FireabaseUserName}")
                return document
            }
        }

    private val chatChannelCollection = firestoreInstance.collection(References.chatChannels.toString())

    fun initCurrentUserIfFirstTime(CurrentCotext: Context ,onComplete: () -> Unit) {
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
             if (!documentSnapshot.exists()) { // If does not exist


            val CreateNewUser =                                                                                                                             // Refer to data class for User inputs in this
                User(FireabaseClass.UserName, FireabaseClass.Gender, "", null, ArrayList<String>(), mutableListOf() , FirebaseAuth.getInstance().currentUser!!.uid, ArrayList<String>() ,ArrayList<String>() , ArrayList<String>() , ArrayList<String>()  , true  )


            currentUserDocRef.set(CreateNewUser).addOnSuccessListener {
                onComplete()
                val S = (Intent(CurrentCotext, UserInfor::class.java))
                CurrentCotext.startActivity(S)

            }

              }
               else {
                 onComplete()
                 val S = (Intent(CurrentCotext, MainPage::class.java))
                 CurrentCotext.startActivity(S)

             }
           }
        }






    fun MatchMakeSameGender(Bool : Boolean) {
        getCurrentUser { user ->
            val userFieldMap = mutableMapOf<String, Any>()
            userFieldMap["MatchMakeSameGender"] = Bool
            currentUserDocRef.update(userFieldMap)

        }
    }




    fun getOrCreateChatChannel(otherUserId: String,
                               onComplete: (channelId: String) -> Unit) {
        currentUserDocRef.collection("engagedChatChannels")
            .document(otherUserId).get().addOnSuccessListener {
                if (it.exists()) {
                    onComplete(it["channelId"] as String)
                    return@addOnSuccessListener
                }

                val currentUserId = FirebaseAuth.getInstance().currentUser!!.displayName!!

                val newChannel = chatChannelCollection.document()
                newChannel.set(ChatChannel(mutableListOf(currentUserId, otherUserId)))

                currentUserDocRef
                    .collection("engagedChatChannels")
                    .document(otherUserId)
                    .set(mapOf("channelId" to newChannel.id))

                firestoreInstance.collection("users").document(otherUserId)
                    .collection("engagedChatChannels")
                    .document(currentUserId)
                    .set(mapOf("channelId" to newChannel.id))

                onComplete(newChannel.id)
            }
    }
    fun addChatMessagesListener(channelId: String, context: Context,
                                onListen: (List<Item>) -> Unit): ListenerRegistration {
        return chatChannelCollection.document(channelId).collection("messages")
            .orderBy("time")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "ChatMessagesListener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = mutableListOf<Item>()
                querySnapshot!!.documents.forEach {
                    if (it["type"] == MessageType.TEXT) {
                        items.add(TestMessageItem(it.toObject(TextMessage::class.java)!!, context))
                    }
                    else
                          //  items.add(TextMessageItem(it.toObject(ImageMessage::class.java)!!, context))
                    return@forEach
                }
                onListen(items)
            }
    }


    fun UpdateUser( bio : String = "" , profilePicturePath: String? = null) {
        val userFieldMap = mutableMapOf<String, Any>()
        if (bio.isNotBlank()) userFieldMap["Bio"] = bio
        if (profilePicturePath != null)
            userFieldMap["profilePicturePath"] = profilePicturePath
        currentUserDocRef.update(userFieldMap)
    }

    fun getCurrentUser(onComplete: (User) -> Unit) {
        currentUserDocRef.get()
            .addOnSuccessListener {
                onComplete(it.toObject(User::class.java)!!)
            }


    }



    fun UpdateInterest(Arrays : ArrayList<String>) {
        val userFieldMap = mutableMapOf<String, Any>()
        userFieldMap["Interest"] = Arrays
        currentUserDocRef.update(userFieldMap)
    }


    // Adds Requests
    fun UpdateOtherUsersInFO(OtherUserId : String , UserId : String, OtherUserIdRequests : ArrayList<String>  , MineNotShown : ArrayList<String>) {
        val document: DocumentReference = firestoreInstance.document("users/${OtherUserId}")
        val userFieldMap = mutableMapOf<String, Any>()
        val Requests = OtherUserIdRequests
        Requests.add(UserId)
        MineNotShown.add(OtherUserId)




        userFieldMap["requests"] = Requests


        getCurrentUser { user ->
            val MyDocument: DocumentReference = firestoreInstance.document("users/${user.name}")
            val MyUserTable = mutableMapOf<String, Any>()
            MyUserTable["NotToBeViwedAnywhere"] = MineNotShown
            currentUserDocRef.update(MyUserTable)
        }


           document.update(userFieldMap)


    }


    fun AcceptIt(OtherUserId: String , OtherChatTalkingToRequests : ArrayList<String> , myUser : String , MyChatRequests : ArrayList<String> , numberofDating : ArrayList<String>) {

        val document: DocumentReference = firestoreInstance.document("users/${OtherUserId}")
        val userFieldMap = mutableMapOf<String, Any>()
        val Requests = OtherChatTalkingToRequests
        Requests.add(myUser)
        userFieldMap["numberOfPeopleDating"] = Requests
        document.update(userFieldMap)


        // Update my reqquests
        val MyUserField = mutableMapOf<String, Any>()
        val datingAlready = numberofDating
        datingAlready.add(OtherUserId)
        MyUserField["numberOfPeopleDating"] = datingAlready



        // Removes Person from requests
        val RequestReceived = MyChatRequests
        RequestReceived.remove(OtherUserId)
        MyUserField["requests"] = RequestReceived

        currentUserDocRef.update(MyUserField)



    }

    // Accept the request that was sent to you by other users to start talking , dating and chating .
//

    fun DeclineRequest(OtherUserId: String , MyUserId : String, NotViewPeople : ArrayList<String> , ChatTalkingToRequests : ArrayList<String> , OtherUsersIdRequests : ArrayList<String>) {

        val MyUserField = mutableMapOf<String, Any>() // Used for both the cases if and else



        // Remove person from being viewed anymore
        val otherUser: DocumentReference = firestoreInstance.document("users/${OtherUserId}")
        otherUser.get()
        val UserField = mutableMapOf<String, Any>()
        NotViewPeople.add(MyUserId)
        UserField[References.NotToBeViwedAnywhere.toString()] = NotViewPeople


        otherUser.update(UserField)

        // Remove Requests Once Sent




        // #2 Update My User too
        val MyChat = ChatTalkingToRequests

        MyUserField[References.NotToBeViwedAnywhere.toString()] = MyUserField
        MyChat.add(OtherUserId)


        // Removes Person from interests
        val RequestReceived = OtherUsersIdRequests
        RequestReceived.add(OtherUserId)
        MyUserField[References.requests.toString()] = RequestReceived
        currentUserDocRef.update(MyUserField)










    }


    fun Block(OtherUserId: String, MyUserId : String, NotViewPeopleArray: ArrayList<String>, MyDeclineBlocked : ArrayList<String>, NumberOfPeopleDating : ArrayList<String> , NoOfPeopleOtherUserDating : ArrayList<String>) {

        val MyUserField = mutableMapOf<String, Any>() // Used for both the cases if and else


        val otherUser: DocumentReference = firestoreInstance.document("users/${OtherUserId}")

        otherUser.get()
        val UserField = mutableMapOf<String, Any>()
        val OtherUserChat = NotViewPeopleArray
        OtherUserChat.add(MyUserId)
        NoOfPeopleOtherUserDating.remove(MyUserId)
        UserField["numberOfPeopleDating"] = NoOfPeopleOtherUserDating
        UserField[References.NotToBeViwedAnywhere.toString()] = UserField



        otherUser.update(UserField)

        // Remove Requests Once Sent




        // #2 Add to blocked users
        val DeclineArray = MyDeclineBlocked
        MyUserField[References.declineOrBlocked.toString()] = MyUserField
        DeclineArray.add(OtherUserId)


        // Remove from chat activity
        val Num = NumberOfPeopleDating
        Num.remove(OtherUserId)
        MyUserField["numberOfPeopleDating"] = Num


        currentUserDocRef.update(MyUserField)













    }



    // Send Image to the other user in message
    fun sendMessage(message: Mesage, channelId: String) {
        chatChannelCollection.document(channelId)
            .collection("messages")
            .add(message)
    }



    fun UserInfoUpload(Items : MutableMap<String , Any>) {
        currentUserDocRef.update(Items)
    }

    fun addRequestListener(RequestedUsers :  ArrayList<String> ,context: Context, onListen: (List<Item>) -> Unit): ListenerRegistration {





            return firestoreInstance.collection("users")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        Log.e("FIRESTORE", "Users listener error.", firebaseFirestoreException)
                        return@addSnapshotListener
                    }

                    val items = mutableListOf<com.xwray.groupie.kotlinandroidextensions.Item>()
                    querySnapshot!!.documents.forEach {


                        if (it.id != FirebaseAuth.getInstance().currentUser?.displayName && RequestedUsers.contains(it.id)) {

                            items.add(RequestClass(it.toObject(User::class.java)!!, it.id, context))


                        }


                    }
                    onListen(items)
                }

    }



    fun GetDateChats(UsersNeeded : ArrayList<String>  , context: Context, onListen: (List<Item>) -> Unit): ListenerRegistration {

        return firestoreInstance.collection("users")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "Users listener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = mutableListOf<com.xwray.groupie.kotlinandroidextensions.Item>()
                querySnapshot!!.documents.forEach {


                    if (it.id != FirebaseAuth.getInstance().currentUser?.displayName && UsersNeeded.contains(it.id)) {

                        items.add(PersonClass(it.toObject(User::class.java)!!, it.id, context))


                    }
                    FireabaseClass.myRef.child(References.ExtraDelete3.toString()).setValue(items)

                }
                onListen(items)
            }

    }

    fun addMatchMakers(context: Context, UsersNeeded : ArrayList<String>, onListen: (List<com.xwray.groupie.kotlinandroidextensions.Item>) -> Unit): ListenerRegistration {
        return firestoreInstance.collection("users")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "Users listener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = mutableListOf<com.xwray.groupie.kotlinandroidextensions.Item>()
                querySnapshot!!.documents.forEach {
                   if (it.id != FirebaseAuth.getInstance().currentUser?.displayName && UsersNeeded.contains(it.id)) {


                        items.add(MatchMakinProfile(it.toObject(User::class.java)!!, it.id, context))


                  }


                }

                onListen(items)
            }
    }

    fun removeListener(registration: ListenerRegistration) = registration.remove()

    // send Messages
    fun sendMessage(message: TextMessage, channelId: String) {
        chatChannelCollection.document(channelId)
            .collection("messages")
            .add(message)
    }




    // FCM Tokens registered here
    fun getFCMRegistrationTokens(onComplete: (tokens: MutableList<String>) -> Unit) {
        currentUserDocRef.get().addOnSuccessListener {
            val user = it.toObject(User::class.java)!!
            onComplete(user.registrationTokens)
        }
    }

    fun setFCMRegistrationTokens(registrationTokens: MutableList<String>) {
        currentUserDocRef.update(mapOf("registrationTokens" to registrationTokens))
    }

    fun AddToDatingSystem(name : String) { // How many he is talking to
        val userFieldMap = mutableMapOf<String, Any>()

        var DatingAlready = ArrayList<String>()
        getCurrentUser { user ->

            DatingAlready = user.numberOfPeopleDating

        }
        DatingAlready.add(name)
        userFieldMap["dating"] = name
        currentUserDocRef.update(userFieldMap)
    }


}
