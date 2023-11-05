package ie.setu.bin_there_app.views.login

import ie.setu.bin_there_app.main.MainApp
import ie.setu.bin_there_app.models.user.UserStore
import timber.log.Timber.i

class LoginPresenter(val view: LoginView, val userStore: UserStore) {

    var app: MainApp
    init {
        app = view.application as MainApp
    }

    fun onLoginClicked(email: String, password: String) {
        val user = userStore.login(email, password)
        i("Login button clicked.")

        if (user != null) {
            view.onLoginSuccess()
            i("User logged in successfully.")
        } else {
            view.onLoginError("Invalid credentials or user does not exist!")
        }
    }

}
