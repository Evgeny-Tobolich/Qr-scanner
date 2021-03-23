package tobolich.qr.scanner.common.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import tobolich.qr.scanner.R
import tobolich.qr.scanner.feature.scanner.ui.ScannerActivity

fun Activity.copy(string: String) {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText("Copy", string)
    clipboardManager.setPrimaryClip(clipData)

    Toast.makeText(this, getString(R.string.copy_toast_text), Toast.LENGTH_SHORT).show()
}

fun Activity.share(string: String) {

}

// TODO: реализовать функционал
fun ScannerActivity.openInBrowser() {
    Toast.makeText(this, "Поделились", Toast.LENGTH_SHORT).show()
}

