package com.example.musicapp.popular.data

import androidx.media3.common.MediaItem
import com.example.musicapp.app.vkdto.TrackItem
import com.example.musicapp.captcha.data.cache.CaptchaDataStore
import com.example.musicapp.main.data.cache.AccountDataStore
import com.example.musicapp.trending.domain.TrackDomain
import javax.inject.Inject

/**
 * Created by HP on 22.08.2023.
 **/
interface PopularRepository {

    suspend fun fetchPopulars(): List<TrackDomain>

    class Base @Inject constructor(
        private val popularsService: PopularsService,
        private val accountDataStore: AccountDataStore,
        private val captchaDataStore: CaptchaDataStore,
        private val mapper: TrackItem.Mapper<TrackDomain>
    ): PopularRepository {

        override suspend fun fetchPopulars(): List<TrackDomain>{
            return popularsService.getPopular(
                accessToken = accountDataStore.token(),
                owner_id = accountDataStore.ownerId(),
                captcha_sid = captchaDataStore.captchaId(),
                captcha_key = captchaDataStore.captchaEnteredData(),
            ).response.map { it.map(mapper) }
        }
    }
}