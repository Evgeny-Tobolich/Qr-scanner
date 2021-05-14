package tobolich.qr.scanner.feature.scanner.ui

import android.Manifest.permission.CAMERA
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.budiyev.android.codescanner.*
import tobolich.qr.scanner.R
import tobolich.qr.scanner.common.dialogs.ErrorDialog
import tobolich.qr.scanner.common.dialogs.RequestCameraPermissionDialog
import tobolich.qr.scanner.common.utils.callPhoneNumber
import tobolich.qr.scanner.common.utils.copy
import tobolich.qr.scanner.common.utils.openResult
import tobolich.qr.scanner.common.utils.share
import tobolich.qr.scanner.databinding.ScannerActivityBinding
import tobolich.qr.scanner.domain.scanner.model.ScanResult.*
import tobolich.qr.scanner.feature.scanner.presentation.ScannerState
import tobolich.qr.scanner.feature.scanner.presentation.ScannerState.Idle
import tobolich.qr.scanner.feature.scanner.presentation.ScannerState.Scanned
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

        viewModel.state.observe(this, ::renderState)

        viewModel.error.observe(this, ::showErrorDialog)

        initNewScanListener()
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
        if (hasPermissionCamera() && viewModel.state.value is Idle?) {
            codeScanner.startPreview()
        }
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
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback { scan ->
                runOnUiThread { viewModel.processScan(scan.text) }
            }

            errorCallback = ErrorCallback { error ->
                runOnUiThread { viewModel.showErrorDialog(error) }
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

    private fun showErrorDialog(error: Throwable) {
        ErrorDialog.newInstance(error)
            .show(supportFragmentManager, ErrorDialog.TAG)
    }

    private fun renderState(state: ScannerState) = when (state) {
        is Idle -> renderIdleState()
        is Scanned -> renderScannedState(state)
    }

    private fun renderIdleState() {
        renderScanResultTextWithHint()
        renderScanResultButtons(isVisible = false)
    }

    private fun renderScannedState(scanned: Scanned) = when (scanned.scanResult) {
        is Url,
        is Text -> renderUrl(scanned.scanResult.string)
        is Phone -> renderPhone(scanned.scanResult.string)
    }

    private fun renderScanResultTextWithHint() {
        renderScanResultText(getString(R.string.scanner_hint))
        binding.scanResultText.textSize = 24F
    }

    private fun renderScanResultButtons(isVisible: Boolean) {
        binding.copyButton.isVisible = isVisible
        binding.shareButton.isVisible = isVisible
        binding.openResultButton.isVisible = isVisible
        binding.doneText.isVisible = isVisible
        binding.newScanButton.isVisible = isVisible
    }

    private fun renderUrl(string: String) {
        renderScanResultButtons(isVisible = true)
        renderScanResultText(string)
        initClickListeners(string)
        initUrlListener(string)
    }

    private fun renderPhone(string: String) {
        renderScanResultButtons(isVisible = true)
        renderScanResultText(string)
        initClickListeners(string)
        initPhoneListener(string)
    }

    private fun renderScanResultText(string: String) {
        binding.scanResultText.text = string
        binding.scanResultText.textSize = 16F
    }

    private fun initClickListeners(string: String) {

        binding.copyButton.setOnClickListener {
            copy(string)
        }

        binding.shareButton.setOnClickListener {
            share(string)
        }
    }

    private fun initUrlListener(string: String) {
        binding.openResultButton.setOnClickListener {
            openResult(string)
        }

        binding.openResultButton.text = getString(R.string.open_in_browser)
    }

    private fun initPhoneListener(string: String) {
        binding.openResultButton.setOnClickListener {
            callPhoneNumber(string)
        }

        binding.openResultButton.text = getString(R.string.call)
    }

    private fun initNewScanListener() {
        binding.newScanButton.setOnClickListener {
            viewModel.resetScanResult()
            codeScanner.startPreview()
            renderScanResultTextWithHint()
        }
    }
}

