package tobolich.qr.scanner.common.utils

import android.annotation.SuppressLint
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

    Toast.makeText(this, getString(R.string.copied), Toast.LENGTH_SHORT).show()
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
    } else {
        openInBrowserAsQueryInGoogle(string)
    }

@SuppressLint("QueryPermissionsNeeded")
fun Activity.openInBrowserAsURL(string: String) {
    val url = if (!string.startsWith("http://") && !string.startsWith("https://")) {
        "https://$string"
    } else {
        string
    }

    val intent = Intent(Intent.ACTION_VIEW)
        .apply { data = Uri.parse(url) }

    if (intent.resolveActivity(packageManager) != null) startActivity(intent)
}

fun Activity.openInBrowserAsQueryInGoogle(string: String) {
    val intent = Intent(Intent.ACTION_VIEW)
        .apply { data = Uri.parse("https://www.google.com/search?q=$string") }
    startActivity(intent)
}

@SuppressLint("QueryPermissionsNeeded")
fun Activity.callPhoneNumber(string: String) {
    val intent = Intent(Intent.ACTION_DIAL)
        .apply { data = Uri.parse("tel:$string") }

    if (intent.resolveActivity(packageManager) != null) startActivity(intent)
}

