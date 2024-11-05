package ru.nsu.currencyconverter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import ru.nsu.currencyconverter.databinding.ActivityMainBinding
import ru.nsu.currencyconverter.model.Currency
import ru.nsu.currencyconverter.ui.ConvertFromRubFragment
import ru.nsu.currencyconverter.ui.CurrencySelectionListener
import ru.nsu.currencyconverter.ui.ViewPagerAdapter

class MainActivity : AppCompatActivity(), CurrencySelectionListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var convertFromRubFragment: ConvertFromRubFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        convertFromRubFragment = ConvertFromRubFragment()

        viewPagerAdapter = ViewPagerAdapter(this, convertFromRubFragment)
        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.offscreenPageLimit = viewPagerAdapter.itemCount

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = viewPagerAdapter.getFragmentName(position)
        }.attach()
    }

    override fun onCurrencySelected(currency: Currency) {
        // Передаем выбранную валюту во фрагмент конвертации
        convertFromRubFragment.setSelectedCurrency(currency)
        // Переключаемся на вкладку "Конвертация"
        binding.viewPager.currentItem = 1
    }
}
