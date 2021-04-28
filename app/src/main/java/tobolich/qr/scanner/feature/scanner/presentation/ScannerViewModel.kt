package tobolich.qr.scanner.feature.scanner.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tobolich.qr.scanner.domain.scanner.ProcessScanResultInteractor

class ScannerViewModel(
    private val processScanResultInteractor: ProcessScanResultInteractor
) : ViewModel() {

    private val _liveData = MutableLiveData<String>()
    val liveData: LiveData<String> = _liveData

    fun processScanResult(string: String) {
        processScanResultInteractor.execute(string).let {
            _liveData.value = it.toString()
        }
    }
}

