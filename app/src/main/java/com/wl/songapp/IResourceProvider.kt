package com.wl.songapp

interface IResourceProvider {
    //others not needed in this example
    fun getString(resourceId: Int): String
}