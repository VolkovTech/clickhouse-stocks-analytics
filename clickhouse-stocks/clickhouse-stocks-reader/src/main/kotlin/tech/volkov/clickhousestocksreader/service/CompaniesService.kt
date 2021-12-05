package tech.volkov.clickhousestocksreader.service

import tech.volkov.clickhousestocksreader.dto.*

interface CompaniesService {

    fun getCompanies(): List<CompanyDto>
}
