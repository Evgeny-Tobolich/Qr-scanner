package tobolich.qr.scanner.main.ui

import android.Manifest.permission.CAMERA
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import tobolich.qr.main.databinding.BottomHalfBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val RC_PERMISSON_CAMERA = 101
    }

    private lateinit var codeScanner: CodeScanner
    private lateinit var codeScannerView: CodeScannerView
    private lateinit var binding: BottomHalfBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BottomHalfBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init(isInitial = true)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == RC_PERMISSON_CAMERA) init(isInitial = false)
    }

    override fun onResume() {
        super.onResume()
        if (hasPermissionCamera()) codeScanner.startPreview()
    }

    override fun onPause() {
        if (::codeScanner.isInitialized) codeScanner.releaseResources()
        super.onPause()
    }

    private fun init(isInitial: Boolean) = when {
        hasPermissionCamera() -> initScanner()
        isInitial -> requestPermissionCamera()
        else -> showErrorDialog()
    }

    private fun initScanner() {
        codeScannerView = binding.scannerView
        codeScanner = CodeScanner(this, codeScannerView)

        with(codeScanner) {
            camera = CodeScanner.CAMERA_BACK //CAMERA_BACK or CAMERA_FRONT or specific camera id
            formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
            autoFocusMode = AutoFocusMode.SAFE // SAfE or CONTINUOUS
            scanMode = ScanMode.SINGLE // SINGLE or CONTINUOUS or PREVIEW
            isAutoFocusEnabled = true // Whether to enable auto focus or not
            isFlashEnabled = true // Whether to enable flash or not

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    Toast.makeText(
                        this@MainActivity,
                        "Scan result: ${it.text}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Toast.makeText(
                        this@MainActivity,
                        "Camera error: ${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        with(codeScannerView) {
            setOnClickListener {
                codeScanner.startPreview()
            }
        }
    }

    private fun hasPermissionCamera(): Boolean {
        return ContextCompat.checkSelfPermission(this, CAMERA) == PERMISSION_GRANTED
    }

    private fun requestPermissionCamera() {
        ActivityCompat.requestPermissions(this, arrayOf(CAMERA), RC_PERMISSON_CAMERA)
    }

    private fun showErrorDialog() {
        RequestCameraPermissionDialog.newInstance()
            .show(
                supportFragmentManager,
                RequestCameraPermissionDialog.TAG
            )
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




