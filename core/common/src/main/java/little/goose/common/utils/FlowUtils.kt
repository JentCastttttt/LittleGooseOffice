package little.goose.common.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

inline fun Fragment.launchAndRepeatWithViewLifeCycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
): Job {
    return viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
            block()
        }
    }
}

inline fun <T> Flow<T>.collectLastWithLifecycleOwner(
    lifecycleOwner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.(data: T) -> Unit
) = this.collectLastWithLifecycle(lifecycleOwner.lifecycle, minActiveState, block)

inline fun <T> Flow<T>.collectLastWithLifecycle(
    lifecycle: Lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.(data: T) -> Unit
) = lifecycle.coroutineScope.launch {
    this@collectLastWithLifecycle.flowWithLifecycle(lifecycle, minActiveState).collectLatest {
        block(it)
    }
}