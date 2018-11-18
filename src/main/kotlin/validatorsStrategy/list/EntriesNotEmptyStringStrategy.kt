package validatorsStrategy.list

import utils.getKType
import validatorsStrategy.StrategyBuilder
import validatorsStrategy.ValidationStrategy
import validatorsStrategy.string.ViolationsMessage

class EntriesNotEmptyStringStrategy(override val message: String) : ValidationStrategy<List<String>>, ViolationsMessage {

    @Target(AnnotationTarget.PROPERTY)
    annotation class EntriesNotEmptyString(val message: String = "")

    override fun isValid(value: List<String>?): Boolean {
        if (value == null)
            return false

        return value.all { it.isNotEmpty() }
    }

    companion object : StrategyBuilder<List<String>>{
     override val kType = getKType<List<String>>()

        override fun buildFromAnnotation(annotation: Annotation): ValidationStrategy<List<String>>? {
            return if (annotation is EntriesNotEmptyString) {
                return EntriesNotEmptyStringStrategy(annotation.message)
            } else null
        }
    }
}