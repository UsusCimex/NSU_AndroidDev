package ru.nsu.currencyconverter.fragment

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class RetryDialogFragment : DialogFragment() {

    interface RetryDialogListener {
        fun onRetry()
        fun onExit()
    }

    private var listener: RetryDialogListener? = null

    fun setRetryDialogListener(listener: RetryDialogListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val message = arguments?.getString("message") ?: "Ошибка. Попробовать снова?"

        return AlertDialog.Builder(requireContext())
            .setTitle("Ошибка загрузки")
            .setMessage(message)
            .setPositiveButton("Повторить") { _, _ -> listener?.onRetry() }
            .setNegativeButton("Выход") { _, _ -> listener?.onExit() }
            .create()
    }

    companion object {
        fun newInstance(message: String): RetryDialogFragment {
            val fragment = RetryDialogFragment()
            val args = Bundle()
            args.putString("message", message)
            fragment.arguments = args
            return fragment
        }
    }
}
