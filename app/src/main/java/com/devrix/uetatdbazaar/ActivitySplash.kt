package com.devrix.uetatdbazaar

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import androidx.core.view.updateLayoutParams
import com.devrix.uetatdbazaar.databinding.ActivitySplashBinding
import com.google.android.material.progressindicator.LinearProgressIndicator
import android.provider.Settings
import com.devrix.uetatdbazaar.Activities.ActivityHome
import com.devrix.uetatdbazaar.Activities.ActivityLogin
import com.google.firebase.auth.FirebaseAuth

class ActivitySplash : AppCompatActivity() {

    lateinit var binding : ActivitySplashBinding
    lateinit var auth : FirebaseAuth

    // For Animation
    private val splashDuration = 3000L
    private val appName = "Campus Bazaar"

    private lateinit var logo: View
    private lateinit var ring1: View
    private lateinit var ring2: View
    private lateinit var titleRow: LinearLayout
    private lateinit var tagline: View
    private lateinit var progress: LinearProgressIndicator

    private var destination: Class<*>? = null
    private var animationDone = false
    private var navigated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)



        initializeEveryThing()

    }

    // My Own functions
    fun initializeEveryThing()
    {

        auth = FirebaseAuth.getInstance()

        logo = findViewById(R.id.logo)
        ring1 = findViewById(R.id.ring1)
        ring2 = findViewById(R.id.ring2)
        titleRow = findViewById(R.id.titleRow)
        tagline = findViewById(R.id.tagline)
        progress = findViewById(R.id.progress)

        // Edge-to-edge: keep the progress bar above the navigation bar
        val baseMargin = progress.run { (layoutParams as android.widget.FrameLayout.LayoutParams).bottomMargin }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root)) { _, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            progress.updateLayoutParams<android.widget.FrameLayout.LayoutParams> {
                bottomMargin = baseMargin + bars.bottom
            }
            insets
        }

        buildTitleLetters()


        if (animationsDisabled()) {
            // Respect the system "remove animations" setting
            logo.alpha = 1f
            tagline.alpha = 1f
            progress.progress = 100
            titleRow.children().forEach { it.alpha = 1f; it.translationY = 0f }
            animationDone = true
            progress.postDelayed({ navigate() }, 600)
        } else {
            playAnimations()
        }
    }

    fun playAnimations() {
        // Logo: scale in + rotate
        logo.scaleX = 0.3f
        logo.scaleY = 0.3f
        logo.rotation = -140f
        val logoAnim = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(logo, View.ALPHA, 0f, 1f),
                ObjectAnimator.ofFloat(logo, View.SCALE_X, 0.3f, 1f),
                ObjectAnimator.ofFloat(logo, View.SCALE_Y, 0.3f, 1f),
                ObjectAnimator.ofFloat(logo, View.ROTATION, -140f, 0f)
            )
            duration = 1000
            startDelay = 100
            interpolator = OvershootInterpolator(1.1f)
        }

        // Pulse rings (repeat)
        pulse(ring1, 500)
        pulse(ring2, 1300)

        // Title letters slide up one by one
        val density = resources.displayMetrics.density
        titleRow.children.forEachIndexed { i, v ->
            v.translationY = 40f * density
            v.animate()
                .alpha(1f).translationY(0f)
                .setStartDelay(900L + i * 50L)
                .setDuration(600)
                .setInterpolator(DecelerateInterpolator(2f))
                .start()
        }

        // Tagline fade in
        tagline.animate().alpha(1f).setStartDelay(1600).setDuration(800).start()

        // Progress bar fills
        ObjectAnimator.ofInt(progress, "progress", 0, 100).apply {
            duration = splashDuration - 200
            startDelay = 200
            interpolator = android.view.animation.AccelerateDecelerateInterpolator()
            addListener(object : android.animation.AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    animationDone = true
                    checkAuth()
                    navigate()
                }
            })
            start()
        }

        logoAnim.start()


    }

     fun pulse(ring: View, delay: Long) {
        val set = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(ring, View.SCALE_X, 0.6f, 2.3f),
                ObjectAnimator.ofFloat(ring, View.SCALE_Y, 0.6f, 2.3f),
                ObjectAnimator.ofFloat(ring, View.ALPHA, 0.9f, 0f)
            )
            duration = 2400
        }
        // Repeat by restarting on end, because AnimatorSet cannot repeat directly
        set.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: android.animation.Animator) {
                if (!navigated) set.start()
            }
        })
        ring.postDelayed({ if (!navigated) set.start() }, delay)
    }

    /** Navigates only when both the animation and the auth check are finished. */
    fun navigate() {
        val target = destination
        if (navigated || !animationDone || target == null) return
        navigated = true
        startActivity(Intent(this, target))
        finish()
        @Suppress("DEPRECATION")
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

    }

     fun buildTitleLetters() {
        val font = ResourcesCompat.getFont(this, R.font.plusjakartasansregular)
        appName.forEach { ch ->
            val tv = TextView(this).apply {
                text = if (ch == ' ') "\u00A0" else ch.toString()
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
                setTextColor(getColor(R.color.dark_rust))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    typeface = Typeface.create(font, 800, false)
                }
                gravity = Gravity.CENTER
                alpha = 0f
            }
            titleRow.addView(tv)
        }
    }

     fun animationsDisabled(): Boolean =
        Settings.Global.getFloat(contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f

     fun LinearLayout.children(): List<View> = (0 until childCount).map { getChildAt(it) }

    private fun checkAuth() {
        val user = FirebaseAuth.getInstance().currentUser

        if (user == null)
        {
            startActivity(Intent(this@ActivitySplash, ActivityLogin::class.java))
        }
        else{
            startActivity(Intent(this@ActivitySplash, ActivityHome::class.java))
        }
        finish()
    }
}




