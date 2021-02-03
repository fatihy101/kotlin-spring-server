package com.enstrurental.server.controllers

import com.enstrurental.server.entitites.Products
import com.enstrurental.server.entitites.ProductsRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("products")
class ProductsController(val productsRepository: ProductsRepository) {

    @CrossOrigin
    @GetMapping("/")
    fun getAllProducts() : Flux<Products> =  productsRepository.findAll() // Todo: Order by added_date

    @CrossOrigin
    @GetMapping("/{category}")
    fun getByCategory(@PathVariable category: String) : Flux<Products>{
        return productsRepository.findProductsByCategory(category)
    }

    @CrossOrigin
    @PostMapping("/create")
    fun createProduct(@RequestBody product : Products): Mono<ResponseEntity<Products>>
    = productsRepository.save(product)
            .map { response -> ResponseEntity.ok(response) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @CrossOrigin
    @DeleteMapping("/delete/{id}")
    fun deleteProduct(@PathVariable id: String): Mono<ResponseEntity<String>> = productsRepository.deleteById(id)
            .map { ResponseEntity.ok(" Product deleted. ID: $id")}
            .defaultIfEmpty(ResponseEntity.notFound().build())

}