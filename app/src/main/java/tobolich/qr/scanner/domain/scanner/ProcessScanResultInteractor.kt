package tobolich.qr.scanner.domain.scanner

import tobolich.qr.scanner.domain.scanner.model.ScanResult
import tobolich.qr.scanner.domain.scanner.validators.IsPhoneValidator
import tobolich.qr.scanner.domain.scanner.validators.IsUrlValidator

class ProcessScanResultInteractor {

    private lateinit var isPhoneValidator: IsPhoneValidator
    private lateinit var isUrlValidator: IsUrlValidator

    fun execute(input: String): ScanResult = when {
        isPhoneValidator.isValid(input) -> ScanResult.Phone(input)
        isUrlValidator.isValid(input) -> ScanResult.Url(input)
        else -> ScanResult.Text(input)
    }
}

