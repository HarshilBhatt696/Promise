package com.example.tipster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import com.example.tipster.SetImage
import com.example.tipster.Util.FireStoreUtil

import kotlinx.android.synthetic.main.interest.*
import kotlinx.android.synthetic.main.set_image.*

class Interest : AppCompatActivity() {

   lateinit var Radios : ArrayList<RadioButton>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.interest)
         Radios = arrayListOf<RadioButton>(Interest1 , Interest2 , Interest3)


        if (RadioGroup.checkedRadioButtonId != -1) {

            if (Interest1.isChecked) {}



        }

        var CategoryNumber = 0 // Current Category On
        // Next Clicks
        NextInterest.setOnClickListener {
            if (CheckAllRadioClicked() == false ) {} // IF and only if the page is completed // This is to check all the current radio grou atleast one is marked

            else {

                CategoryNameText.text = Interests.Category[CategoryNumber] // assign name to category type

                var CategoryTypeNumber = 0 // To get the category Types
                for (n in Radios) { // if it crashes it means you have less options
                    n.text =
                        Interests.AllCategoryTypes[CategoryNumber][CategoryTypeNumber] // Assigns values to radio buttons

                    if (n.isChecked) {
                        Interests.CategoryGatheredTypes[CategoryNumber] =
                            Interests.AllCategoryTypes[CategoryNumber][CategoryTypeNumber] // Assigns User interest values
                    }
                    CategoryTypeNumber += 1

                }

                if (Interests.Category.count() - 1 > CategoryNumber) {
                    CategoryNumber += 1
                } else {
                    // Upload Interests online
                    FireabaseClass.AssignInterest()
                    // Upload to Firestore user
                    FireStoreUtil.UpdateInterest(Interests.CategoryGatheredTypes)


                    val intent = Intent(this,  com.example.tipster.SetImage::class.java)

                    startActivity(intent)


                }


            }

        }

        // If went back to the user
        PreviousInterest.setOnClickListener {

            if (CategoryNumber != 0 ) {
                CategoryNumber -= 1


                CategoryNameText.text = Interests.Category[CategoryNumber] // assign name to category type

                var CategoryTypeNumber = 0 // To get the category Types
                for (n in Radios) { // if it crashes it means you have less options
                    n.text =
                        Interests.AllCategoryTypes[CategoryNumber][CategoryTypeNumber] // Assigns values to radio buttons


                    if (n.isChecked) {
                        Interests.CategoryGatheredTypes[CategoryNumber] =
                            Interests.AllCategoryTypes[CategoryNumber][CategoryTypeNumber] // Assigns User interest values
                    }
                    CategoryTypeNumber += 1

                }

                // Replace items for Previous
                for (n in Radios) {

                    if (n.text.toString() == Interests.CategoryGatheredTypes[CategoryNumber]) {
                            n.isChecked = true

                    }


                }


            }


        }





    }



    fun CheckAllRadioClicked() : Boolean {

        var OneClickedMin = false // True if any one check box is clicked
        for (n in Radios) {
            if (n.isChecked) {
                OneClickedMin = true
            }
        }
        return OneClickedMin

    }


}


// Interest Class
val Interests = InterestList()

class InterestList() {

    var Category = arrayListOf<String>("Type" , "Stream" , "I like " , "Sports") // If you add here , Add Gathered too || Follow the same order for category Gathered types

    private val Type = arrayListOf<String>("Funny" , "Serious" , "Treywey")

    private val Stream = arrayListOf<String>("Commerce" , "Science"  , "Arts")
    private val Like = arrayListOf<String>("Me" , "You" , "Both")
    private val Sports = arrayListOf<String>("Football" , "Basketball"  , "Cricket")

    // Only All Types

    val AllCategoryTypes= arrayListOf<ArrayList<String>>(Type , Stream , Like , Sports)

        // Interest Gathered

    var CategoryGatheredTypes = arrayListOf<String>("Funny" , "Commerce" , "Both" , "Football") // Should be equal to Category // Should match the order


}
