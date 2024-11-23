package ru.nsu.currencyconverter.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nsu.currencyconverter.databinding.ItemCurrencyBinding
import ru.nsu.currencyconverter.model.Currency

class CurrencyAdapter(
    private val currencies: List<Currency>,
    private val onClick: (Currency) -> Unit
) : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding =
            ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(currencies[position])
    }

    override fun getItemCount(): Int = currencies.size

    inner class CurrencyViewHolder(private val binding: ItemCurrencyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(currency: Currency) {
            binding.textViewCurrencyName.text = currency.Name
            binding.textViewCurrencyRate.text = "Курс к рублю: ${currency.Value}"
            binding.root.setOnClickListener {
                Log.d("CurrencyAdapter", "Валюта нажата: ${currency.Name}")
                onClick(currency)
            }
        }
    }
}
