package ie.setu.bin_there_app.models

class PoiMemStore : PoiStore {
    val pois = ArrayList<PoiModel>()

    override fun findAll(): List<PoiModel> {
        return pois
    }

    override fun create(poi: PoiModel) {
        pois.add(poi)
    }
}