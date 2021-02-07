package com.enstrurental.server.controllers

import org.springframework.web.bind.annotation.*
import com.enstrurental.server.services.PhotoService
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.http.codec.multipart.Part
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*
import org.springframework.web.bind.annotation.RequestMapping


@RestController
@RequestMapping("photos")
class PhotosController (val photoService: PhotoService) {
    // BUG: Something wrong with the image BSON data.
    @CrossOrigin
    @PostMapping("/save")
    fun savePhoto(@RequestPart("id") id: String,
                  @RequestPart("image") image: Flux<Part>) : Flux<String>  = image
        .filter { part -> part is FilePart }
        .ofType(FilePart::class.java)
        .flatMap { data -> photoService.addPhoto(id, data) }


    @CrossOrigin
    @GetMapping("get/{id}")
    fun getPhoto(@PathVariable id: String) : Mono<ResponseEntity<String>> = photoService.getPhoto(id)
    .map { photo_object -> ResponseEntity.ok(Base64.getEncoder().encodeToString(photo_object.image.data)) }
    // In front-end, photo will be consumed as Base64. Therefore, encoding is required.
}