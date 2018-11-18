package validatorsStrategy.string

import utils.getKType
import validatorsStrategy.StrategyBuilder
import validatorsStrategy.ValidationStrategy

class MinEightCharactersStrategy : ValidationStrategy<String> {
    @Target(AnnotationTarget.PROPERTY)
    annotation class MinEightCharacters(val message: String = "")

    override fun isValid(value: String?): Boolean {
        if (value == null)
            return false

        return value.count() < 7
    }

    companion object Builder : StrategyBuilder<String> {
        override val kType = getKType<String>()

        override fun buildFromAnnotation(annotation: Annotation): ValidationStrategy<String>? {
            return if (annotation is MinEightCharacters) {
                MinEightCharactersStrategy()
            } else null
        }
    }
}