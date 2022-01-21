package com.juarez.coppeldemo.data.models

import com.google.gson.annotations.SerializedName

data class Hero(
    val id: String,
    val name: String,
    val image: Image,
    val powerStats: PowerStats,
    val biography: Biography,
    val appearance: Appearance,
    val connections: Connections
)

data class Image(val url: String? = "")

data class PowerStats(
    val name: String? = "",
    val intelligence: String? = "",
    val strength: String? = "",
    val speed: String? = "",
    val durability: String? = "",
    val power: String? = "",
    val combat: String? = ""
)

data class Biography(
    val name: String? = "",
    @SerializedName("full-name")
    val fullName: String? = "",
    @SerializedName("alter-egos")
    val alterEgos: String? = "",
    val aliases: List<String>? = arrayListOf(),
    @SerializedName("place-of-birth")
    val placeOfBirth: String? = "",
    @SerializedName("first-appearance")
    val firstAppearance: String? = "",
    val publisher: String? = "",
    val alignment: String? = ""
)

data class Appearance(
    val gender: String? = "",
    val race: String? = "",
    val height: List<String>? = arrayListOf(),
    val weight: List<String>? = arrayListOf(),
    @SerializedName("eye-color")
    val eyeColor: String? = "",
    @SerializedName("hair-color")
    val hairColor: String? = "",
)

data class Connections(
    @SerializedName("group-affiliation")
    val groupAffiliation: String? = "",
    val relatives: String? = "",
)

