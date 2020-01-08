package com.womeiyouyuming.android.viewpager2

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.abs
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewPager()

    }


    private fun initViewPager() {


        val bgList = listOf(
            R.mipmap.bg1,
            R.mipmap.bg2,
            R.mipmap.bg3,
            R.mipmap.bg4,
            R.mipmap.bg5
        )





        val pageTransformer = CompositePageTransformer().apply {
            //设置页面的间距和缩放效果
            addTransformer(MarginPageTransformer(10))
            addTransformer(ZoomPageTransformer)
        }


        val adapter = MyAdapter()

        viewPager.apply {
            setPageTransformer(pageTransformer)
            this.adapter = adapter
        }


        //获取viewpager内部recyclerview，设置padding用来显示多个页面
        (viewPager.getChildAt(0) as RecyclerView).apply {
            setPadding(50, 0, 50, 0)
            clipToPadding = false
        }


        adapter.submitList(bgList)




    }


    //viewpager的页面缩放效果

    object ZoomPageTransformer : ViewPager2.PageTransformer {


        private const val MIN_SCAN = 0.90f

        //position 当页面居中显示时为0，滑出左边屏幕为-1，滑出右边屏幕为1
        override fun transformPage(page: View, position: Float) {

            page.scaleY = when {
                position < -1 -> {
                    MIN_SCAN
                }
                position <= 1 -> {
                    val rate = 1.0f - position.absoluteValue / 1.0f
                    MIN_SCAN + rate * (1.0f - MIN_SCAN)
                }
                else -> {
                    MIN_SCAN
                }
            }


        }
    }



    //viewpager的adapter

    class MyAdapter : ListAdapter<Int, MyAdapter.MyHolder>(MyDiffCallback) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_view_pager, parent, false)
            return MyHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyHolder, position: Int) {
            holder.bind(getItem(position))
        }

        object MyDiffCallback : DiffUtil.ItemCallback<Int>() {
            override fun areItemsTheSame(oldItem: Int, newItem: Int) = oldItem == newItem

            override fun areContentsTheSame(oldItem: Int, newItem: Int) = oldItem == newItem
        }


        class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            private val imageView = itemView.findViewById<ImageView>(R.id.imageView)

            fun bind(@DrawableRes resource: Int) {
                imageView.setImageResource(resource)
            }

        }

    }

}
