package th.ac.su.ict.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import th.ac.su.ict.curencyconverter.MoneyService

class MainActivity : AppCompatActivity() {
    lateinit var edtInput: EditText
    lateinit var tvOutput1: TextView
    lateinit var tvOutput2: TextView

    var BASE_URL = "https://api.exchangeratesapi.io/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtInput = findViewById<EditText>(R.id.edtInput)
        tvOutput1 = findViewById<TextView>(R.id.tvOutput1)
        tvOutput2 = findViewById<TextView>(R.id.tvOutput2)

        val btnOk = findViewById<Button>(R.id.btnOK)

        btnOk.setOnClickListener {
            getCurrentRatesData()
        }
    }

    fun getCurrentRatesData() {

        val retrofit =Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create(MoneyService::class.java)
        val call = service.getCurrentRatesData("USD")

        call.enqueue(object : Callback<MoneyResponse>{
            override fun onFailure(call: Call<MoneyResponse>?, t: Throwable?) {
                TODO("Not yet implemented")
            }
            override fun onResponse(
                    call: Call<MoneyResponse>?,
                    response: Response<MoneyResponse>?
            ) {

                if (response!=null){
                    if (response.code() == 200){
                        val exchangeResponse = response.body()
                        val calculate = exchangeResponse.rates.THB.toDouble()
                        val exUnit:Double = edtInput.text.toString().toDouble()
                        val sum = (exUnit*calculate)
                        tvOutput1.text = "THB = ${sum.toString()}"
                        val showMoneyTHB = exchangeResponse.rates.THB.toString()
                        tvOutput2.text = "1 USD = ${showMoneyTHB.toString()}"
                    }
                }
            }
        })
    }
}