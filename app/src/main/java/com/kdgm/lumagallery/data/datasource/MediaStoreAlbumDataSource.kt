package com.kdgm.lumagallery.data.datasource

import android.content.Context
import android.provider.MediaStore
import com.kdgm.lumagallery.core.media.Album

class MediaStoreAlbumDataSource(
    private val context: Context
) {

    fun getAlbums(): List<Album> {

        val albumMap = linkedMapOf<Long, MutableList<android.net.Uri>>()
        val nameMap = hashMapOf<Long, String>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )?.use { cursor ->

            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val bucketIdCol =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
            val bucketNameCol =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

            while (cursor.moveToNext()) {

                val imageId = cursor.getLong(idCol)
                val bucketId = cursor.getLong(bucketIdCol)
                val bucketName = cursor.getString(bucketNameCol)

                val uri = android.content.ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    imageId
                )

                nameMap[bucketId] = bucketName

                val list = albumMap.getOrPut(bucketId) {
                    mutableListOf()
                }
                list.add(uri)
            }
        }

        return albumMap.map { entry ->
            val bucketId = entry.key
            val images = entry.value

            Album(
                bucketId = bucketId,
                name = nameMap[bucketId] ?: "Unknown",
                coverUri = images.first(),
                count = images.size
            )
        }.sortedBy { it.name.lowercase() }
    }


}
