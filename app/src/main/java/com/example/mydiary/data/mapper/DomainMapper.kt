package com.example.mydiary.data.mapper

interface DomainMapper<Entity,DomainModel>{
    fun mapToDomain(entity: Entity): DomainModel
    fun mapFromDomain(domainModel: DomainModel): Entity
}