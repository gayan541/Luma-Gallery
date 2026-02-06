package com.kdgm.lumagallery.data.datasource

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.kdgm.lumagallery.core.media.ImageMedia

class MediaStoreImageDataSource(
    private val context: Context
) {

    fun getImages(): List<ImageMedia> {
        val images = mutableListOf<ImageMedia>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.SIZE
        )

        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->

            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                images += ImageMedia(
                    id = id,
                    uri = uri,
                    name = cursor.getString(nameColumn),
                    dateTaken = cursor.getLong(dateColumn),
                    size = cursor.getLong(sizeColumn)
                )
            }
        }

        return images
    }
}

