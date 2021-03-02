package com.enstrurental.server.controllers

import com.enstrurental.server.entitites.Products
import com.enstrurental.server.entitites.ProductsRepository
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("products")
class ProductsController(val productsRepository: ProductsRepository,
                         private val gridFsTemplate: ReactiveGridFsTemplate?) {

    @CrossOrigin
    @GetMapping("/")
    fun getAllProducts() : Flux<Products> =  productsRepository.findAll() // Todo: Order by added_date

    @CrossOrigin
    @GetMapping("/{category}")
    fun getByCategory(@PathVariable category: String) : Flux<Products>{
        return productsRepository.findProductsByCategory(category)
    }

    @CrossOrigin
    @PostMapping("/save")
    fun saveProduct(@RequestBody product : Products): Mono<ResponseEntity<Products>>
    = productsRepository.save(product)
            .map { response -> ResponseEntity.ok(response) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @CrossOrigin
    @DeleteMapping("/delete/{id}")
    fun deleteProduct(@PathVariable id: String): Mono<ResponseEntity<String>> {
        // First we need to find the product.
        productsRepository.findById(id).map { product ->
            // Then we're checking, is there any photo.
            // TODO: TEST
            if (product.photo_ids!!.isNotEmpty()) {
                // if there's, we're deleting photos from GridFS
            product.photo_ids!!.forEach { id -> gridFsTemplate!!.delete(Query(Criteria.where("_id").`is`(id))) }
            }
        }
        // Finally, we're deleting the object itself.
        return productsRepository.deleteById(id)
            .map { ResponseEntity.ok(" Product deleted. ID: $id") }
            .defaultIfEmpty(ResponseEntity.notFound().build())
    }
}