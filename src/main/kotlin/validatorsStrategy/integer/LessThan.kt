package validatorsStrategy.integer

import utils.getKType
import validatorsStrategy.StrategyBuilder
import validatorsStrategy.ValidationStrategy

class LessThanStrategy(private val dependency: Int) : ValidationStrategy<Int>{

    @Target(AnnotationTarget.PROPERTY)
    annotation class LessThan(val dependency: Int, val message: String = "")

    override fun isValid(value: Int?): Boolean {
        if (value == null)
            return false

        return value < dependency
    }

    companion object Builder : StrategyBuilder<Int> {
        override val kType = getKType<Int>()

        override fun buildFromAnnotation(annotation: Annotation): ValidationStrategy<Int>? {
            return if (annotation is LessThan) {
                LessThanStrategy(annotation.dependency)
            } else null
        }
    }
}