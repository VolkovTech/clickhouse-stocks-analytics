package tech.volkov.clickhousestocksloader.services

import tech.volkov.clickhousestockscore.entity.StockEntity
import java.util.Date
import kotlin.random.Random.Default.nextFloat
import kotlin.random.Random.Default.nextInt

class GeneratorService {

    fun generateRows(dates: Set<Date>, companiesExists: Set<String>, companiesCount: Int): List<StockEntity> {
        val companies = generateCompanies(companiesCount, companiesExists)
        var prevValue = randomFloat(MAX_PRICE_VALUE / 2f - BOUND, MAX_PRICE_VALUE / 2f + BOUND)
        var prevVolume = randomFloat(MAX_VOLUME_VALUE / 2f - BOUND_VOLUME, MAX_VOLUME_VALUE /2f + BOUND_VOLUME)
        return companies.flatMap { company ->
            dates.map{ date ->
                val row = generateRow(company, date, prevValue, prevVolume)
                prevValue = row.close
                prevVolume = row.volume
                row
            }.toList()
        }
    }

    private fun generateCompanies(companiesCount: Int, companiesExists: Set<String>): Set<String> {
        val companies: MutableSet<String> = mutableSetOf()
        while (companies.size < companiesCount) {
            val newCompany = generateName()
            if (!companiesExists.contains(newCompany)) {
                companies.add(newCompany)
            }
        }
        return companies
    }

    private fun generateName(): String {
        val length = nextInt(MIN_COMPANY_NAME_LENGTH, MAX_COMPANY_NAME_LENGTH + 1)
        val charPool: List<Char> = ('A'..'Z').toList()
        return (1..length)
            .map { charPool[nextInt(0, charPool.size)] }
            .joinToString(separator = "")
    }

    private fun generateRow(companyName: String, date: Date, prevValue: Float, prevVolume: Float): StockEntity {
        val open = randomFloat(prevValue - BOUND, prevValue + BOUND)
        val high = randomFloat(open, open + BOUND)
        val low = randomFloat(open - BOUND, open)
        return StockEntity(
            company = companyName,
            date = date,
            open = open,
            high = high,
            low = low,
            close = randomFloat(low, high),
            volume = randomFloat(prevVolume - BOUND_VOLUME, prevVolume + BOUND_VOLUME)
        )
    }

    private fun randomFloat(min: Float, max: Float): Float =
        min + nextFloat() * (max - min)

    companion object {
        const val MAX_PRICE_VALUE = 2100f
        const val MAX_VOLUME_VALUE = 658237630f
        const val MIN_COMPANY_NAME_LENGTH = 3
        const val MAX_COMPANY_NAME_LENGTH = 8
        const val BOUND = 20
        const val BOUND_VOLUME = 1000000
    }
}
