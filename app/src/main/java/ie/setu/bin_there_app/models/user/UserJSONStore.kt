package ie.setu.bin_there_app.models.user

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.setu.bin_there_app.helpers.*
import ie.setu.bin_there_app.models.poi.UriParser
import ie.setu.bin_there_app.models.poi.generateRandomId
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val USERS_JSON_FILE = "users.json"
val usersGsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val userListType: Type = object : TypeToken<ArrayList<UserModel>>() {}.type

fun generateRandomUserId(): Long {
    return Random().nextLong()
}

class UserJSONStore(private val context: Context) : UserStore {

    var users = mutableListOf<UserModel>()

    init {
        if (exists(context, USERS_JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<UserModel> {
        logAll()
        return users
    }

    override fun findById(id:Long) : UserModel? {
        val foundUser: UserModel? = users.find { it.id == id }
        return foundUser
    }

    override fun create(user: UserModel) {
        user.id = generateRandomId()
        users.add(user)
        serialize()
    }


    override fun update(user: UserModel) {
        val usersList = findAll() as ArrayList<UserModel>
        var foundUser: UserModel? = usersList.find { p -> p.id == user.id }
        if (foundUser != null) {
            foundUser.email = user.email
            foundUser.password = user.password
            foundUser.name = user.name
        }
        serialize()
    }

    override fun delete(user: UserModel) {
        users.remove(user)
        serialize()
    }

    override fun login(email: String, password: String): UserModel? {
        TODO("Not yet implemented")
    }

    override fun signup(email: String, password: String): UserModel? {
        val existingUser = users.find { it.email == email }
        if (existingUser != null) {
            return null
        }
        val user = UserModel(email = email, password = password, name = "")
        create(user)
        return user
    }

    private fun serialize() {
        val jsonString = usersGsonBuilder.toJson(users, userListType)
        write(context, USERS_JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, USERS_JSON_FILE)
        users = usersGsonBuilder.fromJson(jsonString, userListType)
    }

    private fun logAll() {
        users.forEach { Timber.i("$it") }
    }
}