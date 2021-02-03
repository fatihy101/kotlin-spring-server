package com.enstrurental.server.entitites

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.sql.Timestamp
import java.time.LocalDateTime

@Document
data class Products(
        @Id
        val id: String,
        val renter_id: String,
        val added_date: LocalDateTime? = LocalDateTime.now(),
        var category: String?,
        var brand: String?,
        var model: String?,
        var info: String?,
        var is_rental: Boolean?,
        var is_deposit_required: Boolean?,
        var is_open_to_sell: Boolean?,
        var is_used: Boolean?,
        var max_rental_days: Int?,
        var daily_price: Double?,
        var full_price: Double?,
        var deposit_price: Double?,
        var stock_quantity: Int?,
        var delivery_types : List<String>?,
        var photo_ids: List<String>?,
        var tags :List<String>?
        )