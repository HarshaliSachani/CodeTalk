package com.example.chatapp.modules

import com.example.chatapp.retrofits_api.ApiRepository
import com.example.chatapp.viewmodels.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val splashViewModelModule = module { viewModel { SplashViewModel(get()) } }
val loginViewModelModule = module { viewModel { LoginViewModel(get()) } }
val forgetPasswordViewModelModule = module { viewModel { ForgetPasswordViewModel(get()) } }
val dashboardViewModelModule = module { viewModel { DashboardViewModel(get()) } }
val chatViewModelModule = module { viewModel { ChatViewModel(get()) } }
val apiRepositoryModule = module { single { ApiRepository() } }