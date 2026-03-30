package com.vaibhav.todoapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vaibhav.todoapp.ui.theme.ToDoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoAppTheme {
                MainPage()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage() {
    val myContext = LocalContext.current
    val toDoName = remember {
        mutableStateOf("")
    }

    val focusManage = LocalFocusManager.current

    val itemList = ReadData(myContext)

    val deletedialogStatus = remember {
        mutableStateOf(false)
    }

    val clickedIndex = remember {
        mutableStateOf(0)
    }

    val clickedItem = remember {
        mutableStateOf("")
    }

    val updateDialogStatus = remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
//        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = toDoName.value,
                onValueChange = {
                    toDoName.value = it
                },
                label = { Text(text = "Enter ToDO") },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = colorResource(id = R.color.Green),
                    unfocusedLabelColor = Color.White,
                    errorContainerColor = colorResource(id = R.color.teal_200),
                    focusedTextColor = Color.Black,
                    cursorColor = Color.White
                ),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .border(1.dp, Color.Black, RoundedCornerShape(5.dp))
                    .weight(.7F)
                    .height(60.dp),
                textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 18.sp)
            )
            Spacer(modifier = Modifier.width(5.dp))

            Button(
                onClick = {
                    if (toDoName.value.isNotEmpty()) {
                        itemList.add(toDoName.value)
                        WriteData(itemList, myContext)
                        toDoName.value = ""
                        focusManage.clearFocus()
                    } else {
                        Toast.makeText(myContext, "Please Enter ToDo", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .weight(.3F)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.Green),
                    contentColor = Color.White
                )
            ) {
                Text(text = "Add", fontSize = 20.sp)
            }
        }

        LazyColumn {
            items(
                count = itemList.size,
                itemContent = { index ->
                    val item = itemList[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(1.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(0.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item,
                                color = Color.White,
                                fontSize = 18.sp,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.width(300.dp)
                            )

                            Row() {
                                IconButton(onClick = {
                                    clickedIndex.value = index
                                    clickedItem.value = item
                                    updateDialogStatus.value = true
                                }) {
                                    Icon(
                                        Icons.Filled.Edit,
                                        contentDescription = "Edit",
                                        tint = Color.White
                                    )
                                }

                                IconButton(onClick = {
                                    deletedialogStatus.value = true
                                    clickedIndex.value = index
                                }) {
                                    Icon(
                                        Icons.Filled.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            )
        }


        if (deletedialogStatus.value) {
            AlertDialog(
                onDismissRequest = {
                    deletedialogStatus.value = false
                },
                title = {
                    Text(text = "Delete")
                },
                text = {
                    Text(text = "Do you want to delete your ToDo")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            itemList.remove(itemList[clickedIndex.value])
                            WriteData(itemList, myContext)
                            deletedialogStatus.value = false
                            Toast.makeText(myContext, "Item is Removed", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text(text = "Yes")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            deletedialogStatus.value = false
                        }
                    ) {
                        Text(text = "No")
                    }
                }

            )
        }

        if (updateDialogStatus.value) {
            AlertDialog(
                onDismissRequest = {
                    updateDialogStatus.value = false
                },
                title = {
                    Text(text = "Edit")
                },
                text = {
                    TextField(
                        value = clickedItem.value,
                        onValueChange = {
                            clickedItem.value = it
                        }
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            itemList[clickedIndex.value] = clickedItem.value
                            WriteData(itemList, myContext)
                            updateDialogStatus.value = false
                            Toast.makeText(myContext, "Item is Updated", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text(text = "Confirm")
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun SimpleComposablePreview() {
    MainPage()
}