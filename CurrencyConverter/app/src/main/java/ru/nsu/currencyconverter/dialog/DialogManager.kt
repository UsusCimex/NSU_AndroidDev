package ru.nsu.currencyconverter.dialog

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import ru.nsu.currencyconverter.fragment.LoadingDialogFragment
import ru.nsu.currencyconverter.fragment.RetryDialogFragment

class DialogManager(private val activity: AppCompatActivity) {
    private var loadingDialog: LoadingDialogFragment? = null
    private val fragmentManager: FragmentManager = activity.supportFragmentManager

    fun showLoadingDialog(message: String = "Загрузка...") {
        if (loadingDialog?.isVisible == true) return
        loadingDialog = LoadingDialogFragment.newInstance(message)
        loadingDialog?.show(fragmentManager, LOADING_DIALOG_TAG)
    }

    fun hideLoadingDialog() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    fun showRetryDialog(
        errorMessage: String,
        onRetry: () -> Unit,
        onExit: () -> Unit
    ) {
        val retryDialog = RetryDialogFragment.newInstance("$errorMessage. Попробовать снова?")
        retryDialog.setRetryDialogListener(object : RetryDialogFragment.RetryDialogListener {
            override fun onRetry() {
                onRetry()
            }
            override fun onExit() {
                onExit()
            }
        })
        retryDialog.show(fragmentManager, RETRY_DIALOG_TAG)
    }

    companion object {
        private const val LOADING_DIALOG_TAG = "LoadingDialog"
        private const val RETRY_DIALOG_TAG = "RetryDialog"
    }
}
