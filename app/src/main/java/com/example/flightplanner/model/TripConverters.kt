package com.example.flightplanner.model

private var tripChecklistIdGen = 1L
private var tripCategoryIdGen = 1L
private var tripItemIdGen = 1L

fun ChecklistTemplate.toTripChecklist(): TripChecklist {
    return TripChecklist(
        id = tripChecklistIdGen++,
        name = this.name,
        categories = this.categories.map { cat ->
            TripChecklistCategory(
                id = tripCategoryIdGen++,
                name = cat.name,
                items = cat.items.map { item ->
                    TripChecklistItem(
                        id = tripItemIdGen++,
                        text = item.text,
                        checked = false
                    )
                }
            )
        }
    )
}
