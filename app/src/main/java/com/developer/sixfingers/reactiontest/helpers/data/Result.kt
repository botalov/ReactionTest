package com.developer.sixfingers.reactiontest.helpers.data

import com.google.firebase.database.*


data class Result (
    val name:String = "",
    val res_val: Long = 0
)

fun addResultToDataBase(firebaseData: FirebaseDatabase, res: Result, callerror: (String)->Unit) {

    val reference = firebaseData.getReference("results")

    reference.addListenerForSingleValueEvent(object : ValueEventListener{
        override fun onDataChange(p0: DataSnapshot?) {
            reference.push().setValue(res)
        }

        override fun onCancelled(p0: DatabaseError?) {
            callerror(p0.toString())
        }

    })
}

fun getResultsFromDataBase(firebaseData: FirebaseDatabase, callerror: (String)->Unit) : MutableList<Result> {
    val reference = firebaseData.getReference("results")
    val res : MutableList<Result> = mutableListOf()

    reference.addValueEventListener(object : ValueEventListener{
        override fun onDataChange(p0: DataSnapshot?) {


            p0!!.children.mapNotNullTo(res) { it.getValue<Result>(Result::class.java) }
        }

        override fun onCancelled(p0: DatabaseError?) {
            callerror(p0.toString())
        }
    })

    return res
}