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
import tobolich.qr.scanner.common.dialogs.RequestCameraPermissionDialog
import tobolich.qr.scanner.common.utils.copy
import tobolich.qr.scanner.common.utils.openInBrowser
import tobolich.qr.scanner.common.utils.share
import tobolich.qr.scanner.databinding.ScannerActivityBinding
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

            renderScanResult(scan.string)

            binding.copyButton.setOnClickListener {
                copy(scan.string)
            }

            binding.openInBrowserButton.setOnClickListener {
                openInBrowser(scan.string)
            }

            binding.shareButton.setOnClickListener {
                share(scan.string)
            }
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
            isAutoFocusEnabled = true // Whether to enable auto focus or not
            isFlashEnabled = true // Whether to enable flash or not

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
        ActivityCompat.requestPermissions(this, arrayOf(CAMERA), RC_PERMISSION_CAMERA)
    }

    private fun showRequestCameraPermissionDialog() {
        RequestCameraPermissionDialog.newInstance()
            .show(supportFragmentManager, RequestCameraPermissionDialog.TAG)
    }

    private fun renderScanResult(string: String) {

        binding.scanResultText.text = string

        if (binding.scanResultText.text != "") {
            binding.openInBrowserButton.visibility = View.VISIBLE
            binding.copyButton.visibility = View.VISIBLE
            binding.shareButton.visibility = View.VISIBLE
            binding.doneText.visibility = View.VISIBLE
        }
    }
}

