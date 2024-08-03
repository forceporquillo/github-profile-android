package dev.forcecodes.hov.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

fun <R> ViewModel.launchAndCollect(useCase: Flow<R>, collector: FlowCollector<R>) =
    viewModelScope.launch { useCase.collect(collector) }

fun <R> ViewModel.launchAndCollect(useCase: () -> Flow<R>, collector: FlowCollector<R>) =
    launchAndCollect(useCase(), collector)