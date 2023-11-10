package com.brunagiovanagiovanna.listify_mobile.models

import kotlinx.serialization.Serializable

@Serializable
class Task (
    var _id: String,
    var name: String,
    var description: String,
    var priority: String,
    var dateLimit: String,
    var status: String
):java.io.Serializable