package com.kt.uptodo.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.kt.uptodo.data.UptodoDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FocusViewModel @Inject constructor(
    private val database: UptodoDatabase
) : ViewModel() {

}