package ie.setu.bin_there_app.models

import timber.log.Timber

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

object PoiManager : PoiStore {

    private val pois = ArrayList<PoiModel>()

    override fun findAll(): List<PoiModel> {
        return pois
    }

    override fun findById(id:Long) : PoiModel? {
        val foundPoi: PoiModel? = pois.find { it.id == id }
        return foundPoi
    }

    override fun create(poi: PoiModel) {
        poi.id = getId()
        pois.add(poi)
        logAll()
    }

    fun logAll() {
        Timber.v("** Pois List **")
        pois.forEach { Timber.v("Poi ${it}") }
    }
}