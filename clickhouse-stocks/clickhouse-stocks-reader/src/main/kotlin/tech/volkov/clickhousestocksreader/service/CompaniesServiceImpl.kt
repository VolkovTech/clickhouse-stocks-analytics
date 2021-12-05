package tech.volkov.clickhousestocksreader.service

import org.springframework.stereotype.Service
import tech.volkov.clickhousestockscore.configuration.StocksClickHouseProperties
import tech.volkov.clickhousestockscore.entity.CompanyEntity
import tech.volkov.clickhousestockscore.entity.toCompanyEntity
import tech.volkov.clickhousestockscore.storage.ClickHouseStorage
import tech.volkov.clickhousestocksreader.dto.*

@Service
class CompaniesServiceImpl(
    private val clickHouseStorage: ClickHouseStorage,
    private val stocksClickHouseProperties: StocksClickHouseProperties
) : CompaniesService {

    override fun getCompanies(): List<CompanyDto> {
        val companyEntities = mutableListOf<CompanyEntity>()

        clickHouseStorage.selectByQuery(toQuery()) {
            while (it.next()) {
                companyEntities.add(it.toCompanyEntity())
            }
            companyEntities
        }

        val resultList = mutableListOf<CompanyDto>()

        companyEntities.forEach {
            resultList.add(it.toCompanyDto())
        }

        return resultList
    }

    private fun toQuery() = buildString {
        append("select distinct company from ${stocksClickHouseProperties.table} ")
        append("where ")
        append("company is not null")
    }

    private fun CompanyEntity.toCompanyDto() = CompanyDto(
        name = company
    )
}
