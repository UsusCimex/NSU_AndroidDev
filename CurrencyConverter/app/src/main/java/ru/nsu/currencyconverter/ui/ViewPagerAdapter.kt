package ru.nsu.currencyconverter.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(activity: FragmentActivity, private val convertFromRubFragment: ConvertFromRubFragment) : FragmentStateAdapter(activity) {

    private val fragments = listOf(
        CurrencyListFragment(),    // Список валют
        convertFromRubFragment     // Конвертация рублей в выбранную валюту
    )

    private val fragmentNames = listOf(
        "Курсы валют",
        "Конвертация"
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    fun getFragmentName(position: Int): String = fragmentNames[position]
}
