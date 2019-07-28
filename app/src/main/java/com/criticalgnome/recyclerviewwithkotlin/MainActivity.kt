package com.criticalgnome.recyclerviewwithkotlin

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.R.attr.data
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import java.util.jar.Manifest


class MainActivity : AppCompatActivity()  {

    public var elementsImg = arrayOf<Bitmap>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val items = listOf(
                MainItem("Соответсвие фото"),
                MainItem("Недостатки"),
                MainItem("Расположение"),
                MainItem("Чистота"),
                MainItem("Обслуживание"),
                MainItem("Удобство номера"),
                MainItem("Цена - качество или в две строчки")
        )
        val avgRating = findViewById<TextView>(R.id.avgRating)
        val buttonAddPhoto = findViewById<TextView>(R.id.addPhotoBtn)

        buttonAddPhoto.setOnClickListener {
            showDialog()
        }
        val myAdapter = MainAdapter(items, this)

        //val imgAdapter = ImageAdapter(elementsImg,this)

        myRecycler.adapter = myAdapter
    }

    private fun showDialog() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Выберите действие")

        val cases = arrayOf("Камера", "Галлерея")
        builder.setItems(cases) { dialog, which ->
            when (which) {
                0 -> {
                    takePhoto()
                }
                1 -> {
                    checkPersmission()
                    requestPermission()
                    pickImageFromGallery()
                }
            }
        }

// create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    companion object {
        private val REQUEST_TAKE_PHOTO = 0
        private val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM) // GIVE AN INTEGER VALUE FOR IMAGE_PICK_CODE LIKE 1000
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val pickedImage = data?.getData()
        val filePath = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(pickedImage, filePath, null, null, null)
        cursor!!.moveToFirst()
        val imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]))
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        val bitmap = BitmapFactory.decodeFile(imagePath, options)
        var images = arrayListOf<String>()
        images.set(0, imagePath)
        println(images)

        imageLink.setImageBitmap(bitmap)
        // At the end remember to close the cursor or you will end with the RuntimeException!
        cursor.close()
       // println(pickedImage)
    }


    private fun takePhoto() {
        val intent1 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent1.resolveActivity(packageManager) != null) {
            startActivityForResult(intent1, REQUEST_TAKE_PHOTO)
        }
    }

    private fun checkPersmission(): Boolean {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    1000)
            return true
        }
        return false
    }

    /**
     * Если необходимо для callback
     */
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1000 -> {
                // If request is cancelled, the result arrays are empty.
                println("ZBS!!!!")
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    println("YEssy")
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    println("NONONON")
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                println(222)
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                println(111)
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        1000)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            println(444)
            // Permission has already been granted
        }
    }

    private fun takePicture() {

        val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        /*val file: File = createFile()

        val uri: Uri = FileProvider.getUriForFile(
                this,
                "com.example.android.fileprovider",
                file
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)*/
        startActivityForResult(intent, 101)
    }

}
