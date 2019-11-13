package io.seroo.sampleplayer.common

import android.view.View
import androidx.recyclerview.widget.RecyclerView

sealed class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view)
abstract class BaseViewHolder1<in T>(view: View) : BaseViewHolder(view) {
    abstract fun bindView(item: T, position: Int)
}
