package tobolich.qr.scanner.feature.scanner.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tobolich.qr.scanner.domain.scanner.ProcessScanResultInteractor
import tobolich.qr.scanner.domain.scanner.validators.IsPhoneValidator
import tobolich.qr.scanner.domain.scanner.validators.IsUrlValidator

class ScannerViewModel : ViewModel() {

    private val stateProducer = MutableLiveData<ScannerState>()
        .apply { ScannerState.Idle }
    val state: LiveData<ScannerState> = stateProducer

    private val errorProducer = MutableLiveData<Throwable>() //TODO: заменить на EventLiveData
    val error: LiveData<Throwable> = errorProducer

    private val processScanResultInteractor: ProcessScanResultInteractor
        get() = ProcessScanResultInteractor(isPhoneValidator, isUrlValidator)

    private val isPhoneValidator: IsPhoneValidator
        get() = IsPhoneValidator()

    private val isUrlValidator: IsUrlValidator
        get() = IsUrlValidator()

    fun processScan(string: String) {
        stateProducer.value =
            ScannerState.Scanned(processScanResultInteractor.execute(string))
    }

    fun resetScanResult() {
        stateProducer.value = ScannerState.Idle
    }

    fun showErrorDialog(error: Throwable) {
        errorProducer.value = error
    }
}
