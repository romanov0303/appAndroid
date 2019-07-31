package com.criticalgnome.recyclerviewwithkotlin

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import okhttp3.*
import java.io.IOException


class LoadActivity : AppCompatActivity() {

    lateinit var context: LoadActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        val resultPermision = checkPersmission(android.Manifest.permission.INTERNET)
        if (!resultPermision) {
            requestPermission(android.Manifest.permission.INTERNET, 1003)
        } else {
            getDataFromUrl("https://api.myjson.com/bins/grhsp")
        }
        //

       //
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
            1003 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getDataFromUrl("https://api.myjson.com/bins/grhsp")
                } else {
                    println("888888")
                }
            }

            else -> {

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
                runOnUiThread {
                    println(response.body()?.string())
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }
        })
    }
}