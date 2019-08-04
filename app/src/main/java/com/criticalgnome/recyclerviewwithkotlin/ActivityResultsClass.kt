package com.criticalgnome.recyclerviewwithkotlin

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import kotlinx.android.synthetic.main.activity_main.*

class ActivityResultsClass {

    fun activityResults(requestCode: Int, resultCode: Int, data: Intent?, mainClass: MainActivity){
        when(requestCode) {
            0 -> {
                println(resultCode)
                if (resultCode == Activity.RESULT_OK) {
                    mainClass.elementsImg.add(mainClass.photoURIPublic)
                    var datas = data?.getExtras()
                    println(datas)
                    var imgAdapter = ImageAdapter(mainClass.elementsImg,mainClass)
                    println(mainClass.elementsImg)
                    mainClass.recycleImg.adapter = imgAdapter
                }
            }
            1 -> {
                println(resultCode)
                if (resultCode == Activity.RESULT_OK) {
                    val pickedImage = data?.getData()
                    val filePath = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = mainClass.contentResolver.query(pickedImage, filePath, null, null, null)
                    cursor!!.moveToFirst()
                    val imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]))
                    mainClass.elementsImg.add(pickedImage!!.toString())
                    var imgAdapter = ImageAdapter(mainClass.elementsImg,mainClass)
                    mainClass.recycleImg.adapter = imgAdapter
                    cursor.close()
                }
            }
        }
    }
}