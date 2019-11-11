package io.seroo.sampleplayer

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object PermissionUtils {
    const val REQUEST_PERMISSION: Int = 0x100
    const val REQUEST_SETTINGS: Int = 0x101
    private const val res = Manifest.permission.READ_EXTERNAL_STORAGE

    fun requestPermission(
        fragment: Fragment,
        onGrantedAction: () -> Unit
    ) {
        fragment.activity?.let {
            when {
                checkPermission(it) -> onGrantedAction.invoke()
                ActivityCompat.shouldShowRequestPermissionRationale(it, res) -> {
                    showRequestPermissionRationaleDialog(fragment)
                }
                else -> ActivityCompat.requestPermissions(it, arrayOf(res), REQUEST_PERMISSION)
            }
        }
    }

    fun onRequestPermissionResult(
        grantResults: IntArray,
        onGrantedAction: () -> Unit,
        onDeniedPermission: () -> Unit
    ) {
        when (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            true -> onGrantedAction.invoke()
            false -> onDeniedPermission.invoke()
        }
    }

    private fun checkPermission(context: Context) = ContextCompat.checkSelfPermission(
        context,
        res
    ) == PackageManager.PERMISSION_GRANTED

    private fun showRequestPermissionRationaleDialog(fragment: Fragment) {
        fragment.context?.let {
            AlertDialog.Builder(it).apply {
                setTitle(R.string.permission_check_dialog_title)
                    .setMessage(context.getString(R.string.permission_check_dialog_message))
                    .setPositiveButton(R.string.settings) { _, _ ->
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:${context.packageName}")
                        ).also {
                            SampleIntent.startActivityForResult(fragment, it, REQUEST_SETTINGS)
                        }
                    }
                setCancelable(false)
            }.show()
        }
    }
}