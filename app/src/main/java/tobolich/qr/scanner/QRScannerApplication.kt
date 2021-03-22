package tobolich.qr.scanner

import android.app.Application
import timber.log.Timber

class QRScannerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}