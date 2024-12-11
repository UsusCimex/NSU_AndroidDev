package ru.nsu.currencyconverter

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ru.nsu.currencyconverter.client.CentralBankClient
import ru.nsu.currencyconverter.databinding.ActivityMainBinding
import ru.nsu.currencyconverter.dialog.DialogManager
import ru.nsu.currencyconverter.fragment.CurrencySelectionFragment
import ru.nsu.currencyconverter.listener.CurrencySelectionListener
import ru.nsu.currencyconverter.logic.MainLogic
import ru.nsu.currencyconverter.model.Currency
import ru.nsu.currencyconverter.model.CurrencyType
import ru.nsu.currencyconverter.utils.CurrencyConverter

class MainActivity : AppCompatActivity(), CurrencySelectionListener, MainLogic.MainLogicCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var logic: MainLogic
    private lateinit var dialogManager: DialogManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialogManager = DialogManager(this)
        logic = MainLogic(
            context = this,
            lifecycleScope = lifecycleScope,
            client = CentralBankClient(),
            converter = CurrencyConverter(),
            callback = this
        )

        logic.loadCurrencies()
        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        binding.buttonSelectFromCurrency.setOnClickListener {
            if (logic.shouldShowCurrencySelection()) {
                openCurrencySelectionFragment(CurrencyType.FROM)
            } else {
                logic.retryLoadCurrencies()
            }
        }

        binding.buttonSelectToCurrency.setOnClickListener {
            if (logic.shouldShowCurrencySelection()) {
                openCurrencySelectionFragment(CurrencyType.TO)
            } else {
                logic.retryLoadCurrencies()
            }
        }

        binding.convertButton.setOnClickListener {
            val amount = binding.editTextAmount.text.toString().toDoubleOrNull()
            logic.convertCurrency(amount)
        }
    }

    private fun openCurrencySelectionFragment(type: CurrencyType) {
        val fragmentTag = "CurrencySelectionFragment"

        supportFragmentManager.popBackStack(
            null,
            androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
        )

        val fragment = CurrencySelectionFragment.newInstance(type, logic.getCurrencyList())
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, fragmentTag)
            .addToBackStack(null)
            .commit()
        Log.d("MainActivity", "Открыт CurrencySelectionFragment для $type")
    }

    override fun onCurrencySelected(currency: Currency, type: CurrencyType) {
        logic.handleCurrencySelection(currency, type)
    }

    override fun onCurrenciesLoaded() {
        binding.buttonSelectFromCurrency.isEnabled = true
        binding.buttonSelectToCurrency.isEnabled = true
    }

    override fun onCurrenciesFailed(errorMessage: String) {
        dialogManager.showRetryDialog(
            errorMessage = errorMessage,
            onRetry = { logic.retryLoadCurrencies() },
            onExit = { finish() }
        )
    }

    override fun onConversionResult(result: String) {
        binding.textViewResult.text = result
    }

    override fun updateFromCurrencyText(name: String) {
        binding.buttonSelectFromCurrency.text = name
    }

    override fun updateToCurrencyText(name: String) {
        binding.buttonSelectToCurrency.text = name
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun finishActivity() {
        finish()
    }
}
