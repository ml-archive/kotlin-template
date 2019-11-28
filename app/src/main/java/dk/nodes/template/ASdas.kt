package dk.nodes.template

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@Suppress("FunctionName")
fun BroadcastReceiverFlow(c: Context, intentFilter: IntentFilter): Flow<Intent> {
    return callbackFlow {
        val broadcastReceiver = object : android.content.BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                offer(intent)
            }
        }
        c.registerReceiver(broadcastReceiver, intentFilter)
        awaitClose { c.unregisterReceiver(broadcastReceiver) }
    }
}
