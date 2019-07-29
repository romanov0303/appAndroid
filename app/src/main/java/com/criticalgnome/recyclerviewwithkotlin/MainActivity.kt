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
                    var res = checkPersmission(android.Manifest.permission.CAMERA)
                    if (res) {
                        takePhoto()
                    } else {
                        requestPermission(android.Manifest.permission.CAMERA, 1000)
                    }

                }
                1 -> {
                    var res = checkPersmission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    if (res) {
                        pickImageFromGallery()
                    } else {
                        requestPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, 1001)
                    }

                }
            }
        }

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
        when(requestCode) {
            0 -> {
                var image = data?.getExtras()?.get("data")
                println(image)
            }
            1 -> {
                println(requestCode)
                val pickedImage = data?.getData()
                val filePath = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = contentResolver.query(pickedImage, filePath, null, null, null)
                cursor!!.moveToFirst()
                val imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]))
                val options = BitmapFactory.Options()
                options.inPreferredConfig = Bitmap.Config.ARGB_8888
                val bitmap = BitmapFactory.decodeFile(imagePath, options)
                // var images = arrayListOf<String>()
                /*images.set(0, imagePath)
                println(images)*/

                imageLink.setImageBitmap(bitmap)
                // At the end remember to close the cursor or you will end with the RuntimeException!
                cursor.close()
                // println(pickedImage)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)

    }


    private fun takePhoto() {
        val intent1 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent1.resolveActivity(packageManager) != null) {
            startActivityForResult(intent1, REQUEST_TAKE_PHOTO)
        }
    }

    private fun checkPersmission(permission: String): Boolean {
        return (ContextCompat.checkSelfPermission(this, permission) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                permission) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission(permission: String, code: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), code)
    }

    /**
     * Если необходимо для callback
     */
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1000 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    takePhoto()
                } else {
                    Toast.makeText(this, "Denied!", Toast.LENGTH_SHORT).show()

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
            1001 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    pickImageFromGallery()
                } else {
                    Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show()
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


}
