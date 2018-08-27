package com.shenjiajun.customizeviewdemo

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.main.activity_indicator.*
import kotlinx.android.synthetic.main.content_indicator.*
import kotlinx.android.synthetic.main.layout_indicator_item.view.*
import org.jetbrains.anko.backgroundColor

class IndicatorActivity : AppCompatActivity() {

    var titleList = arrayListOf("蓝", "黑", "灰", "绿", "红", "黄")
    var colorList = arrayListOf(Color.BLUE, Color.BLACK, Color.DKGRAY, Color.GREEN, Color.RED, Color.YELLOW)
    lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_indicator)
        setSupportActionBar(toolbar)

        viewPager.adapter = MyAdapter()
        indicatorView.pageNum = colorList.size
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                indicatorView.currentPage = position
            }

        })
    }


    inner class MyAdapter : PagerAdapter() {
        override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
            return view === `object`
        }

        override fun getCount(): Int {
            return colorList.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val layout = LayoutInflater.from(this@IndicatorActivity).inflate(R.layout.layout_indicator_item, null)
            layout.itemLayout.backgroundColor = colorList[position]
            container.addView(layout)
            return layout
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }
}
