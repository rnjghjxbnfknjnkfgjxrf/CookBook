package ru.ac.uniyar.handlers

import org.http4k.lens.Lens
import org.http4k.lens.LensFailure

fun <IN : Any, OUT>lensOrNull(lens: Lens<IN, OUT?>, value : IN): OUT?{
    return try {
        lens.invoke(value)
    }catch (_: LensFailure){
        null
    }
}

fun <IN : Any, OUT>lensOrDefault(lens: Lens<IN, OUT?>, value : IN, default : OUT): OUT{
    return try {
        lens.invoke(value) ?: default
    }catch (_: LensFailure){
        default
    }
}
