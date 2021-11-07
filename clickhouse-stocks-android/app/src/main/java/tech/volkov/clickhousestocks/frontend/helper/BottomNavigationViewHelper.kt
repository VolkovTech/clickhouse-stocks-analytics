package tech.volkov.clickhousestocks.frontend.helper

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx

class BottomNavigationViewHelper {
    companion object {
        fun setupBottomNavigationView(bottomNavigationViewEx: BottomNavigationViewEx) {
            bottomNavigationViewEx.setIconSize(27f, 27f)
            bottomNavigationViewEx.setTextSize(14f)
            bottomNavigationViewEx.setIconsMarginTop(14)
        }
    }
}
