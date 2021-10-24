package tech.volkov.clickhousestockscore.storage

import tech.volkov.clickhousestockscore.entity.StockEntity
import java.sql.ResultSet

interface ClickHouseStorage {

    fun save(stocksEntities: List<StockEntity>)

    fun <T> selectByQuery(query: String, extractFunction: (ResultSet) -> List<T>): List<T>
}
