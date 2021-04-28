package tobolich.qr.scanner.feature.scanner.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tobolich.qr.scanner.domain.scanner.ProcessScanResultInteractor
import tobolich.qr.scanner.domain.scanner.model.ScanResult
import tobolich.qr.scanner.domain.scanner.validators.IsPhoneValidator
import tobolich.qr.scanner.domain.scanner.validators.IsUrlValidator

class ScannerViewModel : ViewModel() {

    private val mutableLiveData = MutableLiveData<ScanResult>()
    val liveData: LiveData<ScanResult> = mutableLiveData

    private val processScanResultInteractor: ProcessScanResultInteractor
        get() = ProcessScanResultInteractor(isPhoneValidator, isUrlValidator)

    private val isPhoneValidator: IsPhoneValidator
        get() = IsPhoneValidator()

    private val isUrlValidator: IsUrlValidator
        get() = IsUrlValidator()

    fun processScan(string: String) {
        mutableLiveData.value = processScanResultInteractor.execute(string)
    }
}

