package com.example.musicapp.search.data

import com.example.musicapp.app.core.SearchQueryRepository
import com.example.musicapp.searchhistory.data.cache.SearchQueryTransfer
import javax.inject.Inject

/**
 * Created by HP on 14.08.2023.
 **/
class BaseSearchQueryRepository @Inject constructor(
    transfer: SearchQueryTransfer
): SearchQueryRepository.Abstract(transfer),SearchQueryRepository