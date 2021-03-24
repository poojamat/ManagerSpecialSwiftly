package com.example.managerspecial

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.example.managerspecial.databinding.ItemManagerSpecialBinding
import com.example.managerspecial.network.ManagerSpecial
import com.squareup.picasso.Picasso

class ManagerSpecialStaggeredListAdapter: RecyclerView.Adapter<ManagerSpecialStaggeredListAdapterViewHolder>() {
    var managerSpecialList: List<ManagerSpecial> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManagerSpecialStaggeredListAdapterViewHolder {
        val binding = ItemManagerSpecialBinding.inflate(
                LayoutInflater
                        .from(parent.context), parent, false)
        return ManagerSpecialStaggeredListAdapterViewHolder(binding, parent)
    }

    override fun onBindViewHolder(holder: ManagerSpecialStaggeredListAdapterViewHolder, position: Int) {
        holder.bind(managerSpecialList, position)
    }

    override fun getItemCount(): Int {
        return managerSpecialList.size
    }
}
class ManagerSpecialStaggeredListAdapterViewHolder(private val binding: ItemManagerSpecialBinding,
                               private val parent: ViewGroup) : RecyclerView.ViewHolder(binding.root) {
    fun bind(managerSpecialList: List<ManagerSpecial>, position: Int) {
        val item = managerSpecialList[position]
        binding.description.text = item.display_name
        binding.originalPrice.text = """${"$"}${item.original_price}"""
        binding.discountPrice.text = """${"$"}${item.price}"""
        binding.description.text = item.display_name
        val ratio =String.format("h,%d:%d", item.width,item.height)
        val set = ConstraintSet()
        set.clone(binding.body)
        set.setDimensionRatio(binding.image.id, ratio)

        set.applyTo(binding.body)

        Picasso.with(parent.context)
                .load(item.imageUrl)
                .into(binding.image)
    }
}
