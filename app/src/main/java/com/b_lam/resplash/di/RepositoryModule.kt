package com.b_lam.resplash.di

import com.b_lam.resplash.domain.autowallpaper.AutoWallpaperRepository
import com.b_lam.resplash.domain.collection.CollectionRepository
import com.b_lam.resplash.domain.login.LoginRepository
import com.b_lam.resplash.domain.photo.PhotoRepository
import com.b_lam.resplash.domain.user.UserRepository
import org.koin.dsl.module

val repositoryModule = module {

    single(createdAtStart = true) { PhotoRepository(get(), get(), get(), get()) }
    single(createdAtStart = true) { CollectionRepository(get(), get(), get()) }
    single(createdAtStart = true) { UserRepository(get(), get()) }
    single(createdAtStart = true) { LoginRepository(get(), get(), get()) }

    single { AutoWallpaperRepository(get(), get()) }
}