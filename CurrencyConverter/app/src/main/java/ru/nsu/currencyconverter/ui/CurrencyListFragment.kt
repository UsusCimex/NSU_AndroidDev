package ru.nsu.currencyconverter.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nsu.currencyconverter.R
import ru.nsu.currencyconverter.databinding.FragmentCurrencyListBinding
import ru.nsu.currencyconverter.model.Currency
import ru.nsu.currencyconverter.repository.CurrencyRepository


class CurrencyListFragment : Fragment() {

    private var _binding: FragmentCurrencyListBinding? = null
    private val binding get() = _binding!!
    private val repository = CurrencyRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCurrencyListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fetchCurrencies()
    }

    private fun fetchCurrencies() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getCurrencies()
            response?.let {
                val currencies = it.Valute.values.toList()
                requireActivity().runOnUiThread {
                    setupRecyclerView(currencies)
                }
                Log.d("CurrencyListFragment", "Currencies loaded successfully.")
            } ?: run {
                Log.e("CurrencyListFragment", "Failed to load currencies.")
            }
        }
    }

    private fun setupRecyclerView(currencies: List<Currency>) {
        val adapter = CurrencyAdapter(currencies) { currency ->
            val fragment = ConverterFragment(currency)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
            Log.d("CurrencyListFragment", "Opened ConverterFragment for ${currency.CharCode}.")
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
