package validatorsStrategy.string

import utils.getKType
import validatorsStrategy.StrategyBuilder
import validatorsStrategy.ValidationStrategy

class NotBlankStrategy : ValidationStrategy<String> {
    @Target(AnnotationTarget.PROPERTY)
    annotation class NotBlank(val message: String = "")

    override fun isValid(value: String?): Boolean {
        return value?.isNotBlank() ?: false
    }

    companion object Builder: StrategyBuilder<String> {
        override val kType = getKType<String>()

        override fun buildFromAnnotation(annotation: Annotation): ValidationStrategy<String>? {
            return if (annotation is NotBlank) {
                NotBlankStrategy()
            } else null
        }
    }
}