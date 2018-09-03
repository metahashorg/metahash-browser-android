package org.metahash.browser

import android.animation.Animator
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.Window
import android.view.WindowManager
import android.webkit.*
import android.widget.TextView
import java.util.regex.Pattern
import okhttp3.OkHttpClient
import okhttp3.Request
import android.webkit.WebResourceResponse
import android.widget.Toast
import org.metahash.browser.data.WebViewState
import org.metahash.browser.extentions.bind
import org.metahash.browser.extentions.hideKeyboard
import org.metahash.browser.extentions.makeGone
import org.metahash.browser.presentation.view.EditUrlView


class MainActivity : AppCompatActivity() {

    private val vRoot by bind<View>(R.id.vRoot)
    private val vUrl by bind<View>(R.id.vUrl)
    private val ivHome by bind<View>(R.id.ivHome)
    private val tvUrl by bind<TextView>(R.id.tvUrl)
    private val webView by bind<WebView>(R.id.wv)
    private val vEditUrl by bind<EditUrlView>(R.id.vEditUrl)

    private val urlPatterns = arrayOf(
            Pattern.compile("mh://0x[a-zA-z0-9]{50}/"),
            Pattern.compile("mh://0x[a-zA-z0-9]{50}"),
            Pattern.compile("0x[a-zA-z0-9]{50}/"),
            Pattern.compile("0x[a-zA-z0-9]{50}")
    )
    private var address = ""

    private val BASE_URL = "http://31.172.81.6:80/"
    private val HEADER = "Host"
    private var state = WebViewState.IDLE

    private val appUrl: String by lazy { intent.getStringExtra(DATA_URL) }

    companion object {
        private const val DATA_URL = "data_url"

        fun openActivity(context: Context, url: String) {
            context.startActivity(Intent(context, MainActivity::class.java).apply {
                putExtra(DATA_URL, url)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        initWebView()
        initView()
        vEditUrl.makeGone()

        if (appUrl.isNotEmpty()) {
            processUrl(appUrl)
            vEditUrl.setUrl(appUrl)
        }
    }

    private fun initView() {
        ivHome.setOnClickListener {
            finish()
        }
        vUrl.setOnClickListener {
            showEditUrlView()
        }
        vEditUrl.onCancelClick = {
            hideKeyboard(tvUrl.windowToken)
            hideEditUrlView()
        }
        vEditUrl.onUrlEntered = {
            hideKeyboard(tvUrl.windowToken)
            hideEditUrlView()
            processUrl(it)
        }
        vEditUrl.onRefreshClick = {
            hideKeyboard(tvUrl.windowToken)
            hideEditUrlView()
            webView.reload()
        }
        vEditUrl.onStopClick = {
            hideKeyboard(tvUrl.windowToken)
            hideEditUrlView()
            webView.stopLoading()
        }
    }

    private fun processUrl(url: String) {
        if (checkUrl(url)) {
            getAddressFromUrl(url)
            loadUrl(BASE_URL)
            tvUrl.text = url
        } else {
            Toast.makeText(this, "Incorrect address", Toast.LENGTH_SHORT).show()
        }
    }
    private fun getAddressFromUrl(url: String) {
        val matcher = urlPatterns[3].matcher(url)
        address = if (matcher.find()) {
            matcher.group(0)
        } else {
            ""
        }
    }

    private fun initWebView() {
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(true)
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = HeadersClient()
    }

    private fun loadUrl(url: String) {
        webView.loadUrl(url)
    }

    private fun checkUrl(url: String): Boolean {
        urlPatterns.forEach {
            val matcher = it.matcher(url)
            if (matcher.matches()) {
                try {
                    val res = matcher.group(0)
                    if (res.isNotEmpty()) {
                        return true
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
        return false
    }

    private fun showEditUrlView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animateRevealEditUrlView()
        }
        vEditUrl.show(state)
    }

    private fun hideEditUrlView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animateHideEditUrlView()
        } else {
            vEditUrl.makeGone()
        }
    }

    override fun onBackPressed() {
        if (vEditUrl.visibility == View.VISIBLE) {
            hideEditUrlView()
        } else {
            super.onBackPressed()
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun animateHideEditUrlView() {
        val x = vRoot.right
        val y = vRoot.top
        val startRadius = 0F
        val endRadius = Math.hypot(vRoot.width.toDouble(), vRoot.height.toDouble()).toFloat()

        val anim = ViewAnimationUtils.createCircularReveal(vEditUrl, x, y, endRadius, startRadius)
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                vEditUrl.makeGone()
            }
        })
        anim.start()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun animateRevealEditUrlView() {
        val x = vRoot.right
        val y = vRoot.top
        val startRadius = 0F
        val endRadius = Math.hypot(vRoot.width.toDouble(), vRoot.height.toDouble()).toFloat()

        val anim = ViewAnimationUtils.createCircularReveal(vEditUrl, x, y, startRadius, endRadius)
        anim.start()
    }

    inner class HeadersClient : WebViewClient() {

        private val client = OkHttpClient()

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            state = WebViewState.LOADING
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            state = WebViewState.LOADED
            super.onPageFinished(view, url)
        }


        @SuppressWarnings("deprecation")
        override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
            return intercept(url ?: "")
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
            return intercept(request?.url?.toString() ?: "")
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            state = WebViewState.LOADED
            super.onReceivedError(view, request, error)
        }

        @SuppressWarnings("deprecation")
        override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
            state = WebViewState.LOADED
            super.onReceivedError(view, errorCode, description, failingUrl)
        }

        private fun intercept(url: String): WebResourceResponse? {
            try {
                val request = Request.Builder()
                        .url(url.trim())
                        .addHeader(HEADER, address) // Example header
                        .build()
                val response = client.newCall(request).execute()

                return WebResourceResponse(
                        null,
                        response.header("content-encoding", "utf-8"),
                        response.body()?.byteStream()
                )
            } catch (ex: Exception) {
                ex.printStackTrace()
                return null
            }
        }
    }
}
