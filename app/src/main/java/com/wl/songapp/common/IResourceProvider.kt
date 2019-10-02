package com.wl.songapp.common

interface IResourceProvider {
    //others not needed in this example
    fun getString(resourceId: Int): String
}