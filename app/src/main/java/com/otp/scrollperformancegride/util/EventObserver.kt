package com.otp.scrollperformancegride.util

import androidx.lifecycle.Observer

/**
 * An [Observer] for [Event]s, simplifying the consumption of one-time events.
 */
class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(value: Event<T>) {
        value.getContentIfNotHandled()?.let {
            onEventUnhandledContent(it)
        }
    }
}