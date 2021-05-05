package tobolich.qr.scanner.feature.scanner.ui

import android.Manifest.permission.CAMERA
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import tobolich.qr.scanner.R
import tobolich.qr.scanner.common.dialogs.RequestCameraPermissionDialog
import tobolich.qr.scanner.common.utils.*
import tobolich.qr.scanner.databinding.ScannerActivityBinding
import tobolich.qr.scanner.domain.scanner.model.ScanResult
import tobolich.qr.scanner.domain.scanner.model.ScanResult.*
import tobolich.qr.scanner.feature.scanner.presentation.ScannerViewModel

class ScannerActivity : AppCompatActivity() {

    companion object {
        private const val RC_PERMISSION_CAMERA = 101
    }

    private lateinit var codeScanner: CodeScanner
    private lateinit var codeScannerView: CodeScannerView
    private lateinit var binding: ScannerActivityBinding
    private val viewModel: ScannerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ScannerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init(isInitial = true)

        viewModel.scanResultLiveData.observe(this) { scan ->

            renderScanResult(scan)

            resetScanScreen()

            bindButtons(scan)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RC_PERMISSION_CAMERA) init(isInitial = false)
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
        else -> showRequestCameraPermissionDialog()
    }

    private fun initScanner() {
        codeScannerView = binding.scannerView
        codeScanner = CodeScanner(this, codeScannerView)

        with(codeScanner) {
            camera = CodeScanner.CAMERA_BACK //CAMERA_BACK or CAMERA_FRONT or specific camera id
            formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
            autoFocusMode = AutoFocusMode.SAFE // SAfE or CONTINUOUS
            scanMode = ScanMode.SINGLE // SINGLE or CONTINUOUS or PREVIEW
            isAutoFocusEnabled = true
            isFlashEnabled = true

            decodeCallback = DecodeCallback { scan ->
                runOnUiThread { viewModel.processScan(scan.text) }
            }

            errorCallback = ErrorCallback { //TODO: передавать результат на вьюмодел
                runOnUiThread {
                    Toast.makeText(
                        this@ScannerActivity,
                        "Camera error: ${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun hasPermissionCamera(): Boolean {
        return ContextCompat.checkSelfPermission(this, CAMERA) == PERMISSION_GRANTED
    }

    private fun requestPermissionCamera() {
        ActivityCompat.requestPermissions(this, arrayOf(CAMERA), RC_PERMISSION_CAMERA)
    }

    private fun showRequestCameraPermissionDialog() {
        RequestCameraPermissionDialog.newInstance()
            .show(supportFragmentManager, RequestCameraPermissionDialog.TAG)
    }

    private fun renderScanResult(scanResult: ScanResult?) {

        binding.scanResultText.text = scanResult?.string

        isGoneButtons()

        if (scanResult != null) {
            openDefultButtons(scanResult).also {
                when (scanResult) {
                    is Url -> binding.openInBrowserButton.visibility = View.VISIBLE
                    is Phone -> binding.openInBrowserButton.visibility = View.VISIBLE
                    else -> binding.openInBrowserButton.visibility = View.VISIBLE
                }
            }
        }
    }

    //TODO: заменить на ScannerState
    private fun resetScanScreen() {
        binding.newScan.setOnClickListener {
            viewModel.resetScanResult()
            codeScanner.startPreview()
            isGoneButtons()
            binding.scanResultText.text = getString(R.string.scanner_hint)
        }
    }

    //TODO: добавить кнопку для номера телефона
    private fun bindButtons(scanResult: ScanResult?) {
        if (scanResult != null) {
            binding.copyButton.setOnClickListener {
                copy(scanResult.string)
            }

            binding.openInBrowserButton.setOnClickListener {
                openInBrowser(scanResult.string)
            }

            binding.shareButton.setOnClickListener {
                share(scanResult.string)
            }
        }
    }

    private fun openDefultButtons(scanResult: ScanResult?) = when (scanResult) {
        is Url -> isVisibleButtons()
        is Phone -> isVisibleButtons()
        else -> isVisibleButtons()
    }

    private fun isGoneButtons() {
        binding.doneText.visibility = View.GONE
        binding.newScan.visibility = View.GONE
        binding.copyButton.visibility = View.GONE
        binding.shareButton.visibility = View.GONE
        binding.openInBrowserButton.visibility = View.GONE
    }

    private fun isVisibleButtons() {
        binding.doneText.visibility = View.VISIBLE
        binding.newScan.visibility = View.VISIBLE
        binding.copyButton.visibility = View.VISIBLE
        binding.shareButton.visibility = View.VISIBLE
    }
}

