package com.stremio.core

import android.util.Log
import com.stremio.core.proto.types.resource.Video
import com.stremio.core.runtime.EnvError
import com.stremio.core.runtime.RuntimeEvent
import com.stremio.core.runtime.msg.Action
import pbandk.Message
import pbandk.decodeFromByteArray
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.full.companionObjectInstance

object Core {
    init {
        System.loadLibrary("stremio_core_android")
    }

    fun interface EventListener {
        fun onEvent(event: RuntimeEvent)
    }

    private val listeners = Collections.newSetFromMap(ConcurrentHashMap<EventListener, Boolean>())

    fun addEventListener(listener: EventListener) {
        listeners.add(listener)
    }

    fun removeEventListener(listener: EventListener) {
        listeners.remove(listener)
    }

    external fun initialize(storage: Storage): EnvError?

    external fun <T> getState(field: Field): T

    external fun dispatch(action: Action, field: Field?)

    external fun getSeriesInfoBinary(): ByteArray

    external fun getStateBinary(field: Field): ByteArray

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Message> getProtobufState(field: Field): T {
        val protobuf = getSeriesInfoBinary()
        val companion = T::class.companionObjectInstance as Message.Companion<T>
        return companion.decodeFromByteArray(protobuf)
    }

    @JvmStatic
    private fun onRuntimeEvent(event: RuntimeEvent) {
        listeners.forEach {
            try {
                it.onEvent(event)
            } catch (e: Exception) {
                Log.e("Stremio", "Failed passing event: ", e)
            }
        }
    }
}
