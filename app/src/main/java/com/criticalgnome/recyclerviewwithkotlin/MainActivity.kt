package com.criticalgnome.recyclerviewwithkotlin

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
import android.content.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.widget.Adapter
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.URI
import java.util.jar.Manifest


class MainActivity : AppCompatActivity()  {

    public var elementsImg = mutableListOf<Uri>()

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

        val imgAdapter = ImageAdapter(elementsImg,this)

        myRecycler.adapter = myAdapter

        recycleImg.adapter = imgAdapter
    }

    private fun showDialog() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Выберите действие")

        val cases = arrayOf("Камера", "Галлерея", "Чтение права")
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
                2 -> {
                    var res = checkPersmission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    println(res)
                    requestPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, 1002)

                   // println(res)
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
                val imageBitmap = data!!.extras.get("data") as Bitmap
                val uri = saveImageFromCamera(imageBitmap)
                elementsImg.add(elementsImg.lastIndex + 1, uri)

                val imgAdapter = ImageAdapter(elementsImg,this)

                recycleImg.adapter = imgAdapter

               // imageLink.setImageURI(uri)
            }
            1 -> {
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

               // imageLink.setImageBitmap(bitmap)
                cursor.close()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

    private fun saveImageFromCamera(bitmap: Bitmap): Uri {
        val wrapper = ContextWrapper(this)
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")
        val stream: OutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.flush()
        stream.close()
        return Uri.parse(file.absolutePath)
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
            1002 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    println("111111")
                } else {
                    println("888888")
                }
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }


}
