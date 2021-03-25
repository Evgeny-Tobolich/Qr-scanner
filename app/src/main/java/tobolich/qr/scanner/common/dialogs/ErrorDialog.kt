package tobolich.qr.scanner.common.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import tobolich.qr.scanner.R

class ErrorDialog : DialogFragment() {

    companion object {
        val TAG = ErrorDialog::class.java.simpleName

        fun newInstance(string: String): ErrorDialog {
            return ErrorDialog()
        }

        fun newInstance(throwable: Throwable): ErrorDialog {
            return ErrorDialog()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.error_dialog_title))
            .setMessage(getString(R.string.error_dialog_message))
            .setPositiveButton(getString(R.string.undrstndbly)) { _, _ -> }
            .create()
}

