package tobolich.qr.scanner.Main.Package.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import tobolich.qr.scanner.databinding.BottomHalfBinding

class ButtonsRealisation : AppCompatActivity() {

    private lateinit var binding: BottomHalfBinding
    private lateinit var scanResult: String
    private lateinit var clipData: ClipData
    private lateinit var clipBoardManager: ClipboardManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BottomHalfBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun copyBtnClick(view: View) {
        //TODO добавить текст для копирования
        val copyToast = Toast.makeText(
            this, "Сopied!",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun shareBntClick(view: View) {
        // получение значения текстового поля result_text
        val scanTextResult = binding.resultText.text.toString()

        //TODO добавить ссылку из скана в putExtra - вэлью
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    fun openInBrws(view: View) {
        // TODO добавить ссылку из скана в УРЛ
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"))
        startActivity(browserIntent)
    }

    fun textViewResult(view: View) {


    }
}
