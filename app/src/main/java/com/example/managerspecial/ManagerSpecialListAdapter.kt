package com.example.managerspecial

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.managerspecial.databinding.ItemManagerSpecialBinding
import com.example.managerspecial.network.ManagerSpecial
import com.squareup.picasso.Picasso

class ManagerSpecialAdapter: RecyclerView.Adapter<ManagerSpecialViewHolder>() {
    var managerSpecialList: List<ManagerSpecial> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManagerSpecialViewHolder {
        val binding = ItemManagerSpecialBinding.inflate(
                LayoutInflater
                        .from(parent.context), parent, false)
        return ManagerSpecialViewHolder(binding, parent)
    }

    override fun onBindViewHolder(holder: ManagerSpecialViewHolder, position: Int) {
        holder.bind(managerSpecialList, position)
    }

    override fun getItemCount(): Int {
        return managerSpecialList.size
    }
}
class ManagerSpecialViewHolder(private val binding: ItemManagerSpecialBinding,
                               private val parent: ViewGroup) : RecyclerView.ViewHolder(binding.root) {
    fun bind(managerSpecialList: List<ManagerSpecial>, position: Int) {
        val item = managerSpecialList[position]
        binding.description.text = item.display_name
        binding.originalPrice.text = """${"$"}${item.original_price}"""
        binding.discountPrice.text = """${"$"}${item.price.toString()}"""
        binding.description.text = item.display_name

        Picasso.with(parent.context)
                .load(item.imageUrl)
                .into(binding.image)
    }
}
