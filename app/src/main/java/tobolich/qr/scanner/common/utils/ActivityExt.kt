package tobolich.qr.scanner.common.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import tobolich.qr.scanner.R
import tobolich.qr.scanner.feature.scanner.ui.ScannerActivity

lateinit var clipData: ClipData
lateinit var clipboardManager: ClipboardManager

//TODO: добавить текст для копирования
fun ScannerActivity.copy(scanResultText: String) {
    clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipData = ClipData.newPlainText("Copy", scanResultText)
    clipboardManager.setPrimaryClip(clipData)

    Toast.makeText(this, getString(R.string.copy_toast_text), Toast.LENGTH_SHORT).show()
}

//TODO: реализовать функционал
fun ScannerActivity.share() {
    Toast.makeText(this, "Поделились", Toast.LENGTH_SHORT).show()
}

// TODO: реализовать функционал
fun ScannerActivity.openInBrowser() {
    Toast.makeText(this, "Поделились", Toast.LENGTH_SHORT).show()
}

