package ru.nsu.currencyconverter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import ru.nsu.currencyconverter.R

class LoadingDialogFragment : DialogFragment() {

    companion object {
        private const val ARG_MESSAGE = "message"

        fun newInstance(message: String = "Загрузка..."): LoadingDialogFragment {
            val fragment = LoadingDialogFragment()
            val args = Bundle()
            args.putString(ARG_MESSAGE, message)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_loading, container, false)
        val loadingMessage = view.findViewById<TextView>(R.id.loadingMessage)
        loadingMessage.text = arguments?.getString(ARG_MESSAGE)
        isCancelable = false
        return view
    }
}
