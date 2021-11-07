package tech.volkov.clickhousestocks.backend.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tech.volkov.clickhousestocks.backend.model.CompanyDto
import tech.volkov.clickhousestocks.backend.retrofit.CompaniesRepositoryClient
import tech.volkov.clickhousestocks.backend.retrofit.RetrofitBuilder
import tech.volkov.clickhousestocks.backend.utils.getOrNull
import javax.inject.Inject

@Suppress("BlockingMethodInNonBlockingContext")
class CompaniesRepository @Inject constructor() {

    private val client = RetrofitBuilder.retrofit.create(CompaniesRepositoryClient::class.java)

    suspend fun getCompanies(): List<CompanyDto>? = withContext(Dispatchers.IO) {
        return@withContext getOrNull { client.getCompanies().execute().body() }
    }
}
