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
            val errorDialog = ErrorDialog()
            val args = Bundle()
            args.putString("string", string)
            errorDialog.arguments = args

            return errorDialog
        }

        fun newInstance(throwable: Throwable): ErrorDialog {
            val errorDialog = ErrorDialog()
            val args = Bundle()
            args.putString("throwable", throwable.message)
            errorDialog.arguments = args

            return errorDialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val message = arguments?.getString("string") ?: arguments?.getString("throwable")

        return AlertDialog.Builder(requireContext())
            .apply {
                setTitle(getString(R.string.error_dialog_title))
                setMessage(message)
                setPositiveButton(getString(R.string.got_it)) { _, _ -> }
            }
            .create()
    }
}


