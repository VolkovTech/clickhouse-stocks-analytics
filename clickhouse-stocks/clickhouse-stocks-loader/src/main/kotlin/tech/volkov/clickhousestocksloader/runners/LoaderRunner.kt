package tech.volkov.clickhousestocksloader.runners

import mu.KLogging
import org.springframework.boot.CommandLineRunner
import org.springframework.util.ResourceUtils
import tech.volkov.clickhousestockscore.entity.CLICKHOUSE_DATE_FORMAT
import tech.volkov.clickhousestockscore.entity.StockEntity
import tech.volkov.clickhousestockscore.storage.ClickHouseStorage
import tech.volkov.clickhousestocksloader.configuration.GeneratorProperties
import tech.volkov.clickhousestocksloader.services.DataBatchService
import tech.volkov.clickhousestocksloader.services.GeneratorService
import java.io.FileReader
import java.util.Date

class LoaderRunner(
    private val dataBatchService: DataBatchService,
    private val generatorService: GeneratorService,
    private val clickHouseStorage: ClickHouseStorage,
    private val batchSize: Int,
    private val generatorProperties: GeneratorProperties
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        val reader = FileReader(ResourceUtils.getFile("classpath:all_stocks_5yr.csv"))
        logger.info("Start loading data")
        val availableDates: MutableSet<Date> = mutableSetOf()
        val companiesExists: MutableSet<String> = mutableSetOf()
        val count = dataBatchService.forEachBatch(reader, batchSize) { batch ->
            clickHouseStorage.save(batch)
            processBatch(batch, availableDates, companiesExists)
        }
        val countGenerated = generateRows(availableDates, companiesExists)
        logger.info("Data was loaded : \n$count rows from dataset\n$countGenerated random rows was generated" +
                "\n${count + countGenerated} rows total")
    }

    private fun processBatch(
        batch: List<StockEntity>,
        availableDates: MutableSet<Date>,
        companiesExists: MutableSet<String>
    ) {
        batch.forEach { stockEntity ->
            if (stockEntity.date.after(CLICKHOUSE_DATE_FORMAT.parse(generatorProperties.dateStart)) &&
                stockEntity.date.before(CLICKHOUSE_DATE_FORMAT.parse(generatorProperties.dateEnd))) {
                availableDates.add(stockEntity.date)
            }
            companiesExists.add(stockEntity.company)
        }
    }

    private fun generateRows(availableDates: Set<Date>, companiesExists: Set<String>): Long {
        var countGenerated = 0L
        if (generatorProperties.enabled) {
            generatorService.generateRows(availableDates, companiesExists, generatorProperties.companiesCount)
                .chunked(batchSize) { batch ->
                    clickHouseStorage.save(batch)
                    countGenerated += batch.size
                }
        }
        return countGenerated
    }

    companion object : KLogging()
}
