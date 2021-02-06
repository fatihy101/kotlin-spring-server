package com.enstrurental.server.controllers

import org.springframework.web.bind.annotation.*
import com.enstrurental.server.services.PhotoService
import org.bson.types.Binary
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("photos")
class PhotosController (val photoService: PhotoService) {

    @CrossOrigin
    @PostMapping("/save")
    fun savePhoto(@RequestPart("id") id: String,
                  @RequestPart("image") image: MultipartFile): Mono<String> = photoService.addPhoto(id, image)

    @CrossOrigin
    @GetMapping("get/{id}")
    fun getPhoto(@PathVariable id: String) : Mono<ResponseEntity<String>> = photoService.getPhoto(id)
    .map { photo_object -> ResponseEntity.ok(Base64.getEncoder().encodeToString(photo_object.image.data)) }
    // In front-end, photo will be consumed as Base64. Therefore, encoding is required.
}