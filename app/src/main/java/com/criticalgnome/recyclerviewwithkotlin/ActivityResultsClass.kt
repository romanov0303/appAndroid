package com.criticalgnome.recyclerviewwithkotlin

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class ActivityResultsClass {

    fun activityResults(requestCode: Int, resultCode: Int, data: Intent?, mainClass: MainActivity){
        when(requestCode) {
            0 -> {
                println(resultCode)
                if (resultCode == Activity.RESULT_OK) {
                    mainClass.elementsImg.add(mainClass.photoURIPublic)
                    var datas = data?.getExtras()
                    var imgAdapter = ImageAdapter(mainClass.elementsImg,mainClass)
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