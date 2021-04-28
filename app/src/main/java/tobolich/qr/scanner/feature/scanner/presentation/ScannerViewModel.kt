package tobolich.qr.scanner.feature.scanner.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.budiyev.android.codescanner.DecodeCallback
import tobolich.qr.scanner.domain.scanner.ProcessScanResultInteractor

class ScannerViewModel(
    private val processScanResultInteractor: ProcessScanResultInteractor,
    private val decodeCallback: DecodeCallback
) : ViewModel() {

    private val resultLiveData = MutableLiveData<DecodeCallback>()

    fun processScanResult(string: String) {
        processScanResultInteractor.execute(string)
        resultLiveData.value = decodeCallback
    }
}

