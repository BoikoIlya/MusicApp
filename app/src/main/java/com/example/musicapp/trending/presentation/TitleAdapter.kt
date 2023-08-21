package com.example.musicapp.trending.presentation

import android.graphics.Typeface
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.musicapp.databinding.TitleItemBinding

/**
 * Created by HP on 07.08.2023.
 **/
class TitleAdapter(
    private val text: String,
    private val textSize:Float,
    private val margins: List<Int>,
    private val typeface: Int,
    private val dpToPx: Float
): RecyclerView.Adapter<TitleViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        return TitleViewHolder(
            TitleItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),dpToPx)
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) =
        holder.bind(text,textSize,typeface,margins)

}

class TitleViewHolder(
    private val binding: TitleItemBinding,
    private val dpToPx: Float
): ViewHolder(binding.root){

    fun bind(
        text: String,
        textSize: Float,
        typeface: Int,
        margins: List<Int>
    ){
        val leftMarginInPx = (margins[0] * dpToPx).toInt()
        val topMarginInPx = (margins[1] * dpToPx).toInt()
        val rightMarginInPx = (margins[2] * dpToPx).toInt()
        val bottomMarginInPx = (margins[3] * dpToPx).toInt()

        val layoutParams = binding.root.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(leftMarginInPx, topMarginInPx, rightMarginInPx, bottomMarginInPx)
        binding.root.layoutParams = layoutParams

        binding.root.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize)
        binding.root.setTypeface(null,typeface)
        binding.root.text = text
    }


}