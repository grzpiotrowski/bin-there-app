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
            logAll()
        }
    }

    fun logAll() {
        pois.forEach{ i("${it}") }
    }
}