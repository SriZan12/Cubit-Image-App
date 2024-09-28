package com.example.imageapp.util

import kotlin.random.Random

object NumberUtils {

    fun generateRandomLikesAndComments(): Pair<Int, Pair<Int, Int>> {
        val random = Random(System.currentTimeMillis()) // Initialize Random with current time
        val randomLikes = random.nextInt(1, 101)
        val randomDisLikes = random.nextInt(1, 101)
        val randomComment = random.nextInt(1, 101)
        return Pair(randomLikes, Pair(randomDisLikes, randomComment))
    }
}