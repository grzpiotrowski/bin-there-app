package ie.setu.bin_there_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ie.setu.bin_there_app.databinding.ActivityWelcomeBinding
import ie.setu.bin_there_app.views.login.LoginView
import ie.setu.bin_there_app.views.signup.SignupView
import timber.log.Timber.i

class WelcomeView : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
