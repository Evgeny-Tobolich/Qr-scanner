package tobolich.qr.scanner.domain.scanner

import tobolich.qr.scanner.domain.scanner.model.ScanResult
import tobolich.qr.scanner.domain.scanner.validators.IsPhoneValidator
import tobolich.qr.scanner.domain.scanner.validators.IsUrlValidator

abstract class Interactor {

    protected val isPhoneValidator = IsPhoneValidator()
    protected val isUrlValidator = IsUrlValidator()

    abstract fun execute(string: String): ScanResult
}
