package validatorsStrategy.integer

import utils.getKType
import validatorsStrategy.StrategyBuilder
import validatorsStrategy.ValidationStrategy
import validatorsStrategy.string.ViolationsMessage

class BetweenStrategy(
        private val lower: Int,
        private val greater: Int,
        override val message: String
) : ValidationStrategy<Int>, ViolationsMessage {

    @Target(AnnotationTarget.PROPERTY)
    annotation class Between(val lower: Int, val greater: Int, val message: String = "")

    override fun isValid(value: Int?): Boolean {
        if (value == null)
            return false

        return value in lower..greater
    }

    companion object Builder : StrategyBuilder<Int> {
        override val kType = getKType<Int>()

        override fun buildFromAnnotation(annotation: Annotation): ValidationStrategy<Int>? {
            return if (annotation is Between) {
                BetweenStrategy(annotation.lower, annotation.greater, annotation.message)
            } else null
        }
    }
}