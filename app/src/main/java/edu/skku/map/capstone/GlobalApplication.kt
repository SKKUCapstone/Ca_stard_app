package edu.skku.map.capstone

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // Kakao Sdk 초기화
        KakaoSdk.init(this, "09e7ce580fee2dc13ec5d24c66cd8238")
    }
}