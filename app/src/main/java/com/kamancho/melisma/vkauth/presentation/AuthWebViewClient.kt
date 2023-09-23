package com.kamancho.melisma.vkauth.presentation

import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * Created by HP on 15.09.2023.
 **/
class AuthWebViewClient(
    private val viewModel: AuthViewModel
): WebViewClient() {


    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)

        viewModel.handleUrl(url.toString())
    }
}