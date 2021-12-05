package tech.volkov.clickhousestockscore.entity

import java.sql.ResultSet

data class CompanyEntity (
    val company: String
)

fun ResultSet.toCompanyEntity() = CompanyEntity(
    company = getString("company")
)