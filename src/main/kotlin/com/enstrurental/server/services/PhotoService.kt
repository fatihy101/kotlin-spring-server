package com.enstrurental.server.services

import com.enstrurental.server.entitites.Photos
import com.enstrurental.server.entitites.PhotosRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.bson.BsonBinarySubType
import org.bson.types.Binary
import reactor.core.publisher.Mono

import java.io.IOException

@Service
class PhotoService {
    @Autowired
    lateinit var photosRepository: PhotosRepository

    fun addPhoto(id: String, file: MultipartFile): Mono<String> {
        val photo = Photos(id, Binary(BsonBinarySubType.BINARY, file.bytes))
        return photosRepository.save(photo).map { saved_photo -> saved_photo.id }
    }

    fun getPhoto(id:String): Mono<Photos> = photosRepository.findById(id)
}