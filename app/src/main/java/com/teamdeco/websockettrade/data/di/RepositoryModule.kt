package com.teamdeco.websockettrade.data.di

import com.teamdeco.websockettrade.data.repository.upbit_coin.CoinRepositoryImpl
import com.teamdeco.websockettrade.domain.repository.upbit_coin.CoinRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsCoinRepository(
        coin_repository: CoinRepositoryImpl,
    ): CoinRepository
}