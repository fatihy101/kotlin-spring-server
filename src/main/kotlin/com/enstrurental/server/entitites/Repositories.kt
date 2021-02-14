package com.enstrurental.server.entitites

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface ClientsRepository: ReactiveMongoRepository<Clients, String>

@Repository
interface RentersRepository: ReactiveMongoRepository<Renters, String>

@Repository
interface OrdersRepository: ReactiveMongoRepository<Orders, Int>

@Repository
interface ProductsRepository: ReactiveMongoRepository<Products, String>{
    fun findProductsByCategory(category: String) : Flux<Products>
}

@Repository
interface AddressesRepository: ReactiveMongoRepository<Addresses, Int>{
    fun findAddressesByUid(uid: String) : Flux<Addresses>
}

@Repository
interface AllUsersRepository: ReactiveMongoRepository<AllUsers, String>
