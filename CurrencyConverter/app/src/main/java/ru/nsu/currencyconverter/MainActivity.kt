package ru.nsu.currencyconverter

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.nsu.currencyconverter.client.CentralBankClient
import ru.nsu.currencyconverter.databinding.ActivityMainBinding
import ru.nsu.currencyconverter.fragment.CurrencySelectionFragment
import ru.nsu.currencyconverter.listener.CurrencySelectionListener
import ru.nsu.currencyconverter.model.Currency
import ru.nsu.currencyconverter.model.CurrencyType
import ru.nsu.currencyconverter.utils.CurrencyConverter

class MainActivity : AppCompatActivity(), CurrencySelectionListener {

    private lateinit var binding: ActivityMainBinding;
    private var client: CentralBankClient = CentralBankClient()
    private var converter: CurrencyConverter = CurrencyConverter()

    private var selectedFromCurrency: Currency? = null
    private var selectedToCurrency: Currency? = null

    private var loadingDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate вызван")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadCurrencies()
        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        binding.buttonSelectFromCurrency.setOnClickListener {
            if (client.getCurrencyList().isEmpty()) {
                retryLoadCurrencies()
            } else {
                openCurrencySelectionFragment(CurrencyType.FROM)
            }
        }

        binding.buttonSelectToCurrency.setOnClickListener {
            if (client.getCurrencyList().isEmpty()) {
                retryLoadCurrencies()
            } else {
                openCurrencySelectionFragment(CurrencyType.TO)
            }
        }

        binding.convertButton.setOnClickListener { convertCurrency() }
    }

    private fun retryLoadCurrencies() {
        Toast.makeText(this, "Повторная загрузка данных...", Toast.LENGTH_SHORT).show()
        Log.d("MainActivity", "Повторная попытка загрузки валют")
        loadCurrencies()
    }

    private fun loadCurrencies() {
        showLoadingDialog()
        Log.d("MainActivity", "Начало загрузки валют")

        lifecycleScope.launch {
            client.loadCurrencies(
                onSuccess = {
                    hideLoadingDialog()
                    binding.buttonSelectFromCurrency.isEnabled = true
                    binding.buttonSelectToCurrency.isEnabled = true
                    Log.d("MainActivity", "Валюты успешно загружены")
                },
                onError = { errorMessage ->
                    hideLoadingDialog()
                    Log.e("MainActivity", "Ошибка загрузки валют: $errorMessage")
                    showRetryDialog(errorMessage)
                }
            )
        }
    }

    private fun showLoadingDialog(message: String = "Загрузка...") {
        val dialogView = layoutInflater.inflate(R.layout.dialog_loading, null)
        val loadingMessage = dialogView.findViewById<TextView>(R.id.loadingMessage)
        loadingMessage.text = message

        loadingDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()
        loadingDialog?.show()
        Log.d("MainActivity", "Показано диалоговое окно загрузки")
    }

    private fun hideLoadingDialog() {
        loadingDialog?.dismiss()
        loadingDialog = null
        Log.d("MainActivity", "Диалоговое окно загрузки скрыто")
    }

    private fun showRetryDialog(errorMessage: String) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Ошибка загрузки")
            .setMessage("$errorMessage. Попробовать снова?")
            .setPositiveButton("Повторить") { dialog, _ ->
                Log.d("MainActivity", "Пользователь выбрал повторную загрузку")
                dialog.dismiss()
                loadCurrencies()
            }
            .setNegativeButton("Выход") { dialog, _ ->
                Log.d("MainActivity", "Пользователь выбрал выход")
                dialog.dismiss()
                finish()
            }
            .create()

        dialog.show()
    }

    private fun openCurrencySelectionFragment(type: CurrencyType) {
        val fragmentTag = "CurrencySelectionFragment"

        supportFragmentManager.popBackStack(
            null,
            androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
        )

        val fragment = CurrencySelectionFragment.newInstance(type, client.getCurrencyList())
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, fragmentTag)
            .addToBackStack(null)
            .commit()
        Log.d("MainActivity", "Открыт CurrencySelectionFragment для $type")
    }

    private fun convertCurrency() {
        val amount = binding.editTextAmount.text.toString().toDoubleOrNull()
        if (selectedFromCurrency == null || selectedToCurrency == null || amount == null) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            Log.w("MainActivity", "Конвертация не выполнена: не все поля заполнены")
            return
        }

        val result = converter.convert(amount, selectedFromCurrency!!, selectedToCurrency!!)
        binding.textViewResult.text = getString(
            R.string.conversion_result,
            amount,
            selectedFromCurrency!!.CharCode,
            result,
            selectedToCurrency!!.CharCode
        )
        Log.d(
            "MainActivity",
            "Конвертировано $amount ${selectedFromCurrency!!.CharCode} в $result ${selectedToCurrency!!.CharCode}"
        )
    }

    override fun onCurrencySelected(currency: Currency, type: CurrencyType) {
        when (type) {
            CurrencyType.FROM -> {
                selectedFromCurrency = currency
                binding.buttonSelectFromCurrency.text = currency.Name
                Log.d("MainActivity", "Выбрана валюта исходная: ${currency.Name}")
            }
            CurrencyType.TO -> {
                selectedToCurrency = currency
                binding.buttonSelectToCurrency.text = currency.Name
                Log.d("MainActivity", "Выбрана валюта целевая: ${currency.Name}")
            }
        }
    }
}
