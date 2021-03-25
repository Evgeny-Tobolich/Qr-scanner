package tobolich.qr.scanner.common.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import tobolich.qr.scanner.R

class ErrorDialog : DialogFragment() {

    companion object {
        val TAG = ErrorDialog::class.java.simpleName
        private const val ARG_MESSAGE = "string"

        fun newInstance(string: String): ErrorDialog {
            val errorDialog = ErrorDialog()
            val args = Bundle()
            args.putString(ARG_MESSAGE, string)
            errorDialog.arguments = args

            return errorDialog
        }

        fun newInstance(throwable: Throwable): ErrorDialog {
            return newInstance(throwable.message.toString())
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val message: String by lazy {
            "${arguments?.getString(ARG_MESSAGE)}"
        }
        return AlertDialog.Builder(requireContext())
            .apply {
                setTitle(getString(R.string.error_dialog_title))
                setMessage(message)
                setPositiveButton(getString(android.R.string.ok)) { _, _ -> }
            }
            .create()
    }
}


