package com.example.mydiary.data.mapper

interface RemoteMapper<Remote, Local>{
    fun mapFromRemote(remote:Remote): Local
    fun mapToRemote(local:Local): Remote
}