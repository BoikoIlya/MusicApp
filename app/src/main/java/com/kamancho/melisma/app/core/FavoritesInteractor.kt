package com.kamancho.melisma.app.core

import com.kamancho.melisma.R

/**
 * Created by HP on 27.06.2023.
 **/
interface FavoritesInteractor<D,C,N,U,E>: Interactor<U,E> {

    fun saveItemToTransfer(item: U)

    suspend fun equalsWithUserId(id: Int): Boolean


    abstract class Abstract<D,C,N,M,E>(
        private val repository: FavoritesRepository<D,C,N>,
        private val managerResource: ManagerResource,
        private val uiToDomainMapper: Mapper<M,D>,
        private val handleResponse: HandleResponse,
        private val transfer: DataTransfer<D>
    ): FavoritesInteractor<D,C,N,M,E>{

        override suspend fun addToFavorites(item: M): E =
            handleResponse.handle({
                val newId = repository.addToFavorites(uiToDomainMapper.map(item))
                success(message = managerResource.getString(R.string.success_add_message), newId = newId)
            },{message,error->
                error(message = message)
            })


        override suspend fun addToFavoritesIfNotDuplicated(item: M): E =
            handleResponse.handle({
                val newId = repository.addToFavoritesIfNotDuplicated(uiToDomainMapper.map(item))
                success(message = managerResource.getString(R.string.success_add_message), newId = newId)
            },{message,error->
                if(error is DuplicateException)
                    duplicated()
                else
                    error(message = message)
            })


        override suspend fun deleteData(id: Int): E =
            handleResponse.handle({
                repository.deleteData(id)
                success(message = managerResource.getString(R.string.success_remove_message))
            },{message,error->
                error(message)
            })

        override suspend fun updateData(): String = handleResponse.handle({
            repository.updateData()
            ""
            },{message,exception->
                message
            })

        override suspend fun containsTrackInDb(item: Pair<String,String>) = repository.containsInDb(item)

        override fun saveItemToTransfer(item: M) = transfer.save(uiToDomainMapper.map(item))

        override suspend fun equalsWithUserId(id: Int): Boolean = id == repository.userId()


        protected abstract fun success(message: String, newId: Int=-1):E

        protected abstract fun error(message: String):E

        protected abstract fun duplicated():E
    }
}
