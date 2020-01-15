package com.example.tipster

import android.transition.Visibility
import android.view.View
import android.webkit.DateSorter
import com.airbnb.lottie.LottieAnimationView
import com.example.tipster.Util.FireStoreUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.Item
import kotlin.reflect.KFunction

var DstingClass = DatingSystem()

class DatingSystem() {

    var NumberOfPeople = 0 // Number of people currently meeting
    var UsersDating = arrayListOf<String>() // Usernames dating

    private val Gender = FireabaseClass.Gender// Lets say its Male
    private var OppositeGender = "Females"



    var LoversCollected = arrayListOf<String>()
    val SameGendersCollected = arrayListOf<String>()

    // Final list collected from same gender
    var SameGCollected = ArrayList<String>()



    private fun CheckOppositeGender() {
        if (Gender == References.Males.toString()) {
            OppositeGender = References.Females.toString()
        } else {
            OppositeGender = References.Males.toString()

        }
    }


    // When I click Matchmake the result will be from the function below



    // Assign the number of people wanted


    fun GetNumberOfPeopleMatched(DateWanted : Int, Lottie : LottieAnimationView): ArrayList<String> {
        CheckOppositeGender()
        // Reload
        var MatchMakeSameGenderBool = true

        val Db = FireabaseClass.myRef.child(References.Likes.toString())

        Db.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

            var NumberOfPeopleDating = ArrayList<String>()


                FireStoreUtil.getCurrentUser { user ->
                    NumberOfPeopleDating = user.numberOfPeopleDating
                    val BlockedUsers = user.declineOrBlocked
                    val NotToBeSeen = user.NotToBeViwedAnywhere

                    val InterestRecieved = user.Interest

                    for (n in Interests.Category.indices) {

                        val MatchMakes = p0.child(Interests.Category[n]).child(InterestRecieved[n])
                            .child(OppositeGender).children


                        for (Lovers in MatchMakes) {

                            if (!NumberOfPeopleDating.contains(Lovers.key.toString()) && !BlockedUsers.contains(Lovers.key.toString()) && !NotToBeSeen.contains(Lovers.key.toString()) && !user.requests.contains(Lovers.key.toString())) {


                                LoversCollected.add(0, Lovers.key.toString())

                            }


                        }

//                        val MatchMakeSameGender = p0.child(Interests.Category[n]).child(Interests.CategoryGatheredTypes[n])
//                            .child(Gender).children


                        // Same Gender Collection

                        if (user.MatchMakeSameGender) {


                            val MatchMakeSameGender = p0.child(Interests.Category[n]).child(InterestRecieved[n])
                                .child(Gender).children
                            for (SameGender in MatchMakeSameGender) {


                                if (!NumberOfPeopleDating.contains(SameGender.key.toString()) && !BlockedUsers.contains(
                                        SameGender.key.toString()
                                    )
                                ) {


                                    SameGendersCollected.add(0, SameGender.key.toString())

                                }


                            }
                        } else {
                            MatchMakeSameGenderBool = false
                        }


                    }

                    // Gets the best Matches

                }
                Lottie.pauseAnimation()
                Lottie.visibility = View.GONE

            }



            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


        })

        return  GetMultiples(3 , MatchMakeSameGenderBool)



    } // Lovers Wanted

    // Algorithm
    private fun GetMultiples(PeopleWanted : Int  , SameGenderMatchMake : Boolean) : ArrayList<String> {

        val Dict = mutableMapOf<String, Int>()





        for (n in LoversCollected.indices) {

            if (Dict.contains(LoversCollected[n])) {
                Dict[LoversCollected[n]] = 1 + Dict[LoversCollected[n]]!!

            } else {

                Dict[LoversCollected[n]] = 1
            }


        }
        //{Shela=1, Stella=3, Kaira=1, Jessie=4}
        //

        val People = arrayListOf<String>()
        val Rank = arrayListOf<Int>() // [2,8,4,1,4]

        for (n in Dict) {
            People.add(n.component1())
            Rank.add(n.component2())
        }



        for (n in Rank.indices) {



            for (S in Rank.indices)

                if (Rank[n] >= Rank[S]) {
                    val Num = Rank[n]
                    Rank[n] = Rank[S]
                    Rank[S] = Num

                    val Same = People[n]
                    People[n] = People[S]
                    People[S] = Same
                }
        }

        val DateGoingFor = arrayListOf<String>() // Final Usernames going in
        if (People.size > PeopleWanted) {
            for (n in 0..PeopleWanted - 1) {
                DateGoingFor.add(People[n])

            }
        } else {
            for (n in People) {
                DateGoingFor.add(n)

            }
        }

        if (SameGenderMatchMake) {
            SameGCollected = MatchMakingCollected(2)
        }


        return DateGoingFor

    }


    private fun MatchMakingCollected(PeopleWanted: Int) : ArrayList<String> {
        val Dict = mutableMapOf<String, Int>()

        for (n in SameGendersCollected.indices) {

            if (Dict.contains(SameGendersCollected[n])) {
                Dict[SameGendersCollected[n]] = 1 + Dict[SameGendersCollected[n]]!!

            } else {

                Dict[SameGendersCollected[n]] = 1
            }


        }
        //{Shela=1, Stella=3, Kaira=1, Jessie=4}
        //

        val People = arrayListOf<String>()
        val Rank = arrayListOf<Int>() // [2,8,4,1,4]

        for (n in Dict) {
            People.add(n.component1())
            Rank.add(n.component2())
        }



        for (n in Rank.indices) {



            for (S in Rank.indices)

                if (Rank[n] >= Rank[S]) {
                    val Num = Rank[n]
                    Rank[n] = Rank[S]
                    Rank[S] = Num

                    val Same = People[n]
                    People[n] = People[S]
                    People[S] = Same
                }
        }

        val DateGoingFor = arrayListOf<String>() // Final Usernames going in
        if (People.size > PeopleWanted) {
            for (n in 0..PeopleWanted - 1) {
                DateGoingFor.add(People[n])

            }
        } else {
            for (n in People) {
                DateGoingFor.add(n)

            }
        }
        FireabaseClass.myRef.child(References.ExtraDelete.toString()).setValue(DateGoingFor)



        return DateGoingFor



    }






}


