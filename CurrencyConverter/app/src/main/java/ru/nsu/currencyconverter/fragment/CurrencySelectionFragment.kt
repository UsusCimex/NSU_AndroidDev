package ru.nsu.currencyconverter.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.nsu.currencyconverter.adapter.CurrencyAdapter
import ru.nsu.currencyconverter.databinding.FragmentCurrencySelectionBinding
import ru.nsu.currencyconverter.listener.CurrencySelectionListener
import ru.nsu.currencyconverter.model.Currency
import ru.nsu.currencyconverter.model.CurrencyType

class CurrencySelectionFragment : Fragment() {

    private var _binding: FragmentCurrencySelectionBinding? = null
    private val binding get() = _binding!!
    private lateinit var currencyList: List<Currency>
    private lateinit var type: CurrencyType

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrencySelectionBinding.inflate(inflater, container, false)
        Log.d("CurrencySelectionFragment", "onCreateView вызван")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currencyList = arguments?.getSerializable("currencyList") as List<Currency>
        type = arguments?.getSerializable("type") as CurrencyType
        Log.d("CurrencySelectionFragment", "Размер списка валют: ${currencyList.size}, тип: $type")

        val adapter = CurrencyAdapter(currencyList) { selectedCurrency ->
            Log.d("CurrencySelectionFragment", "Выбрана валюта: ${selectedCurrency.Name}")
            (activity as? CurrencySelectionListener)?.onCurrencySelected(selectedCurrency, type)
            parentFragmentManager.popBackStack()
        }

        binding.recyclerViewCurrencies.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCurrencies.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("CurrencySelectionFragment", "onDestroyView вызван")
    }

    companion object {
        fun newInstance(type: CurrencyType, currencyList: List<Currency>): CurrencySelectionFragment {
            val fragment = CurrencySelectionFragment()
            val args = Bundle().apply {
                putSerializable("currencyList", ArrayList(currencyList))
                putSerializable("type", type)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
