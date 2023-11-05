package ie.setu.bin_there_app.views.signup

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ie.setu.bin_there_app.R
import ie.setu.bin_there_app.databinding.ActivitySignupBinding
import ie.setu.bin_there_app.models.user.UserJSONStore
import ie.setu.bin_there_app.models.user.UserModel
import timber.log.Timber.i

class SignupView : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var presenter: SignupPresenter

    var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        i("Signup activity started.")

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = SignupPresenter(this, UserJSONStore(this))

        binding.signupButton.setOnClickListener {
            i("Bttn click")
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            presenter.onSignupClicked(email, password)
        }
    }

    fun onSignupSuccess() {
        Toast.makeText(this, "Signed up successfully", Toast.LENGTH_SHORT).show()

    }

    fun onSignupError(error: String) {
        i(error)
        Toast.makeText(this, "Signup error", Toast.LENGTH_SHORT).show()
    }

}
