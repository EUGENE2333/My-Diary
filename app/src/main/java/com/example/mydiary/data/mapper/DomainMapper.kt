package com.example.mydiary.data.mapper

interface DomainMapper<Entity,DomainModel>{
    fun mapToDomain(entity:git cmmit Entity): DomainModel
    fun mapFromDomain(domainModel: DomainModel): Entity
}