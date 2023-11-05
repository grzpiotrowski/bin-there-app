package ie.setu.bin_there_app.views.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ie.setu.bin_there_app.databinding.ActivityLoginBinding
import ie.setu.bin_there_app.models.user.UserJSONStore
import ie.setu.bin_there_app.models.user.UserModel
import ie.setu.bin_there_app.views.poilist.PoiListView
import timber.log.Timber.i

class LoginView : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var presenter: LoginPresenter

    var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        i("Login activity started.")

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = LoginPresenter(this, UserJSONStore(this))

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            presenter.onLoginClicked(email, password)
        }
    }

    fun onLoginSuccess() {
        Toast.makeText(this, "Logged in successfully", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, PoiListView::class.java)
        startActivity(intent)
    }

    fun onLoginError(error: String) {
        Toast.makeText(this, "Login error", Toast.LENGTH_SHORT).show()
    }

}
