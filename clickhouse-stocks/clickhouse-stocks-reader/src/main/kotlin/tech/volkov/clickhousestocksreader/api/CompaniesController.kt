package tech.volkov.clickhousestocksreader.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.volkov.clickhousestocksreader.dto.*
import tech.volkov.clickhousestocksreader.service.CompaniesService

@RestController
@RequestMapping("/api/v1/companies")
class CompaniesController(
    private val companiesService: CompaniesService
) {

    @GetMapping
    fun getCompanies(): List<CompanyDto> {
        return companiesService.getCompanies()
    }
}
