package com.kamancho.melisma.search.data

import com.kamancho.melisma.app.core.SearchQueryRepository
import com.kamancho.melisma.searchhistory.data.cache.SearchQueryTransfer
import javax.inject.Inject

/**
 * Created by HP on 14.08.2023.
 **/
class BaseSearchQueryRepository @Inject constructor(
    transfer: SearchQueryTransfer
): SearchQueryRepository.Abstract(transfer),SearchQueryRepository