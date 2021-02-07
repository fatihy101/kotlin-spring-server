package com.enstrurental.server.services

import com.enstrurental.server.entitites.Photos
import com.enstrurental.server.entitites.PhotosRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.bson.BsonBinarySubType
import org.bson.types.Binary
import org.springframework.http.codec.multipart.FilePart
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

import java.io.IOException

@Service
class PhotoService {
    @Autowired
    lateinit var photosRepository: PhotosRepository

    fun addPhoto(id: String, filePart: FilePart): Mono<String>  = filePart.content().flatMap { dataBuffer ->
            val bytes = ByteArray(dataBuffer.readableByteCount())
            val photo = Photos(id, Binary(BsonBinarySubType.BINARY, bytes))
            photosRepository.save(photo).map { saved_photo -> saved_photo.id }
        }.last()

    fun getPhoto(id:String): Mono<Photos> = photosRepository.findById(id)
}