package validatorsStrategy.string
import utils.getKType
import validatorsStrategy.StrategyBuilder
import validatorsStrategy.ValidationStrategy

interface ViolationsMessage {
    val message: String
}

class NotEmptyStrategy(override val message: String) : ValidationStrategy<String>, ViolationsMessage {

    @Target(AnnotationTarget.PROPERTY)
    annotation class NotEmpty(val message: String = "")

    override fun isValid(value: String?): Boolean {
        return value?.isNotEmpty() ?: false
    }

    companion object Builder: StrategyBuilder<String>  {
        override val kType = getKType<String>()

        override fun buildFromAnnotation(annotation: Annotation): ValidationStrategy<String>? {
            return if (annotation is NotEmpty) {
                NotEmptyStrategy(annotation.message)
            } else null
        }
    }
}