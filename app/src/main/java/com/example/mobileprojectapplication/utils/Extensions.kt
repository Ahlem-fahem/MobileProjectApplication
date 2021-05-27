package com.example.mobileprojectapplication.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.gms.common.util.concurrent.HandlerExecutor
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.Executor
import com.example.mobileprojectapplication.R
import com.example.mobileprojectapplication.models.NetworkErrorType
import com.example.mobileprojectapplication.ui.splashscreen.SplashScreenActivity
import kotlinx.android.synthetic.main.loading_view.view.*
import kotlinx.android.synthetic.main.network_error.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

fun Context.mainExecutor(): Executor {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        mainExecutor
    } else {
        HandlerExecutor(mainLooper)
    }
}
fun Context?.isOnline(
    successBlock: () -> Unit,
    failBlock: () -> Unit = { globalInternetFailBock() }
) {
    this?.apply {
        if (isInternetAvailable(this)) {
            successBlock()
        } else {
            failBlock()
        }
    } ?: failBlock()
}

@Suppress("DEPRECATION")
private fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    } else {
        return connectivityManager.activeNetworkInfo?.isConnected ?: false
    }
}
fun globalInternetFailBock() {
}
fun View.displayMessage(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).apply {
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        val snackBarTextView =
            this.view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        snackBarTextView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        show()
    }
}
fun String.isEmailValid(): Boolean {
    return this.contains("@")
}
@ExperimentalCoroutinesApi
fun Context.reloadApplication() {
    val intent = Intent(this, SplashScreenActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION)
    startActivity(intent)
    try {
        (this as Activity).finish()
    } catch (e: Exception) {
        Log.d("navigation", "error context ${e.message}")
    }
}
@ExperimentalCoroutinesApi
fun View.showErrorView(
    errorType: NetworkErrorType,
    retryAction: () -> Unit? = { globalInternetFailBock() }
) {
    visibility = View.VISIBLE
    appLoadingView?.visibility = View.GONE
    networkErrorView?.visibility = View.VISIBLE

    when (errorType) {
        NetworkErrorType.SERVER -> {
            networkErrorImageView.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_server_error))

            networkErrorText.text = context.getString(R.string.internal_server_error_text)
            networkRetryButton.apply {
                text = context.getString(R.string.return_to_home_page)
                setOnClickListener {
                    context?.reloadApplication()
                }
            }
        }
        NetworkErrorType.NETWORK -> {
            networkErrorImageView.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_noun_no_wifi))
            networkErrorText.text = context?.getString(R.string.no_network_empty_state_subtitle)
            networkRetryButton.apply {
                text = context.getString(R.string.reload_page)
                setOnClickListener {
                    showLoadingView()
                    retryAction()
                }
            }
        }
    }
}

@ExperimentalCoroutinesApi
fun View.showLoadingView() {
    visibility = View.VISIBLE
    appLoadingView?.visibility = View.VISIBLE
    networkErrorView?.visibility = View.GONE

}

@ExperimentalCoroutinesApi
fun View.hideLoadingView() {
    visibility = View.GONE
}