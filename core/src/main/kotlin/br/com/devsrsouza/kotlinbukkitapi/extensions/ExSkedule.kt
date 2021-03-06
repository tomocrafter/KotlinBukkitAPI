package br.com.devsrsouza.kotlinbukkitapi.extensions.skedule

import br.com.devsrsouza.kotlinbukkitapi.extensions.plugin.WithPlugin
import br.com.devsrsouza.kotlinbukkitapi.utils.getTakeValuesOrNull
import br.com.devsrsouza.kotlinbukkitapi.utils.registerCoroutineContextTakes
import br.com.devsrsouza.kotlinbukkitapi.utils.time.Millisecond
import br.com.devsrsouza.kotlinbukkitapi.utils.unregisterCoroutineContextTakes
import com.okkero.skedule.BukkitDispatcher
import com.okkero.skedule.BukkitSchedulerController
import com.okkero.skedule.SynchronizationContext
import com.okkero.skedule.schedule
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

fun WithPlugin<*>.schedule(
        initialContext: SynchronizationContext = SynchronizationContext.SYNC,
        co: suspend BukkitSchedulerController.() -> Unit
) = plugin.schedule(initialContext, co)

val BukkitSchedulerController.contextSync get() = SynchronizationContext.SYNC
val BukkitSchedulerController.contextAsync get() = SynchronizationContext.ASYNC

suspend fun BukkitSchedulerController.switchToSync() = switchContext(contextSync)
suspend fun BukkitSchedulerController.switchToAsync() = switchContext(contextAsync)

val WithPlugin<*>.BukkitDispatchers get() = JavaPlugin.getProvidingPlugin(plugin::class.java).BukkitDispatchers
val Plugin.BukkitDispatchers get() = PluginDispatcher(JavaPlugin.getProvidingPlugin(this::class.java))

inline class PluginDispatcher(val plugin: JavaPlugin) {
    val ASYNC get() = BukkitDispatcher(plugin, true)
    val SYNC get() = BukkitDispatcher(plugin, false)
}

// Take max millisecond in a tick

suspend fun BukkitSchedulerController.takeMaxPerTick(time: Millisecond) {
    val takeValues = getTakeValuesOrNull(context)

    if(takeValues == null) {
        // registering take max at current millisecond
        registerCoroutineContextTakes(context, time)
    } else {
        // checking if this exceeded the max time of execution
        if(takeValues.wasTimeExceeded()) {
            unregisterCoroutineContextTakes(context)
            waitFor(1)
        }
    }
}
