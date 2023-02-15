package little.goose.home.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import little.goose.home.databinding.ItemHomeWidgetBinding

class HomeWidgetViewHolder(private val binding: ItemHomeWidgetBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun setData(item: ItemHomeWidget, onImgClickListener: View.OnClickListener? = null) {
        binding.apply {
            tvTitle.text = item.title
            tvContent.text = item.content
            ivIcon.setImageResource(item.icon)
            ivIcon.setOnClickListener(onImgClickListener)
        }
    }

    fun updateImgResource(res: Int) {
        binding.ivIcon.setImageResource(res)
    }

}