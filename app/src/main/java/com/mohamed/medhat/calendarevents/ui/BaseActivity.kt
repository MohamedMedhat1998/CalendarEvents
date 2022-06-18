package com.mohamed.medhat.calendarevents.ui

import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mohamed.medhat.calendarevents.R

private const val REQUIRED_PERMISSIONS_REQUEST_CODE = 99

/**
 * Contains helper functions that are frequently used by all the activities.
 */
open class BaseActivity : AppCompatActivity() {

    private var onPermissionGranted: () -> Unit = {}
    private val dialogsList = mutableListOf<AlertDialog>()

    /**
     * A helper function that requests the passed permissions list if not granted
     * then executes the passed lambda if the permissions were granted.
     * @param requiredPermissions The list of permissions to request.
     * @param permissionNames A list of permissions friendly names that will be displayed for the user.
     * @param onPermissionGranted The lambda to be executed in case the permissions were granted.
     */
    fun requirePermissions(
        requiredPermissions: Array<String>,
        permissionNames: Array<String>,
        onPermissionGranted: () -> Unit
    ) {
        this.onPermissionGranted = onPermissionGranted

        val namesBuilder = StringBuilder()
        permissionNames.forEachIndexed { index, name ->
            namesBuilder.append(name)
            if (index != permissionNames.lastIndex) {
                namesBuilder.appendLine()
            }
        }

        when {
            requiredPermissions.all {
                ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
            } -> {
                this.onPermissionGranted.invoke()
            }
            requiredPermissions.any {
                ActivityCompat.shouldShowRequestPermissionRationale(this, it)
            } -> {
                val permissionsReasonDialog = AlertDialog.Builder(this)
                    .setTitle(getString(R.string.required_permissions_title))
                    .setMessage(
                        getString(
                            R.string.required_permissions_message,
                            namesBuilder.toString()
                        )
                    )
                    .setPositiveButton(R.string.required_permissions_ok) { dialog, _ ->
                        requestPermissions(requiredPermissions, REQUIRED_PERMISSIONS_REQUEST_CODE)
                        dialog.dismiss()
                        dialogsList.clear()
                    }.setNegativeButton(R.string.required_permissions_cancel) { dialog, _ ->
                        dialog.dismiss()
                        dialogsList.clear()
                    }.show()
                dialogsList.add(permissionsReasonDialog)
            }
            else -> {
                requestPermissions(requiredPermissions, REQUIRED_PERMISSIONS_REQUEST_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUIRED_PERMISSIONS_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED })) {
                onPermissionGranted.invoke()
            } else {
                val canNotProceedDialog = AlertDialog.Builder(this)
                    .setTitle(this.getString(R.string.permission_denied_title))
                    .setMessage(this.getString(R.string.permission_denied_message))
                    .setPositiveButton(R.string.permission_denied_ok) { dialog, _ ->
                        dialog.dismiss()
                        dialogsList.clear()
                    }.show()
                dialogsList.add(canNotProceedDialog)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dialogsList.forEach {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }
}