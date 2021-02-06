package com.enstrurental.server.entitites

import org.bson.types.Binary
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Photos (
    @Id
    val id: String,
    var image: Binary
    )