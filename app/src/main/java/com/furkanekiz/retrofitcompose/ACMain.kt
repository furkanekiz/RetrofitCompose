package com.furkanekiz.retrofitcompose


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.furkanekiz.retrofitcompose.ui.theme.RetrofitComposeTheme
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.furkanekiz.retrofitcompose.model.CryptoModel
import com.furkanekiz.retrofitcompose.service.CryptoAPI
import com.furkanekiz.retrofitcompose.ui.theme.OnRowColor
import com.furkanekiz.retrofitcompose.ui.theme.RowColor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ACMain : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RetrofitComposeTheme {
                MainScreen()
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val BASE_URL = "https://raw.githubusercontent.com/"
    val cryptoModels = remember { mutableStateListOf<CryptoModel>() }

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CryptoAPI::class.java)

    val call = retrofit.getData()

    call.enqueue(object : Callback<List<CryptoModel>> {
        override fun onResponse(
            call: Call<List<CryptoModel>>,
            response: Response<List<CryptoModel>>
        ) {
            if (response.isSuccessful) {
                response.body()?.let {
                    //list
                    cryptoModels.addAll(it)
                }
            }
        }

        override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
            t.printStackTrace()
        }

    })

    Scaffold(topBar = { AppBar() }) {
        CryptoList(cryptos = cryptoModels)
    }
}

@Composable
fun AppBar() {
    TopAppBar(contentPadding = PaddingValues(10.dp), backgroundColor = OnRowColor) {
        Text(text = "Cryptocurrency", fontSize = 26.sp, color = Color.White)
    }
}

@Composable
fun CryptoList(cryptos: List<CryptoModel>) {

    LazyColumn {
        items(cryptos) { crypto ->
            CryptoRow(crypto = crypto)
        }
    }
}

@Composable
fun CryptoRow(crypto: CryptoModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = RowColor)
            //.padding(top = 1.dp, bottom = 1.dp)
    ) {
        Text(
            color = OnRowColor,
            text = crypto.currency,
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(2.dp)
                //.background(color = RowColor)
                //.fillMaxWidth(),

        )
        Text(
            color = OnRowColor,
            text = "${crypto.price} $",
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .padding(2.dp)
                //.background(color = RowColor)
                //.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RetrofitComposeTheme {
        CryptoRow(crypto = CryptoModel("FN", "44,666.22"))
    }
}