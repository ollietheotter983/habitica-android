package com.habitrpg.android.habitica.helpers.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.habitrpg.android.habitica.helpers.notifications.PushNotificationManager
import org.unifiedpush.android.connector.UnifiedPush
import org.unifiedpush.android.connector.RegistrationToken

/**
 * Adapter to integrate UnifiedPush with existing PushNotificationManager.
 * - Registers app with UnifiedPush distributor
 * - Listens for registration token updates and forwards them to PushNotificationManager
 * - Listens for incoming messages and forwards to PushNotificationManager.displayNotification
 */

class UnifiedPushAdapter(private val context: Context, private val pushManager: PushNotificationManager) {
    companion object {
        private const val TAG = "UnifiedPushAdapter"
    }

    fun registerWithFirstAvailableDistributor() {
        try {
            val distributors = org.unifiedpush.android.connector.UnifiedPushDistributors.distributors(context)
            val distributorPackage = distributors.firstOrNull()?.packageName
            if (distributorPackage == null) {
                Log.w(TAG, "No UnifiedPush distributors found on device")
                return
            }

            UnifiedPush.registerApp(context, distributorPackage)
            // Add registration listener
            UnifiedPush.addRegistrationListener(context) { token: RegistrationToken? ->
                token?.token?.let { regToken ->
                    Log.d(TAG, "UnifiedPush token received: $regToken")
                    // Set the token into existing PushNotificationManager
                    pushManager.refreshedToken = regToken
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to register UnifiedPush", e)
        }
    }
}
