package ie.setu.bin_there_app

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import ie.setu.bin_there_app.databinding.ActivityWelcomeBinding
import ie.setu.bin_there_app.views.login.LoginView
import ie.setu.bin_there_app.views.signup.SignupView
import timber.log.Timber.i

class WelcomeView : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This line installs the splash screen
        val splashScreen = installSplashScreen()

        // Set the splash screen transition
        splashScreen.setOnExitAnimationListener { splashScreenProvider ->
            val fadeOut = ObjectAnimator.ofFloat(
                splashScreenProvider.view,
                View.ALPHA,
                1f,
                0f
            )
            fadeOut.interpolator = AccelerateInterpolator()
            fadeOut.duration = 500L
            fadeOut.doOnEnd { splashScreenProvider.remove() }
            fadeOut.start()
        }

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val loginIntent = Intent(this, LoginView::class.java)
            startActivity(loginIntent)
        }

        binding.signupButton.setOnClickListener {
            val signupIntent = Intent(this, SignupView::class.java)
            startActivity(signupIntent)
        }
    }
}

