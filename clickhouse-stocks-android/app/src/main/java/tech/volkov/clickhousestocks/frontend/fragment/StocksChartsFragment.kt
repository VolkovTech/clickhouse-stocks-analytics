package tech.volkov.clickhousestocks.frontend.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_stocks_charts.*
import kotlinx.android.synthetic.main.fragment_stocks_charts.view.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import tech.volkov.clickhousestocks.R
import tech.volkov.clickhousestocks.backend.model.StockDto
import tech.volkov.clickhousestocks.backend.presenter.StocksChartsPresenter
import tech.volkov.clickhousestocks.frontend.view.StocksChartsView

class StocksChartsFragment : MvpAppCompatFragment(), StocksChartsView {

    private lateinit var mainActivity: AppCompatActivity

    @InjectPresenter
    lateinit var stocksChartsPresenter: StocksChartsPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_stocks_charts, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        mainActivity = activity as AppCompatActivity
        stocksChartsPresenter.getCompanies()
        setOnClickListeners(view)
    }

    private fun setOnClickListeners(view: View) {
        view.chartsSearchButton.setOnClickListener {
            stocksChartsPresenter.getStocks(
                dateStart = chartsStartDate.text.toString(),
                dateEnd = chartsEndDate.text.toString(),
                company = chartsCompanySpinner.selectedItem.toString()
            )
        }
    }

    override fun fillInCompanies(companies: List<String>) {
        val adapter = ArrayAdapter(
            mainActivity,
            android.R.layout.simple_spinner_item,
            companies
        )
        chartsCompanySpinner.adapter = adapter
    }

    override fun fillInCharts(stocks: List<StockDto>) {
        // fill in stocks
        println("")
    }

    override fun showErrorMessage(message: String) {
        Toasty.error(mainActivity, message, Toast.LENGTH_SHORT, true).show()
    }
}
