package tobolich.qr.scanner.common.utils

import android.widget.Toast
import tobolich.qr.scanner.R
import tobolich.qr.scanner.feature.scanner.ui.ScannerActivity

//TODO добавить текст для копирования
fun ScannerActivity.copy(scanResultText: String) {
    Toast.makeText(
        this,
        getString(R.string.copy_toast_text),
        Toast.LENGTH_SHORT
    ).show()
}

//TODO Refactor
fun ScannerActivity.share() {
    Toast.makeText(this, "Поделились", Toast.LENGTH_SHORT).show()
}

// TODO Refactor
fun ScannerActivity.openInBrowser() {
    Toast.makeText(this, "Поделились", Toast.LENGTH_SHORT).show()
}

