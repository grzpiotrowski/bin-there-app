package ie.setu.bin_there_app.api

import ie.setu.bin_there_app.models.PoiModel
import retrofit2.Call
import retrofit2.http.*


interface PoiService {
    @GET("/pois")
    fun findall(): Call<List<PoiModel>>

    @GET("/pois/{email}")
    fun findall(@Path("email") email: String?)
            : Call<List<PoiModel>>

    @GET("/pois/{email}/{id}")
    fun get(@Path("email") email: String?,
            @Path("id") id: String): Call<PoiModel>

    @DELETE("/pois/{email}/{id}")
    fun delete(@Path("email") email: String?,
               @Path("id") id: String): Call<PoiWrapper>

    @POST("/pois/{email}")
    fun post(@Path("email") email: String?,
             @Body poi: PoiModel)
            : Call<PoiWrapper>

    @PUT("/pois/{email}/{id}")
    fun put(@Path("email") email: String?,
            @Path("id") id: String,
            @Body poi: PoiModel
    ): Call<PoiWrapper>
}

