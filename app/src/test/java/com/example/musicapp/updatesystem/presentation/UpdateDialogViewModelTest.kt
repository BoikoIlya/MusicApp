package com.example.musicapp.updatesystem.presentation

import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.app.core.UiEventState
import com.example.musicapp.core.testcore.TestDispatcherList
import com.example.musicapp.core.testcore.TestManagerResource
import com.example.musicapp.core.testcore.TestSingleUiStateCommunication
import com.example.musicapp.core.testcore.TestUiEventsCommunication
import com.example.musicapp.main.presentation.MainViewModelTest
import com.example.musicapp.updatesystem.data.UpdateDialogMapper
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
    lateinit var uiEventsCommunication: TestUiEventsCommunication
    lateinit var singleUiStateCommunication: TestSingleUiStateCommunication
    lateinit var managerResource: ManagerResource
    lateinit var repository: MainViewModelTest.TestUpdateSystemRepo

    @Before
    fun setup(){
        repository = MainViewModelTest.TestUpdateSystemRepo()
        managerResource = TestManagerResource()
        uiEventsCommunication = TestUiEventsCommunication()
        singleUiStateCommunication = TestSingleUiStateCommunication()
        viewModel = UpdateDialogViewModel(
            updateSystemRepository = repository,
            dateTransfer = DataTransfer.UpdateDialogTransfer.Base(),
            uiEventsCommunication = uiEventsCommunication,
            dispatchersList = TestDispatcherList(),
            mapper = UpdateDialogMapper.Base(
                singleUiStateCommunication,
                managerResource,
                uiEventsCommunication
            )
        )
    }


   @Test
   fun `test load update`(){
       repository.result = UpdateResult.Success("d")
      viewModel.loadUpdate()
      assertEquals(UiEventState.LoadUpdate::class, uiEventsCommunication.stateList.last()::class)

       repository.result = UpdateResult.Failure(1)
       viewModel.loadUpdate()
       assertEquals(UiEventState.ShowDialog::class,uiEventsCommunication.stateList.last()::class)
       assertEquals(SingleUiEventState.ShowSnackBar.Error::class, singleUiStateCommunication.stateList.last()::class)
   }
}