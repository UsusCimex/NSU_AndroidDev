package ru.nsu.currencyconverter.ui

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nsu.currencyconverter.databinding.FragmentConvertFromRubBinding
import ru.nsu.currencyconverter.model.Currency
import ru.nsu.currencyconverter.repository.CurrencyRepository

class ConvertFromRubFragment : Fragment() {

    private var _binding: FragmentConvertFromRubBinding? = null
    private val binding get() = _binding!!
    private val repository = CurrencyRepository()
    private var currencies: List<Currency> = emptyList()
    private var selectedCurrency: Currency? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentConvertFromRubBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchCurrencies()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupUI()
    }

    private fun setupUI() {
        binding.convertButton.setOnClickListener {
            convertCurrency()
        }
    }

    private fun fetchCurrencies() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getCurrencies()
            response?.let {
                currencies = it.Valute.values.toList()
                val currencyNames = currencies.map { currency -> currency.Name }

                requireActivity().runOnUiThread {
                    val adapter = ArrayAdapter(
                        requireContext(),
                        R.layout.simple_spinner_item,
                        currencyNames
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    binding.currencyAutoCompleteTextView.setAdapter(adapter)

                    binding.currencyAutoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
                        selectedCurrency = currencies[position]
                    }

                    binding.convertButton.setOnClickListener {
                        convertCurrency()
                    }
                }
                Log.d("ConvertFromRubFragment", "Currencies loaded successfully.")
            } ?: run {
                Log.e("ConvertFromRubFragment", "Failed to load currencies.")
            }
        }
    }

    private fun convertCurrency() {
        val amountText = binding.amountEditText.text.toString()
        val amountInRubles = amountText.toDoubleOrNull()
        val currency = selectedCurrency

        // Сброс ошибок
        binding.amountTextInputLayout.error = null
        binding.currencyTextInputLayout.error = null

        var isValid = true

        if (amountInRubles == null || amountInRubles <= 0) {
            binding.amountTextInputLayout.error = "Введите корректную сумму"
            isValid = false
        }

        if (currency == null) {
            binding.currencyTextInputLayout.error = "Выберите валюту"
            isValid = false
        }

        if (isValid) {
            val result = (amountInRubles!! / currency!!.Value) * currency.Nominal
            binding.resultTextView.text = String.format("%.2f %s", result, currency.CharCode)
            Log.d("ConvertFromRubFragment", "Conversion successful: $amountInRubles RUB = $result ${currency.CharCode}")
        } else {
            binding.resultTextView.text = ""
            Log.e("ConvertFromRubFragment", "Invalid input.")
        }
    }

    fun setSelectedCurrency(currency: Currency) {
        selectedCurrency = currency
        activity?.runOnUiThread {
            binding.currencyAutoCompleteTextView.setText(currency.Name, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
