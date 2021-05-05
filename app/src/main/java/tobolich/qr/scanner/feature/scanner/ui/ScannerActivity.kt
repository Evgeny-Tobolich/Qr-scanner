package tobolich.qr.scanner.feature.scanner.ui

import android.Manifest.permission.CAMERA
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
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
        Log.i("Scanner", "onCreate()")
        binding = ScannerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init(isInitial = true)

        viewModel.scanResultLiveData.observe(this) { scan ->

            renderScanResult(scan)

            resetScanScreen()

            initClickListeners(scan)
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

    override fun onStart() {
        super.onStart()
        Log.i("Scanner", "onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.i("Scanner", "onResume()")
        if (hasPermissionCamera() && viewModel.scanResultLiveData.value == null) codeScanner.startPreview()
    }

    override fun onPause() {
        if (::codeScanner.isInitialized) codeScanner.releaseResources()
        super.onPause()
        Log.i("Scanner", "onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.i("Scanner", "onStop()")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("Scanner", "onRestart()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("Scanner", "onDestroy()")
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

    private fun renderScanResult(scanResult: ScanResult?) = when (scanResult) {
        is Url,
        is Phone,
        is Text -> {
            renderScanResultText(scanResult.string)
            renderScanResultButtons(isVisible = true)
            renderNewScanButton(isVisible = true)
        }
        null -> {
            renderScanResultTextWithHint()
            renderScanResultButtons(isVisible = false)
            renderNewScanButton(isVisible = false)
        }
    }

    private fun renderScanResultTextWithHint() {
        renderScanResultText(getString(R.string.scanner_hint))
    }

    private fun renderScanResultText(string: String) {
        binding.scanResultText.text = string
    }

    private fun renderScanResultButtons(isVisible: Boolean) {
        binding.copyButton.isVisible = isVisible
        binding.shareButton.isVisible = isVisible
        binding.openInBrowserButton.isVisible = isVisible
        binding.doneText.isVisible = isVisible
    }

    private fun renderNewScanButton(isVisible: Boolean) {
        binding.newScan.isVisible = isVisible
    }

    //TODO: заменить на ScannerState
    private fun resetScanScreen() {
        binding.newScan.setOnClickListener {
            viewModel.resetScanResult()
            codeScanner.startPreview()
            renderScanResultTextWithHint()
        }
    }

    //TODO: добавить кнопку для номера телефона
    private fun initClickListeners(scanResult: ScanResult?) {
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
}

