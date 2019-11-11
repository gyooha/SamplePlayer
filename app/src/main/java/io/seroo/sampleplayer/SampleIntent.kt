package io.seroo.sampleplayer

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment

object SampleIntent {
    fun startActivityForResult(activity: Activity, intent: Intent, requestCode: Int){
        try {
            activity.startActivityForResult(intent, requestCode)
        } catch (e: IllegalStateException) {
            if (BuildConfig.DEBUG) e.printStackTrace()
        }
    }

    fun startActivityForResult(fragment: Fragment, intent: Intent, requestCode: Int){
        try {
            fragment.startActivityForResult(intent, requestCode)
        } catch (e: IllegalStateException) {
            if (BuildConfig.DEBUG) e.printStackTrace()
        }
    }
}