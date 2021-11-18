package tech.volkov.clickhousestocksloader.services

import com.opencsv.CSVReader
import tech.volkov.clickhousestockscore.entity.CLICKHOUSE_DATE_FORMAT
import tech.volkov.clickhousestockscore.entity.StockEntity
import java.io.Reader
import java.lang.NumberFormatException
import java.text.ParseException
import java.util.Date

class DataBatchService {

    fun forEachBatch(reader: Reader, batchSize: Int, process: (List<StockEntity>) -> Unit): Long {
        val csvReader = CSVReader(reader)
        var count = 0L
        csvReader.use {
            reader.use {
                val headers: Array<String> = csvReader.readNext() ?: throw IllegalArgumentException("source is empty")
                var batch = csvReader.nextBatch(batchSize, headers)
                while (batch.isNotEmpty()) {
                    process(batch)
                    count += batch.size
                    batch = csvReader.nextBatch(batchSize, headers)
                }
            }
        }
        return count
    }

    private fun CSVReader.nextBatch(batchSize: Int, headers: Array<String>): List<StockEntity> {
        var row: Array<String>?
        val batch = mutableListOf<StockEntity>()
        do {
            row = this.readNext()
            row?.let { batch.add(transform(it, headers)) }
        } while (row != null && batch.size < batchSize)
        return batch
    }

    private fun transform(row: Array<String>, headers: Array<String>): StockEntity {
        val props = headers.zip(row).toMap()
        return StockEntity(
            company = props["Name"] ?: "",
            date = props.parseDateField("Date", Date()),
            open = props.parseFloatField("Open", 0f),
            high = props.parseFloatField("High", 0f),
            low = props.parseFloatField("Low", 0f),
            close = props.parseFloatField("Close", 0f),
            volume = props.parseFloatField("Volume", 0f)
        )
    }

    private fun Map<String, String>.parseDateField(fieldName: String, defaultValue: Date): Date =
        this.parseField(fieldName, defaultValue) {
            try {
                CLICKHOUSE_DATE_FORMAT.parse(it)
            } catch (e: ParseException) {
                defaultValue
            }
        }

    private fun Map<String, String>.parseFloatField(fieldName: String, defaultValue: Float): Float =
        this.parseField(fieldName, defaultValue) {
            try {
                it.toFloat()
            } catch (e: NumberFormatException) {
                defaultValue
            }
        }

    private fun <T> Map<String, String>.parseField(fieldName: String, defaultValue: T, parser: (String) -> T): T {
        val value = this[fieldName]
        return when {
            value != null -> parser(value)
            else -> defaultValue
        }
    }
}
