package com.juarez.coppeldemo.heroes.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Hero(
    var id: String = "",
    var name: String = "",
    var image: Image = Image(),
    var powerStats: PowerStats = PowerStats(),
    var biography: Biography = Biography(),
    var appearance: Appearance = Appearance(),
    var connections: Connections = Connections(),
)

@Entity(tableName = "heroes_table")
data class HeroEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val heroId: Int,
    val name: String,
    val image: String,
)

fun Hero.toEntity(): HeroEntity {
    return HeroEntity(heroId = id.toInt(), name = name, image = image.url!!)
}

fun HeroEntity.toModel(): Hero {
    return Hero(id = heroId.toString(), name = name, image = Image(image))
}

data class Image(val url: String? = "")

data class PowerStats(
    val name: String? = "",
    val intelligence: String? = "",
    val strength: String? = "",
    val speed: String? = "",
    val durability: String? = "",
    val power: String? = "",
    val combat: String? = "",
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
    val alignment: String? = "",
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

