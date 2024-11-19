package ru.nsu.currencyconverter

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.nsu.currencyconverter.databinding.ActivityMainBinding
import ru.nsu.currencyconverter.model.Currency
import ru.nsu.currencyconverter.utils.CurrencyConverter
import ru.nsu.currencyconverter.client.CentralBankClient
import ru.nsu.currencyconverter.fragment.CurrencySelectionFragment
import ru.nsu.currencyconverter.listener.CurrencySelectionListener
import ru.nsu.currencyconverter.model.CurrencyType

class MainActivity : AppCompatActivity(), CurrencySelectionListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var client: CentralBankClient
    private lateinit var currencyConverter: CurrencyConverter

    private var selectedFromCurrency: Currency? = null
    private var selectedToCurrency: Currency? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        client = CentralBankClient()
        currencyConverter = CurrencyConverter()

        loadCurrencies()
        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        binding.buttonSelectFromCurrency.setOnClickListener {
            openCurrencySelectionFragment(CurrencyType.FROM)
        }

        binding.buttonSelectToCurrency.setOnClickListener {
            openCurrencySelectionFragment(CurrencyType.TO)
        }

        binding.convertButton.setOnClickListener { convertCurrency() }
    }

    private fun loadCurrencies() {
        // Используем lifecycleScope для запуска корутины
        lifecycleScope.launch {
            client.loadCurrencies(
                onSuccess = {
                    // Обновляем UI после успешной загрузки данных
                    binding.buttonSelectFromCurrency.isEnabled = true
                    binding.buttonSelectToCurrency.isEnabled = true
                },
                onError = {
                    Toast.makeText(this@MainActivity, "Ошибка загрузки данных валют", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun openCurrencySelectionFragment(type: CurrencyType) {
        val fragmentTag = "CurrencySelectionFragment"

        supportFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)

        // Создаем и открываем новый фрагмент выбора валюты
        val fragment = CurrencySelectionFragment.newInstance(type, client.getCurrencyList())
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, fragmentTag)
            .addToBackStack(null)
            .commit()
    }


    private fun convertCurrency() {
        val amount = binding.editTextAmount.text.toString().toDoubleOrNull()
        if (selectedFromCurrency == null || selectedToCurrency == null || amount == null) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        val result = currencyConverter.convert(amount, selectedFromCurrency!!, selectedToCurrency!!)
        binding.textViewResult.text = getString(
            R.string.conversion_result,
            amount,
            selectedFromCurrency!!.CharCode,
            result,
            selectedToCurrency!!.CharCode
        )
    }

    override fun onCurrencySelected(currency: Currency, type: CurrencyType) {
        when (type) {
            CurrencyType.FROM -> {
                selectedFromCurrency = currency
                binding.buttonSelectFromCurrency.text = currency.Name
            }
            CurrencyType.TO -> {
                selectedToCurrency = currency
                binding.buttonSelectToCurrency.text = currency.Name
            }
        }
    }
}
