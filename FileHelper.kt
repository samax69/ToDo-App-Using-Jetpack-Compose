@file:Suppress("UNCHECKED_CAST")

package com.vaibhav.todoapp

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.io.FileNotFoundException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

const val FILE_NAME = "todoList.dt"

fun WriteData(items : SnapshotStateList<String>, context: Context){
    val fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
    var oas = ObjectOutputStream(fos)

    val itemList = ArrayList<String>()
    itemList.addAll(items)
    oas.writeObject(itemList)
    oas.close()
}

fun ReadData(context: Context) : SnapshotStateList<String>{
    var itemList : ArrayList<String>
    val items = mutableStateListOf<String>()
    try {
        val fis = context.openFileInput(FILE_NAME)
        val ois = ObjectInputStream(fis)
        itemList = ois.readObject() as ArrayList<String>
        items.addAll(itemList)

    }catch (e : FileNotFoundException){
        itemList = ArrayList<String>()
    }

    return items

}