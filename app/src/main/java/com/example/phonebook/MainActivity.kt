package com.example.phonebook

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.phonebook.routing.MyContactsRouter
import com.example.phonebook.routing.Screen
import com.example.phonebook.screens.ContactsScreen
import com.example.phonebook.screens.SaveContactScreen
import com.example.phonebook.screens.TrashScreen
import com.example.phonebook.ui.theme.MyContactsTheme
import com.example.phonebook.ui.theme.MyContactsThemeSettings
import com.example.phonebook.viewmodel.MainViewModel
import com.example.phonebook.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyContactsTheme(darkTheme = MyContactsThemeSettings.isDarkThemeEnabled) {
                val viewModel: MainViewModel = viewModel(
                    factory = MainViewModelFactory(LocalContext.current.applicationContext as Application)
                )
                MainActivityScreen(viewModel)
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun MainActivityScreen(viewModel: MainViewModel) {
    Surface {
        when (MyContactsRouter.currentScreen) {
            is Screen.Contacts -> ContactsScreen(viewModel)
            is Screen.SaveContact -> SaveContactScreen(viewModel)
            is Screen.Trash -> TrashScreen(viewModel)
        }
    }
}
