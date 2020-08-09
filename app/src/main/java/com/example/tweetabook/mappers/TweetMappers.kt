package com.example.tweetabook.mappers

import com.example.tweetabook.db.entities.TweetEntity
import com.example.tweetabook.screens.main.adapter.AdapterDataClass
import javax.inject.Inject

class TweetMappers
@Inject
constructor() : Mappers<TweetEntity, AdapterDataClass> {
    override fun mapFromEntity(entity: TweetEntity): AdapterDataClass {
        return AdapterDataClass(
            id = entity.id,
            tweet = entity.tweet,
            imageUri = entity.imageUri,
            tweeted = entity.tweeted
        )
    }

    /*override fun mapToEntity(model: AdapterDataClass): TweetEntity {
        return TweetEntity(
            id = model.id,
            tweet = model.tweet,

        )
    }*/
}