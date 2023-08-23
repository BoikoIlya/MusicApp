package com.kamancho.melisma.app.core

import com.kamancho.melisma.R
import java.lang.Exception

/**
 * Created by HP on 21.07.2023.
 **/

interface VkException {

    suspend fun map(): Int

    class Base(private val code: Int) : Exception(), VkException {

        override suspend fun map(): Int {
            return when (code) {
                500 -> R.string.the_action_is_prohibited
                300 -> R.string.the_album_is_full
                203 -> R.string.access_to_community_is_denied
                201 -> R.string.access_to_audio_is_denied
                200 -> R.string.access_to_album_is_denied
                150 -> R.string.wrong_timestamp
                113 -> R.string.wrong_user_id
                101 -> R.string.wrong_app_id
                100 -> R.string.wrong_paramenter
                30 -> R.string.private_profile
                29 -> R.string.too_many_requests_try_again_later
                28 -> R.string.app_access_key_is_invalid
                27 -> R.string.community_acess_key_is_invalid
                24 -> R.string.confirmation_from_the_user_is_required
                23 -> R.string.the_method_was_disabled
                21 -> R.string.this_action_is_allowed_only_for_standalone_and_open_api_apps
                20 -> R.string.this_action_is_prohibited_for_non_standalone_apps
                18 -> R.string.the_page_has_been_deleted_or_blocked
                17 -> R.string.user_validation_required
                16 -> R.string.requests_over_https_are_required
                15 -> R.string.access_is_denied
                14 -> R.string.you_need_to_enter_captcha
                11 -> R.string.user_not_logged_in_or_app_is_disabled_by_developer
                10 -> R.string.internal_server_error
                9 -> R.string.there_are_too_many_similar_actions
                8 -> R.string.wrong_request
                7 -> R.string.no_rights_to_perform_this_action
                6 -> R.string.too_many_requests_per_second
                5 -> R.string.authorization_failed
                4 -> R.string.invalid_signature
                3 -> R.string.unknown_page
                2 -> R.string.app_is_disabled_by_developer
                1 -> R.string.unknown_error_try_again_later
                else -> R.string.oops_something_went_wrong_data
            }
        }


    }
}

