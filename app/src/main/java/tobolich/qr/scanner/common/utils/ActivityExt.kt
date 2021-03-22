package tobolich.qr.scanner.common.utils

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Toast
import tobolich.qr.scanner.feature.scanner.ui.ScannerActivity

fun ScannerActivity.openResult(view: View) {
    // TODO добавить ссылку из скана в УРЛ
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("TODO"))
    startActivity(browserIntent)
}

fun ScannerActivity.copyResult(view: View) {
    //TODO добавить текст для копирования
    val copyToast = Toast.makeText(
        this, "TODO",
        Toast.LENGTH_SHORT
    ).show()
}

fun ScannerActivity.shareResult(view: View) {
    //TODO добавить text-ссылку из скана в putExtra - вэлью
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "TODO")
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}




