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
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.R.attr.data
import android.content.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.widget.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.URI
import java.util.jar.Manifest


class MainActivity : AppCompatActivity()   {

    public var elementsImg = mutableListOf<Uri>()

    private lateinit var pickedImage: ImageAdapter

    public lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        setContentView(R.layout.progressbar)

        getDataFromUrl("https://api.myjson.com/bins/grhsp")
        //setData()
       // setContentView(R.layout.activity_main)
        //setLoader()


    }


    private fun setData() {
        val resultPermision = checkPersmission(android.Manifest.permission.INTERNET)
        if (!resultPermision) {
            requestPermission(android.Manifest.permission.INTERNET, 1003)
        } else {
            getDataFromUrl("https://api.myjson.com/bins/grhsp")
        }

        val items = listOf(
                MainItem("Соответсвие фото"),
                MainItem("Недостатки"),
                MainItem("Расположение"),
                MainItem("Чистота"),
                MainItem("Обслуживание"),
                MainItem("Удобство номера")
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
    private fun setLoader() {
        val progressBar = ProgressBar(this)

       /* progressBar.setIndeterminate(false)
        progressBar.setIndeterminateDrawable(ContextCompat.getDrawable(this, R.drawable.spinner))

        progressBar.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        constraintMain.addView(progressBar)*/

        //constraintMain.setVisibility(View.GONE)
        llProgressBar.setVisibility(View.VISIBLE)
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
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            0 -> {
                //val imageBitmap = data!!.extras.get("data") as Bitmap
                //saveImageFromCamera(imageBitmap)
                //elementsImg.add(elementsImg.lastIndex + 1, uri)
                var imgAdapter = ImageAdapter(elementsImg,this)
                println(elementsImg)
                recycleImg.adapter = imgAdapter
            }
            1 -> {
                val pickedImage = data?.getData()
                val filePath = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = contentResolver.query(pickedImage, filePath, null, null, null)
                cursor!!.moveToFirst()
                val imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]))
                elementsImg.add(elementsImg.lastIndex + 1, pickedImage!!)
                var imgAdapter = ImageAdapter(elementsImg,this)
                recycleImg.adapter = imgAdapter
                cursor.close()
            }
        }


    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_",
                ".jpg",
                storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    /*private fun saveImageFromCamera(bitmap: Bitmap): Any {

        *//*val wrapper = ContextWrapper(this)
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")
        val stream: OutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.flush()
        stream.close()*//*
        //return Uri.parse(file.absolutePath)
    }*/

    private fun takePhoto() {
        val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (intentCamera.resolveActivity(packageManager) != null) {
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {null}
            // Continue only if the File was successfully created
            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.criticalgnome.recyclerviewwithkotlin.provider",
                        it
                )
                intentCamera.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoURI)
                elementsImg.add(elementsImg.lastIndex + 1, photoURI)
            }
            startActivityForResult(intentCamera, REQUEST_TAKE_PHOTO)
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
            1003 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getDataFromUrl("https://api.myjson.com/bins/grhsp")
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

    private fun getDataFromUrl(string: String) {
        var client: OkHttpClient = OkHttpClient()
        println(client)
        val request = Request.Builder()
                .url("https://api.myjson.com/bins/grhsp")
                .build()
        println(request)
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println(e.message)
            }
            override fun onResponse(call: Call, response: Response) {
                /*println("UUUSUKA")
                runOnUiThread {
                    //setContentView(R.layout.activity_main)
                    //
                }*/
                setData()
                println(response.body()?.string())

            }
        })
        println(client)
    }
}
