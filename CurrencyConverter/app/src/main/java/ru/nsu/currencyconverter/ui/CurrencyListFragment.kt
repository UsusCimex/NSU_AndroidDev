package ru.nsu.currencyconverter.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nsu.currencyconverter.databinding.FragmentCurrencyListBinding
import ru.nsu.currencyconverter.model.Currency
import ru.nsu.currencyconverter.repository.CurrencyRepository


class CurrencyListFragment : Fragment() {

    private var _binding: FragmentCurrencyListBinding? = null
    private val binding get() = _binding!!
    private val repository = CurrencyRepository()
    private var currencySelectionListener: CurrencySelectionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCurrencyListBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDetach() {
        super.onDetach()
        currencySelectionListener = null
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CurrencySelectionListener) {
            currencySelectionListener = context
        } else {
            throw RuntimeException("$context должен реализовывать интерфейс CurrencySelectionListener")
        }
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
            currencySelectionListener?.onCurrencySelected(currency)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
