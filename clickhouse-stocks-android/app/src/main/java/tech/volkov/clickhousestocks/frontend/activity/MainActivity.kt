package tech.volkov.clickhousestocks.frontend.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import tech.volkov.clickhousestocks.R
import tech.volkov.clickhousestocks.frontend.fragment.StocksAggregationFragment
import tech.volkov.clickhousestocks.frontend.fragment.StocksChartsFragment
import tech.volkov.clickhousestocks.frontend.helper.BottomNavigationViewHelper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        bottomNavigationView.onNavigationItemSelectedListener = navListener
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView)

        val menu = bottomNavigationView.menu
        val menuItem = menu.getItem(ACTIVITY_NUM)
        menuItem.isChecked = true

        beginTransaction(StocksChartsFragment())
    }

    private val navListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment = StocksChartsFragment()
            when (item.itemId) {
                R.id.nav_item_charts -> selectedFragment = StocksChartsFragment()
                R.id.nav_item_aggregation -> selectedFragment = StocksAggregationFragment()
            }
            beginTransaction(selectedFragment)
            true
        }

    private fun beginTransaction(selectedFragment: Fragment) =
        supportFragmentManager.beginTransaction().replace(R.id.mainContainer, selectedFragment)
            .commit()

    companion object {
        // stocks charts fragment
        private const val ACTIVITY_NUM = 1
    }
}
