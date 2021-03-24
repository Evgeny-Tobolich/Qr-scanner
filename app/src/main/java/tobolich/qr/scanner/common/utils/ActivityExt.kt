package tobolich.qr.scanner.common.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import tobolich.qr.scanner.R

fun Activity.copy(string: String) {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText("Copy", string)
    clipboardManager.setPrimaryClip(clipData)

    Toast.makeText(this, getString(R.string.copy_toast_text), Toast.LENGTH_SHORT).show()
}

fun Activity.share(string: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, string)
        type = "text/plain"
    }.let {
        Intent.createChooser(it, null)
    }
    startActivity(shareIntent)
}

fun Activity.openInBrowser(string: String) =
    if (Patterns.WEB_URL.matcher(string).matches()) {
        openInBrowserAsURL(string)
    } else openInBrowserAsQueryInGoogle(string)

fun Activity.openInBrowserAsURL(string: String) {
    val url = if (
        !string.startsWith("http://") &&
        !string.startsWith("https://") &&
        !string.startsWith("pop3://") &&
        !string.startsWith("ftp://") &&
        !string.startsWith("smtp://")
    ) {
        "http://$string"
    } else string

    val intent = Intent(Intent.ACTION_VIEW)
        .apply { data = Uri.parse(url) }
    startActivity(intent)
}

fun Activity.openInBrowserAsQueryInGoogle(string: String) {
    val intent = Intent(Intent.ACTION_VIEW)
        .apply { data = Uri.parse("https://www.google.com/search?q=$string") }
    startActivity(intent)
}

