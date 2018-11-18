package validatorsStrategy

import kotlin.reflect.KType

interface StrategyBuilder <T : Any> {
    val kType: KType
    fun buildFromAnnotation(annotation: Annotation): ValidationStrategy<T>?
}

