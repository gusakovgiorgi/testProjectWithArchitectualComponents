package com.gusakov.frogogo.repostiory.mapper

interface Mapper<I, O> {
    fun map(input: I): O
}