package ru.nsu.currencyconverter.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.nsu.currencyconverter.databinding.FragmentConverterBinding
import ru.nsu.currencyconverter.model.Currency

class ConverterFragment(private val currency: Currency) : Fragment() {

    private var _binding: FragmentConverterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentConverterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.currencyName.text = currency.Name

        binding.convertButton.setOnClickListener {
            val amountInRubles = binding.amountEditText.text.toString().toDoubleOrNull()
            if (amountInRubles != null) {
                val result = amountInRubles / currency.Value * currency.Nominal
                binding.resultTextView.text = String.format("%.2f %s", result, currency.CharCode)
                Log.d("ConverterFragment", "Conversion successful: $amountInRubles RUB = $result ${currency.CharCode}")
            } else {
                binding.resultTextView.text = "Введите корректную сумму"
                Log.e("ConverterFragment", "Invalid amount entered.")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}