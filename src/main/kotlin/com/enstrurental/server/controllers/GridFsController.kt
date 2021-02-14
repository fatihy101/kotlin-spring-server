package com.enstrurental.server.controllers

import com.mongodb.client.gridfs.model.GridFSFile
import org.bson.types.ObjectId
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory
import org.springframework.data.mongodb.core.convert.MappingMongoConverter
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate
import org.springframework.http.codec.multipart.FilePart
import reactor.core.publisher.Mono
import org.springframework.http.ResponseEntity
import org.springframework.data.mongodb.gridfs.ReactiveGridFsResource
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange

import reactor.core.publisher.Flux

@Configuration
class GridFsConfiguration {
    @Bean
    fun reactiveGridFsTemplate (reactiveMongoDatabaseFactory: ReactiveMongoDatabaseFactory,
                                mappingMongoConverter: MappingMongoConverter) : ReactiveGridFsTemplate
            = ReactiveGridFsTemplate(reactiveMongoDatabaseFactory, mappingMongoConverter)
}

@RestController
@RequestMapping("file")
class GridFsController(private val gridFsTemplate: ReactiveGridFsTemplate?) {
    // To update, first delete and then upload
    @CrossOrigin
    @PostMapping("save")
    fun upload(@RequestPart fileParts: Mono<FilePart>): Mono<ResponseEntity<*>>? {
        return fileParts
            .flatMap { part: FilePart ->
                gridFsTemplate!!.store(
                    part.content(),
                    part.filename()
                )
            }
            .map { id: ObjectId -> ResponseEntity.ok().body(id.toHexString()) }
    }

    @CrossOrigin
    @GetMapping("{id}")
    fun read(@PathVariable id: String?, exchange: ServerWebExchange): Flux<Void>? {
        val query = Query(Criteria.where("_id").`is`(id))
        return gridFsTemplate!!.findOne(query)
            .log()
            .flatMap { file: GridFSFile? ->
                gridFsTemplate.getResource(file!!)
            }
            .flatMapMany { r: ReactiveGridFsResource ->
                exchange.response.writeWith(r.content)
            }
    }

    @CrossOrigin
    @DeleteMapping("delete/{id}")
    fun delete(@PathVariable id: String): Mono<ResponseEntity<*>>? =
        gridFsTemplate!!.delete(Query(Criteria.where("_id").`is`(id)))
            .map { ResponseEntity.ok().body("Deleted photo: $id") }


}