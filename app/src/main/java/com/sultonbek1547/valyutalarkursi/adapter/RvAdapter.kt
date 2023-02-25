package com.sultonbek1547.valyutalarkursi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sultonbek1547.valyutalarkursi.databinding.RvItemBinding
import com.sultonbek1547.valyutalarkursi.model.MyCurrencyItem

class RvAdapter(val context: Context, val list: List<MyCurrencyItem>) :
    RecyclerView.Adapter<RvAdapter.Vh>() {

    inner class Vh(val itemRvBinding: RvItemBinding) : RecyclerView.ViewHolder(itemRvBinding.root) {
        fun onBind(user: MyCurrencyItem, position: Int) {
            itemRvBinding.apply {
                tvName.text = "1 "+user.CcyNm_UZ
                tvNumber.text = user.Rate+" So'm"

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) = holder.onBind(list[position], position)


    override fun getItemCount(): Int = list.size


}