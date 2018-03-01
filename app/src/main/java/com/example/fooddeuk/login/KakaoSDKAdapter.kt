package com.example.fooddeuk.login

import com.example.fooddeuk.`object`.GlobalApplication.getInstance
import com.kakao.auth.IApplicationConfig
import com.kakao.auth.IPushConfig
import com.kakao.auth.ISessionConfig
import com.kakao.auth.KakaoAdapter

/**
 * Created by heo on 2018. 2. 13..
 */
class KakaoSDKAdapter : KakaoAdapter() {

    override fun getSessionConfig(): ISessionConfig {
        return super.getSessionConfig()
    }

    override fun getPushConfig(): IPushConfig {
        return super.getPushConfig()
    }

    override fun getApplicationConfig(): IApplicationConfig {
        return IApplicationConfig { getInstance() }
    }
}
