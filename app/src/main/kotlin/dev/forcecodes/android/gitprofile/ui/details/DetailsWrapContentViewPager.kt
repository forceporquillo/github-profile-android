package dev.forcecodes.android.gitprofile.ui.details

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.UiThread
import androidx.viewpager.widget.ViewPager

class DetailsWrapContentViewPager @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewPager(context, attrs) {

    private var currentPosition: Int = 0
    private var currentView: View? = null

    interface OnChildChangeListener : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            onDetailsChildSelected(position)
        }

        override fun onPageScrollStateChanged(state: Int) = Unit

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) = Unit

        fun onDetailsChildSelected(position: Int)
    }

    @UiThread
    fun measureCurrentChildHeight(position: Int) {
        this.currentPosition = position
        handler.post { requestLayout() }
    }

    @UiThread
    fun remeasureView(view: View) {
        currentView = view
        requestLayout()
    }

    fun addDetailsChildListener(listener: OnPageChangeListener) {
        addOnPageChangeListener(listener)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var newHeightMeasureSpec = heightMeasureSpec
        val mode = MeasureSpec.getMode(heightMeasureSpec)
        // Unspecified means that the ViewPager is in a ScrollView WRAP_CONTENT.
        // At Most means that the ViewPager is not in a ScrollView WRAP_CONTENT.
        if (mode == MeasureSpec.UNSPECIFIED || mode == MeasureSpec.AT_MOST) {
            // super has to be called in the beginning so the child views can be initialized.
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            var height = 0
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                child.measure(
                    widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                )
                val childMeasuredHeight = child.measuredHeight
                if (childMeasuredHeight > height) {
                    height = childMeasuredHeight
                }
            }
            newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        }

        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
    }
}