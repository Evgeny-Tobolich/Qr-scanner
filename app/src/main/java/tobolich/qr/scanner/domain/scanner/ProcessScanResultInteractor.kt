package tobolich.qr.scanner.domain.scanner

import tobolich.qr.scanner.domain.scanner.model.ScanResult
import tobolich.qr.scanner.feature.scanner.presentation.ScannerViewModel

class ProcessScanResultInteractor : UseCase<ScannerViewModel, ScanResult> {

    override fun execute(input: ScannerViewModel): ScanResult {
       //TODO()

    }
}
