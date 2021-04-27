package tobolich.qr.scanner.feature.scanner.ui

import android.Manifest.permission.CAMERA
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.budiyev.android.codescanner.*
import tobolich.qr.scanner.common.dialogs.RequestCameraPermissionDialog
import tobolich.qr.scanner.databinding.ScannerActivityBinding
import tobolich.qr.scanner.feature.scanner.presentation.ScannerViewModel

class ScannerActivity : AppCompatActivity() {

    companion object {
        private const val RC_PERMISSION_CAMERA = 101
    }

    private lateinit var codeScanner: CodeScanner
    private lateinit var codeScannerView: CodeScannerView
    private lateinit var binding: ScannerActivityBinding
    private lateinit var viewModel: ScannerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ScannerActivityBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(ScannerViewModel::class.java)
        setContentView(binding.root)
        init(isInitial = true)

        viewModel.liveData.observe(this, Observer {
            codeScanner.decodeCallback = it
        })

        viewModel.scanResult()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
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

            decodeCallback = DecodeCallback { //TODO: передавать результат на вьюмодел
                runOnUiThread {
                    Toast.makeText(
                        this@ScannerActivity,
                        "Scan result: ${it.text}",
                        Toast.LENGTH_LONG
                    ).show()
                }
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

    //TODO: переименовать  к RequestCameraPermissionDialog
    private fun showErrorDialog() {
        RequestCameraPermissionDialog.newInstance()
            .show(supportFragmentManager, RequestCameraPermissionDialog.TAG)
    }
}




