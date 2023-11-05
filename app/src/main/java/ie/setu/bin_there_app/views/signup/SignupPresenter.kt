package ie.setu.bin_there_app.views.signup

import ie.setu.bin_there_app.main.MainApp
import ie.setu.bin_there_app.models.user.UserStore
import ie.setu.bin_there_app.models.user.generateRandomUserId
import timber.log.Timber.i

class SignupPresenter(val view: SignupView, val userStore: UserStore) {

    var app: MainApp
    init {
        app = view.application as MainApp
    }

    fun onSignupClicked(email: String, password: String) {
        val newUser = userStore.signup(email, password)
        i("Signup button clicked.")

        if (newUser != null) {
            view.onSignupSuccess()
            i("New user signed up.")
        } else {
            view.onSignupError("User with this email already exists!")
        }
    }
}
