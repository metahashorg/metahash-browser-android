package org.metahash.browser

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.OkHttpClient
import org.metahash.browser.data.GetPopularResponse
import org.metahash.browser.data.PopularApp
import org.metahash.browser.data.ViewClickItem
import org.metahash.browser.domain.BrowserApi
import org.metahash.browser.domain.base.BaseObserver
import org.metahash.browser.domain.commands.GetPopularCommand
import org.metahash.browser.extentions.RxExt
import org.metahash.browser.extentions.bind
import org.metahash.browser.presentation.base.BaseActivity
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class HomeActivity : BaseActivity() {

    private val BASE_URL = "http://app.metahash.io/api/"
    private val api: BrowserApi by lazy {
        createRetrofit().create(BrowserApi::class.java)
    }
    private val getPopularCommad: GetPopularCommand by lazy {
        GetPopularCommand(api)
    }
    private lateinit var adapter: PopularAdapter
    private val recycler by bind<RecyclerView>(R.id.recycler)
    private val etSearch by bind<AppCompatEditText>(R.id.etSearch)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_home)
        initAdapter()
        getPopular()
        initSearch()
    }

    private fun initSearch() {
        val observer = BaseObserver<String>().apply {
            onNext = {
                adapter.filter.filter(it)
            }
        }
        addSubscription(
                RxExt.getTextChangeObservable(etSearch).debounce(250, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(observer))
    }

    private fun initAdapter() {
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = PopularAdapter(this, mutableListOf(), R.layout.item_application)
        recycler.adapter = adapter

        val observer = BaseObserver<ViewClickItem<PopularApp>>().apply {
            onNext = {
                MainActivity.openActivity(this@HomeActivity, it.item.url ?: "")
            }
        }

        addSubscription(adapter.getClick()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer))
    }

    private fun getPopular() {
        val observer = BaseObserver<GetPopularResponse>().apply {
            onNext = {
                adapter.addAll(it.data.toMutableList())
            }
            onError = {
                Toast.makeText(this@HomeActivity,
                        "Error while processing request",
                        Toast.LENGTH_SHORT).show()
                it.printStackTrace()
            }
        }
        addSubscription(getPopularCommad.execute()
                .subscribeWith(observer))
    }

    private fun createRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build()

        return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .baseUrl(BASE_URL)
                .build()
    }
}