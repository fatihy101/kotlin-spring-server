package com.enstrurental.server.entitites

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.sql.Timestamp

@Document
data class Instruments(
        @Id
        val id: Int,
        val renter_id: String,
        val added_date: Timestamp = Timestamp(System.currentTimeMillis()),
        var name: String,
        var category: String?,
        var description: String?,
        var availability: Boolean = true,
        var unavailable_due_date: Timestamp?,
        var price_by_day: Double?,
        var deposit_price: Double?,
        var photo_ids: List<String>?,
        var open_to_sell: Boolean = false,
        var is_used: Boolean = false,
        var available_delivery_types : List<String>?,
        var tags :List<String>?
        )