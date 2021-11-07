package tech.volkov.clickhousestocks.frontend.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_stocks_aggregation.*
import kotlinx.android.synthetic.main.fragment_stocks_aggregation.view.*
import kotlinx.android.synthetic.main.fragment_stocks_charts.*
import kotlinx.android.synthetic.main.fragment_stocks_charts.chartsEndDate
import kotlinx.android.synthetic.main.fragment_stocks_charts.chartsStartDate
import kotlinx.android.synthetic.main.fragment_stocks_charts.view.*
import kotlinx.android.synthetic.main.fragment_stocks_charts.view.chartsSearchButton
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import tech.volkov.clickhousestocks.R
import tech.volkov.clickhousestocks.backend.model.AggregateFunction
import tech.volkov.clickhousestocks.backend.model.AggregatedResultDto
import tech.volkov.clickhousestocks.backend.model.StockDto
import tech.volkov.clickhousestocks.backend.model.toAggregateFunction
import tech.volkov.clickhousestocks.backend.presenter.StocksAggregationPresenter
import tech.volkov.clickhousestocks.backend.presenter.StocksChartsPresenter
import tech.volkov.clickhousestocks.frontend.view.StocksAggregationView

class StocksAggregationFragment : MvpAppCompatFragment(), StocksAggregationView {

    private lateinit var mainActivity: AppCompatActivity

    @InjectPresenter
    lateinit var stocksAggregationPresenter: StocksAggregationPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_stocks_aggregation, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        mainActivity = activity as AppCompatActivity
        stocksAggregationPresenter.getCompanies()
        setOnClickListeners(view)
    }

    private fun fillInAggregationFunctions() {
        val functions = mutableListOf("Aggregation function")
            .also { it.addAll(AggregateFunction.values().map { it.toString() }) }
        val adapter = ArrayAdapter(
            mainActivity,
            android.R.layout.simple_spinner_item,
            functions
        )
        aggregationAggregationFunctionSpinner.adapter = adapter
    }

    private fun setOnClickListeners(view: View) {
        view.aggregationSearchButton.setOnClickListener {
            stocksAggregationPresenter.getStocksAggregation(
                dateStart = aggregationStartDate.text.toString(),
                dateEnd = aggregationEndDate.text.toString(),
                company = aggregationCompanySpinner.selectedItem.toString(),
                aggregateFunction = aggregationAggregationFunctionSpinner.selectedItem.toString().toAggregateFunction()!!
            )
        }
    }

    override fun fillInCompanies(companies: List<String>) {
        val adapter = ArrayAdapter(
            mainActivity,
            android.R.layout.simple_spinner_item,
            companies
        )
        aggregationCompanySpinner.adapter = adapter
    }

    override fun fillInStocksAggregation(aggregatedResultDto: AggregatedResultDto) {
        // fill in
    }

    override fun showErrorMessage(message: String) {
        Toasty.error(mainActivity, message, Toast.LENGTH_SHORT, true).show()

        val adapter = ArrayAdapter(
            mainActivity,
            android.R.layout.simple_spinner_item,
            listOf("Company", "MMM", "APPL")
        )
        aggregationCompanySpinner.adapter = adapter

        fillInAggregationFunctions()
    }
}
