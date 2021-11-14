package tech.volkov.clickhousestocksloader.runners

import mu.KLogging
import org.springframework.boot.CommandLineRunner
import org.springframework.util.ResourceUtils
import tech.volkov.clickhousestockscore.storage.ClickHouseStorage
import tech.volkov.clickhousestocksloader.processors.DataBatchProcessor
import java.io.FileReader

class LoaderRunner(
    private val dataBatchProcessor: DataBatchProcessor,
    private val clickHouseStorage: ClickHouseStorage,
    private val batchSize: Int
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        val reader = FileReader(ResourceUtils.getFile("classpath:all_stocks_5yr.csv"))
        logger.info("Start loading data")
        val count = dataBatchProcessor.forEachBatch(reader, batchSize) { batch ->
            clickHouseStorage.save(batch)
        }
        logger.info("Data was loaded : $count rows")
    }

    companion object : KLogging()
}
