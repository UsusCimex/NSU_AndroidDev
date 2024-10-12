package ru.nsu.currencyconverter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.nsu.currencyconverter.ui.CurrencyListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Загрузка CurrencyListFragment при старте
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CurrencyListFragment())
                .commit()
        }
    }
}