package org.metahash.browser

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import org.metahash.browser.extentions.bind
import org.metahash.browser.extentions.fromUI
import org.metahash.browser.extentions.waitForLayout
import org.metahash.browser.presentation.view.ripple.RippleView

class SplashActivity : AppCompatActivity() {

    private val vNode by bind<ImageView>(R.id.vNode)
    private val vRipple by bind<RippleView>(R.id.vRipple)
    private val tvVersion by bind<TextView>(R.id.tvVersion)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        vNode.waitForLayout {
            vRipple.mBoundViewSize = Math.max(vNode.width, vNode.height)
            vRipple.start()
        }
        fromUI({
            startActivity(Intent(this, HomeActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }, 3000)

        tvVersion.text = String.format(getString(R.string.version), BuildConfig.VERSION_NAME)
    }
}