package ie.setu.bin_there_app.models.user

interface UserStore {
    fun findAll(): List<UserModel>
    fun create(user: UserModel)
    fun update(user: UserModel)
    fun delete(user: UserModel)
    fun login(email: String, password: String) : UserModel?
    fun signup(email: String, password: String) : UserModel?
    fun findById(id:Long) : UserModel?
}