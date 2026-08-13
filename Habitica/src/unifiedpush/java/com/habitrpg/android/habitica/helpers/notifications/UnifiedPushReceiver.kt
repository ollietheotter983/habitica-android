import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.habitrpg.android.habitica.helpers.notifications.PushNotificationManager
import org.unifiedpush.android.connector.UnifiedPush

class UnifiedPushReceiver : BroadcastReceiver() {
    companion object {
        private const val TAG = "UnifiedPushReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        try {
            // UnifiedPush put message payload in extras; extract string values into a map
            val data = HashMap<String, String>()
            val extras: Bundle? = intent.extras
            extras?.keySet()?.forEach { key ->
                val value = extras.get(key)?.toString()
                if (value != null) data[key] = value
            }

            Log.d(TAG, "UnifiedPush message received: $data")

            // Forward to existing notification display helper (map-based)
            PushNotificationManager.displayNotificationFromMap(data, context, null)
        } catch (e: Exception) {
            Log.e("UnifiedPushReceiver", "Error handling UnifiedPush message", e)
        }
    }
}
