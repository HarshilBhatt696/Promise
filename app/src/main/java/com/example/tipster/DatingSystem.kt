package com.example.tipster

import android.webkit.DateSorter
import com.example.tipster.Util.FireStoreUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

var DstingClass = DatingSystem()

class DatingSystem() {

    var NumberOfPeople = 0 // Number of people currently meeting
    var UsersDating = arrayListOf<String>() // Usernames dating

    private val Gender = FireabaseClass.Gender// Lets say its Male
    private var OppositeGender = "Females"



    var LoversCollected = arrayListOf<String>()



    private fun CheckOppositeGender() {
        if (Gender == References.Males.toString()) {
            OppositeGender = References.Females.toString()
        } else {
            OppositeGender = References.Males.toString()
        }
    }


    // When I click Matchmake the result will be from the function below



    // Assign the number of people wanted


    fun GetNumberOfPeopleMatched(DateWanted : Int ): ArrayList<String> {
        CheckOppositeGender()
        // Reload


        val Db = FireabaseClass.myRef.child(References.Likes.toString())

        Db.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

            var NumberOfPeopleDating = ArrayList<String>()


                FireStoreUtil.getCurrentUser { user ->
                    NumberOfPeopleDating = user.numberOfPeopleDating
                    val BlockedUsers = user.declineOrBlocked



                    for (n in Interests.Category.indices) {
                        val MatchMakes = p0.child(Interests.Category[n]).child(Interests.CategoryGatheredTypes[n])
                            .child(OppositeGender).children


                        for (Lovers in MatchMakes) {

                            if (!NumberOfPeopleDating.contains(Lovers.key.toString()) && !BlockedUsers.contains(Lovers.key.toString())) {


                                LoversCollected.add(0, Lovers.key.toString())

                            }


                        }

                    }

                    // Gets the best Matches

                }

            }



            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


        })

        return  GetMultiples(3)



    } // Lovers Wanted

    // Algorithm
    private fun GetMultiples(PeopleWanted : Int) : ArrayList<String> {

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

        return DateGoingFor

    }






}


