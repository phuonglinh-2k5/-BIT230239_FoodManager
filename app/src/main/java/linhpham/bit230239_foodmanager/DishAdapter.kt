package linhpham.bit230239_foodmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import linhpham.bit230239_foodmanager.model.Dish

class DishAdapter(
    private var dishList: List<Dish>,
    private val onItemClick: (Dish) -> Unit
) : RecyclerView.Adapter<DishAdapter.DishViewHolder>() {

    inner class DishViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgThumbnail: ImageView = itemView.findViewById(R.id.imgThumbnail)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)

        fun bind(dish: Dish) {
            tvName.text = dish.name
            tvPrice.text = "Giá: ${dish.price} VNĐ"

            if (dish.preparationTime <= 15) {
                tvTime.text = "Quick / Nhanh (${dish.preparationTime}p)"
            } else {
                tvTime.text = "Regular / Thông thường (${dish.preparationTime}p)"
            }

            val thumbnail = dish.dishImages.find { it.isThumbnail } ?: dish.dishImages.firstOrNull()
            if (thumbnail != null) {
                Glide.with(itemView.context).load(thumbnail.imageUrl).into(imgThumbnail)
            }

            itemView.setOnClickListener { onItemClick(dish) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dish, parent, false)
        return DishViewHolder(view)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        holder.bind(dishList[position])
    }

    override fun getItemCount(): Int = dishList.size

    fun updateData(newList: List<Dish>) {
        dishList = newList
        notifyDataSetChanged()
    }
}