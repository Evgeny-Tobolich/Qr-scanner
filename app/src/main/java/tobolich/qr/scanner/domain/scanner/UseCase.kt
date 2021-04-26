package tobolich.qr.scanner.domain.scanner

interface UseCase<in ScannerViewModel, out ScanResult> {

    fun execute(input: ScannerViewModel): ScanResult
}
