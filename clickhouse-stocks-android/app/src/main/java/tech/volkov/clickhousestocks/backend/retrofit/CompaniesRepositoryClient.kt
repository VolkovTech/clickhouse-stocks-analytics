package tech.volkov.clickhousestocks.backend.retrofit

import retrofit2.Call
import retrofit2.http.GET
import tech.volkov.clickhousestocks.backend.model.CompanyDto

interface CompaniesRepositoryClient {

    @GET("/api/v1/companies")
    fun getCompanies(): Call<List<CompanyDto>>
}
