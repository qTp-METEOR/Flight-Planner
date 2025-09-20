package com.example.flightplanner.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flightplanner.viewmodel.ChecklistViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistTemplateDetailScreen(
    vm: ChecklistViewModel,
    templateId: Long,
    onBack: () -> Unit
) {
    val template = vm.templates.collectAsState().value.firstOrNull { it.id == templateId }

    var newCategory by remember { mutableStateOf("") }
    var newItemText by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }

    if (template == null) {
        Text("Template not found")
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(template.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Add category
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = newCategory,
                    onValueChange = { newCategory = it },
                    label = { Text("New category") },
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = {
                    if (newCategory.isNotBlank()) {
                        vm.addCategory(templateId, newCategory.trim())
                        newCategory = ""
                    }
                }) { Text("Add") }
            }

            // Show categories
            if (template.categories.isEmpty()) {
                Text("No categories yet. Add one above.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(template.categories) { category ->
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(category.name, style = MaterialTheme.typography.titleMedium)
                                IconButton(onClick = {
                                    vm.removeCategory(templateId, category.id)
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete category")
                                }
                            }

                            // Items in category
                            category.items.forEach { item ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(item.text)
                                    IconButton(onClick = {
                                        vm.removeItem(templateId, category.id, item.id)
                                    }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete item")
                                    }
                                }
                            }

                            // Add new item row (one per category)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = if (selectedCategoryId == category.id) newItemText else "",
                                    onValueChange = {
                                        selectedCategoryId = category.id
                                        newItemText = it
                                    },
                                    label = { Text("New item") },
                                    modifier = Modifier.weight(1f)
                                )
                                Button(onClick = {
                                    if (newItemText.isNotBlank() && selectedCategoryId == category.id) {
                                        vm.addItem(templateId, category.id, newItemText.trim())
                                        newItemText = ""
                                    }
                                }) { Text("Add") }
                            }
                        }
                    }
                }
            }
        }
    }
}
