package ie.setu.bin_there_app.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.setu.bin_there_app.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "pois.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<PoiModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class PoiJSONStore(private val context: Context) : PoiStore {

    var pois = mutableListOf<PoiModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<PoiModel> {
        logAll()
        return pois
    }

    override fun findById(id:Long) : PoiModel? {
        val foundPoi: PoiModel? = pois.find { it.id == id }
        return foundPoi
    }

    override fun create(poi: PoiModel) {
        poi.id = generateRandomId()
        pois.add(poi)
        serialize()
    }


    override fun update(poi: PoiModel) {
        // todo
    }

    override fun delete(poi: PoiModel) {
        pois.remove(poi)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(pois, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        pois = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        pois.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}