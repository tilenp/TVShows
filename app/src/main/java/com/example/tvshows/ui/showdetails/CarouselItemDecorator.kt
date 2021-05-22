package com.example.tvshows.ui.showdetails

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CarouselItemDecorator(
    private val margin: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val adapterPosition = parent.getChildAdapterPosition(view)
        val lastPosition = parent.adapter?.itemCount?.minus(1) ?: -1
        when (adapterPosition) {
            0 -> setMargins(outRect, 0, margin)
            lastPosition -> setMargins(outRect, margin, 0)
            else -> setMargins(outRect, margin, margin)
        }
    }

    private fun setMargins(outRect: Rect, marginLeft: Int, marginRight: Int) {
        outRect.top = margin
        outRect.bottom = margin
        outRect.left = marginLeft
        outRect.right = marginRight
    }
}
