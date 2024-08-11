package com.teamdeco.websockettrade.data.di

import okhttp3.OkHttpClient
import okhttp3.Request
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WebSocketModule {

    private val UPBIT_WEBSOCKET_BASE_URL = "https://api.upbit.com/websocket/v1"

    // Upbit의 웹소켓을 사용하는 경우, 싱글톤으로 재사용할 수 있는 인스턴스라고 판단
    @Singleton
    //@Provides
    fun provideUpbitWebSocketClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Singleton
    //@Provides
    fun provideUpbitWebSocketRequest(): Request {
        return Request.Builder()
            .url(UPBIT_WEBSOCKET_BASE_URL)
            .build()
    }


    /* 또다른 웹소켓을 사용하는 경우 여기에 추가해줍니다 */


}