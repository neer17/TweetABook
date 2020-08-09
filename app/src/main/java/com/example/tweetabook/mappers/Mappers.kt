package com.example.tweetabook.mappers

interface Mappers<Entity, AdapterDataClass> {

    fun mapFromEntity(entity: Entity): AdapterDataClass

//    fun mapToEntity(model: AdapterDataClass): Entity
}