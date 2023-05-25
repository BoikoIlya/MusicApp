package com.example.musicapp.updatesystem.presentation

import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.favorites.testcore.TestDispatcherList
import com.example.musicapp.favorites.testcore.TestManagerResource
import com.example.musicapp.favorites.testcore.TestSingleUiStateCommunication
import com.example.musicapp.main.presentation.MainViewModelTest
import com.example.musicapp.updatesystem.data.UpdateResult
import com.example.musicapp.updatesystem.data.UpdateSystemRepository
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 29.04.2023.
 **/
class UpdateDialogViewModelTest {

    lateinit var viewModel: UpdateDialogViewModel
    lateinit var singleUiStateCommunication: TestSingleUiStateCommunication
    lateinit var managerResource: ManagerResource
    lateinit var repository: MainViewModelTest.TestUpdateSystemRepo

    @Before
    fun setup(){
        repository = MainViewModelTest.TestUpdateSystemRepo()
        managerResource = TestManagerResource()
        singleUiStateCommunication = TestSingleUiStateCommunication()
        viewModel = UpdateDialogViewModel(
            dateTransfer = DataTransfer.UpdateDialogTransfer.Base(),
            dispatchersList = TestDispatcherList(),
           singleUiEventCommunication = singleUiStateCommunication,
            managerResource = managerResource
        )
    }


   @Test
   fun `test load update`(){
       repository.result = UpdateResult.Success("d")
      viewModel.loadUpdate("sdsfsd")
      assertEquals(SingleUiEventState.LoadUpdate::class, singleUiStateCommunication.stateList.last()::class)

       repository.result = UpdateResult.Failure(1)
       viewModel.loadUpdate("")
       assertEquals(SingleUiEventState.ShowSnackBar.Error::class,
           singleUiStateCommunication.stateList[singleUiStateCommunication.stateList.lastIndex-1]::class)
       assertEquals(SingleUiEventState.ShowDialog::class, singleUiStateCommunication.stateList.last()::class)
   }
}