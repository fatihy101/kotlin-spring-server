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
import java.nio.file.Path
import java.nio.file.Paths

@Service
class PhotoService {
    @Autowired
    lateinit var photosRepository: PhotosRepository

    fun addPhoto(id: String, filePart: FilePart): Mono<String> {

        return filePart.content().flatMap { dataBuffer ->
            /* TODO: Find out how to extract data from dataBuffer. This links might be handy:
             (l1) https://stackoverflow.com/questions/46460599/how-to-correctly-read-fluxdatabuffer-and-convert-it-to-a-single-inputstream
             (l2) https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/core/io/buffer/DataBuffer.html  --> (DataBuffer docs)
              Conclusions:
              # [1] This conversion is definitely wrong. readableByteCount() returns number of bytes, not the content.
              # First I should try to get content properly. TODO: Check all the links which I noted here and extract the content of filePart.
              # If you fail to do so, don't waste your time and try to implement reactiveGridFS
              Related Link: https://github.com/hantsy/spring-reactive-sample/blob/master/boot-data-mongo-gridfs/src/main/java/com/example/demo/DemoApplication.java
            */
            val bytes = ByteArray(dataBuffer.readableByteCount()) // [1]
            val photo = Photos(id, Binary(BsonBinarySubType.BINARY, bytes))
            photosRepository.save(photo).map { saved_photo -> saved_photo.id }
        }.last()
    }

    fun getPhoto(id:String): Mono<Photos> = photosRepository.findById(id)
}