package ie.setu.bin_there_app.models

interface PoiStore {
    fun findAll(): List<PoiModel>
    fun create(poi: PoiModel)
    fun update(poi: PoiModel)
}