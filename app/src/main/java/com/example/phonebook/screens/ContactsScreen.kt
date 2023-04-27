package com.example.phonebook.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.phonebook.domain.model.ContactModel
import com.example.phonebook.routing.Screen
import com.example.phonebook.ui.components.AppDrawer
import com.example.phonebook.ui.components.Contact
import com.example.phonebook.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalMaterialApi
@Composable
fun ContactsScreen(viewModel: MainViewModel) {
    val contacts by viewModel.contactsNotInTrash.observeAsState(listOf())
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Contacts",
                        color = MaterialTheme.colors.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch { scaffoldState.drawerState.open() }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.List,
                            contentDescription = "Drawer Button"
                        )
                    }
                }
            )
        },
        drawerContent = {
            AppDrawer(
                currentScreen = Screen.Contacts,
                closeDrawerAction = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onCreateNewContactClick() },
                contentColor = MaterialTheme.colors.background,
                content = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Contact Button"
                    )
                }
            )
        }
    ) {
        if (contacts.isNotEmpty()) {
            ContactsList(
                contacts = contacts,
                onContactCheckedChange = {
                    viewModel.onContactCheckedChange(it)
                },
                onContactClick = { viewModel.onContactClick(it) }
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun ContactsList(
    contacts: List<ContactModel>,
    onContactCheckedChange: (ContactModel) -> Unit,
    onContactClick: (ContactModel) -> Unit
) {
    val sortContacts = contacts.sortedBy { it.name.uppercase() }
    var currentLetters = ""
    LazyColumn {
        items(count = sortContacts.size) { contactIndex ->
            val contact = sortContacts[contactIndex]
            val firstLetter = contact.name.first().uppercaseChar().toString()

            if(firstLetter != currentLetters){
                currentLetters = firstLetter
                Text(
                    text = currentLetters,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .width(150.dp),
                    style = MaterialTheme.typography.h6
                )
            }
            Contact(
                contact = contact,
                onContactClick = onContactClick,
                isSelected = false
            )
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun ContactsListPreview() {
    ContactsList(
        contacts = listOf(
            ContactModel(1, "test1", "01xxxxx"),
            ContactModel(2, "test1", "02xxxxx"),
            ContactModel(3, "test1", "03xxxxx")
        ),
        onContactCheckedChange = {},
        onContactClick = {}
    )
}