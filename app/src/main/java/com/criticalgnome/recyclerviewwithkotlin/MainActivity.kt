package com.criticalgnome.recyclerviewwithkotlin

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity()   {

    var elementsImg = mutableListOf<String>()

    private lateinit var pickedImage: ImageAdapter

    var listData = mutableListOf<MainItem>()

    lateinit var photoURIPublic: String

    lateinit var currentPhotoPath: String

    lateinit var mainAdapter: MainAdapter

    lateinit var imageAdapter: ImageAdapter

    var listProperties = mutableListOf<MainItem>()

    var elementsImgParcelable = mutableListOf<ListUri>()

    var listPropertiesParcelable = mutableListOf<ParcelableString>()

    var myDialog: MyFragment? = null

    var showDialog: Int? = 1

    companion object {
        //var BaseUrl = "https://api.myjson.com/bins/grhsp/"
        //var BaseUrl = "https://api.myjson.com/bins/b2w4t/"
        //var BaseUrl = "https://api.myjson.com/bins/tqc79/"
        var BaseUrl = "http://10.0.2.2:3000/answer/"
        private val REQUEST_TAKE_PHOTO = 0
        private val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
        private val CAMERA_PERMISSION = 1000
        private val READ_PERMISSION = 1001
        private val INTERNER_PERMISSION = 1003
    }

    fun retrofitGetDataFromUrl() {
        layoutProgress.visibility = View.VISIBLE
        mainView.visibility = View.GONE
        val networkStateStable = isNetworkAvailable()
        if (!networkStateStable) {
            myDialog = MyFragment()
            myDialog!!.show(supportFragmentManager, "fragment_dialog")
        } else {
            val okHttpClient = OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .readTimeout(5000,TimeUnit.MILLISECONDS)
                    .build()

            val retrofit = Retrofit.Builder()
                    .baseUrl(BaseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val listCollector = mutableListOf<Collector>()

            val service = retrofit.create(SutochnoService::class.java)
            val call = service.getData()
            call.enqueue(object : Callback<SutochnoResponse> {
                override fun onResponse(call: Call<SutochnoResponse>, response: Response<SutochnoResponse>) {
                    if (response.code() == 200) {
                        val data = response.body()!!
                        var superNewData = mutableListOf<Collector>()
                        for ((index, value) in data.data!!.review!!.properties!!.withIndex()) {
                            var dataNewList = mutableListOf<Collector>()
                            var groupName = value.groupName
                            //var properties = mutableListOf<Ratings>()
                            var properties = mutableMapOf<String, String>()
                            for ((i,v) in value.items!!.withIndex()) {
                                properties.put(v.title, v.value)
                                //properties.add(Ratings(v.title, v.value))
                            }
                            superNewData.add(Collector(groupName, properties))
                        }
                        layoutProgress.visibility = View.GONE

                        fillDataRecycler(superNewData)
                    }
                }
                override fun onFailure(call: Call<SutochnoResponse>, t: Throwable) {
                    myDialog = MyFragment()
                    myDialog!!.show(supportFragmentManager, "fragment_dialog")
                }
            })
        }

    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

    override fun onSaveInstanceState(outState: Bundle?) {

        outState?.putParcelable("properties", ParcelableClass(listPropertiesParcelable))
        outState?.putParcelable("images", ImagesParcelableNew(elementsImg))
        outState?.putString("plus", et.text.toString())
        outState?.putString("minus", minus.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        var fragment = getSupportFragmentManager().findFragmentByTag("fragment_dialog")
        if (fragment != null) {
            mainView.visibility = View.GONE
            layoutProgress.visibility = View.VISIBLE
        } else {
            val parcelableProperties: ParcelableClass = savedInstanceState!!.getParcelable("properties")
            val parcelableImages: ImagesParcelableNew = savedInstanceState!!.getParcelable("images")
            val images = parcelableImages.getListParams()
            val properties = parcelableProperties.getListProperties()

            val plusVal: String = savedInstanceState!!.getString("plus")
            val minusVal: String = savedInstanceState!!.getString("minus")

            et.setText(plusVal)
            minus.setText(minusVal)
            println("IMAGEEE $images")
            for(i in images!!.iterator()) {
                elementsImg.add(i)
            }

            var propertiesList = mutableMapOf<String, String>()

            for(i in properties!!.iterator()) {
                propertiesList.put(i.getGroup(),i.getRatingVal())
            }

            var collectorDataContainer = mutableListOf<Collector>()

            collectorDataContainer.add(Collector("review_ratings_order", propertiesList))
            fillDataRecycler(collectorDataContainer)
            super.onRestoreInstanceState(savedInstanceState)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            setContentView(R.layout.activity_main)
            layoutProgress.visibility = View.GONE
            mainView.visibility = View.VISIBLE
            var param1 = savedInstanceState?.get("TestInstance")
            println("fucker222 $param1")
            println("fucker333")
        } else {
            setContentView(R.layout.activity_main)
            val resultPermision = checkPersmission(android.Manifest.permission.INTERNET)
            if (!resultPermision) {
                requestPermission(android.Manifest.permission.INTERNET, 1003)
            } else {
                retrofitGetDataFromUrl()
            }
        }

    }

    private fun getDataFromUrl(string: String) {

    }

    /*
    Заполнить шаблоны данными
     */
    private fun fillDataRecycler(list: MutableList<Collector>) {

        for (i in list) {
            if (i.groupName == "review_ratings_order") {
                for (property in i.items) {
                    listProperties.add(MainItem(property.key, property.value))
                }
            }
        }

        val buttonAddPhoto = findViewById<TextView>(R.id.addPhotoBtn)

        buttonAddPhoto.setOnClickListener {
            showDialog()
        }

        mainAdapter = MainAdapter(listProperties, this)

        imageAdapter = ImageAdapter(elementsImg,this)

        myRecycler.adapter = mainAdapter

        recycleImg.adapter = imageAdapter

        mainView.visibility = View.VISIBLE
    }

    /*
    Показать диалог с выбором действия
     */
    private fun showDialog() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Выберите действие")

        val cases = arrayOf("Камера", "Галлерея"/*, "Чтение права"*/)
        builder.setItems(cases) { dialog, which ->
            when (which) {
                0 -> {
                    var res = checkPersmission(android.Manifest.permission.CAMERA)
                    if (res) {
                        takePhoto()
                    } else {
                        requestPermission(android.Manifest.permission.CAMERA, CAMERA_PERMISSION)
                    }

                }
                1 -> {
                    var res = checkPersmission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    if (res) {
                        pickImageFromGallery()
                    } else {
                        requestPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, READ_PERMISSION)
                    }
                }
                2 -> {
                    var res = checkPersmission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    println(res)
                    requestPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, 1002)
                }
            }
        }

        val dialog = builder.create()
        dialog.show()
    }

    /*
    Взять изображение из галереи
     */
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM) // GIVE AN INTEGER VALUE FOR IMAGE_PICK_CODE LIKE 1000
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var activityRequestClass = ActivityResultsClass()
        activityRequestClass.activityResults(requestCode, resultCode, data, this)
    }

    /*
    Создать контейнер для фотографии
     */

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

    /*
    Сфотографировать
     */
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
                photoURIPublic = photoURI.toString()
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
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    takePhoto()
                } else {
                    Toast.makeText(this, "Denied!", Toast.LENGTH_SHORT).show()
                }
                return
            }
            1001 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    pickImageFromGallery()
                } else {
                    Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show()
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
                    getDataFromUrl(BaseUrl)
                } else {
                    println("888888")
                }
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }
}
