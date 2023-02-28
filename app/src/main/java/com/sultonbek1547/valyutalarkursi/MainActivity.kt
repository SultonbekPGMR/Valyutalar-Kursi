package com.sultonbek1547.valyutalarkursi

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sultonbek1547.valyutalarkursi.adapter.RvAdapter
import com.sultonbek1547.valyutalarkursi.adapter.RvClick
import com.sultonbek1547.valyutalarkursi.databinding.ActivityMainBinding
import com.sultonbek1547.valyutalarkursi.databinding.DialogLayoutBinding
import com.sultonbek1547.valyutalarkursi.model.MyCurrencyItem
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity(), RvClick {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var rvAdapter: RvAdapter
    private lateinit var list: List<MyCurrencyItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (isInternetAvailable()) {
            binding.tvNet.text = "Internet is available"
        }

        myTask.execute()

    }

    private fun getCurrency(): List<MyCurrencyItem> {
        val url = URL("http://cbu.uz/uzc/arkhiv-kursov-valyut/json/")
        val list = ArrayList<MyCurrencyItem>()
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.connect()
        val inputStream = connection.inputStream
        val bufferReader = inputStream.bufferedReader()
        val gsonString = bufferReader.readLine()
        val gson = Gson()

        val type = object : TypeToken<ArrayList<MyCurrencyItem>>() {}.type
        list.addAll(gson.fromJson<ArrayList<MyCurrencyItem>>(gsonString, type))

        return list
    }

    /** getting data in background */
    val myTask = @SuppressLint("StaticFieldLeak")
    object : AsyncTask<Void, Void, Void>() {
        override fun onPreExecute() {
            super.onPreExecute()
            binding.myProgressBar.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg p0: Void?): Void? {
            if (binding.tvNet.text.toString() == "Internet is available") {
                list = getCurrency()
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            if (binding.tvNet.text.toString() == "Internet is available") {
                binding.myProgressBar.visibility = View.GONE
                binding.myRv.adapter = RvAdapter(this@MainActivity, list, this@MainActivity)
            }
        }

    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    override fun itemClicked(user: MyCurrencyItem) {

        val alertDialogLayoutBinding =
            DialogLayoutBinding.inflate(layoutInflater)

        alertDialogLayoutBinding.apply {
            flagImage1.countryCode = user.Ccy.substring(0, 2)
            flagImage2.countryCode = "Uz"
            tvName1.text = user.Ccy
            tvName2.text = "UZS"
            edt1.setText("1")
            edt2.setText(user.Rate)

            var whichCurState = 0

            var edtText1 = ""
            var edtText2 = ""


            btnExchange.setOnClickListener {

                if (whichCurState == 0) {
                    whichCurState = 1
                    img2Back.background = getDrawable(R.drawable.image_back)
                    img1Back.background = getDrawable(R.drawable.image_back_none)
                } else {
                    whichCurState = 0
                    img1Back.background = getDrawable(R.drawable.image_back)
                    img2Back.background = getDrawable(R.drawable.image_back_none)

                }
            }

            btnCalculate.setOnClickListener {
                edtText1 = edt1.text.toString()
                edtText2 = edt2.text.toString()

                if (whichCurState == 0) {
                    if (edtText1.isNotEmpty()) {
                        edt2.setText(
                            "%.2f".format(
                                edt1.text.toString().toDouble() * user.Rate.toDouble()
                            )
                        )
                    }
                    return@setOnClickListener
                }
                if (edtText2.isNotEmpty()) {
                    edt1.setText(
                        "%.2f".format(
                            edt1.text.toString().toDouble() / user.Rate.toDouble()
                        )
                    )
                }

            }


        }

        val dialog: AlertDialog =
            AlertDialog.Builder(this)
                .setView(alertDialogLayoutBinding.root)
                .setNegativeButton("Yopish", null)
                .create()
        dialog.show()


    }


}

