package ie.setu.bin_there_app.models

interface PoiStore {
    fun findAll() : List<PoiModel>
    fun findById(id: Long) : PoiModel?
    fun create(donation: PoiModel)
}