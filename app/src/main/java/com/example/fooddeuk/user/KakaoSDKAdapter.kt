package com.example.fooddeuk.user

import com.example.fooddeuk.GlobalApplication.getInstance
import com.kakao.auth.IApplicationConfig
import com.kakao.auth.KakaoAdapter

/**
 * Created by heo on 2018. 2. 13..
 */
class KakaoSDKAdapter : KakaoAdapter() {

    override fun getApplicationConfig(): IApplicationConfig {
        return IApplicationConfig { getInstance() }
    }
}
