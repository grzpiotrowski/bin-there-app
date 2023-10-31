package ie.setu.bin_there_app.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class PoiMemStore : PoiStore {
    val pois = ArrayList<PoiModel>()

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

    override fun update(poi: PoiModel) {
        var foundPoi: PoiModel? = pois.find { p -> p.id == poi.id }
        if (foundPoi != null) {
            foundPoi.title = poi.title
            foundPoi.description = poi.description
            foundPoi.image = poi.image
            foundPoi.location.lat = poi.location.lat
            foundPoi.location.lng = poi.location.lng
            foundPoi.location.zoom = poi.location.zoom
            logAll()
        }
    }

    override fun delete(poi: PoiModel) {
        pois.remove(poi)
    }

    fun logAll() {
        pois.forEach{ i("${it}") }
    }
}