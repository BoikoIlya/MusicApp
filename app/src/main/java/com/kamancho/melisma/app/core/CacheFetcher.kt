package com.kamancho.melisma.app.core

import kotlinx.coroutines.Job

/**
 * Created by Ilya Boiko @camancho
on 08.12.2023.
 **/
interface CacheFetcher {

 fun fetchFromCache(query: String)

 fun clearDownloadCommunication()
}