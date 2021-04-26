package tobolich.qr.scanner.domain.scanner

import tobolich.qr.scanner.domain.scanner.model.ScanResult

class ProcessScanResultInteractor : Interactor() {

    override fun execute(string: String): ScanResult = when {
        isPhoneValidator.isValid(string) -> ScanResult.Phone(string)
        isUrlValidator.isValid(string) -> ScanResult.Url(string)
        else -> ScanResult.Text(string)
    }
}

