package core

import validatorsStrategy.StrategyBuilder
import validatorsStrategy.ValidationStrategy
import validatorsStrategy.string.ViolationsMessage

class PropertyValidator <T : Any> (
        private val property: T,
        private val propertyName: String,
        private val builders: List<StrategyBuilder<in T>>
) {
    fun executeValidations(annotations: List<Annotation>): MutableList<ValidationViolated> {
        val violations = mutableListOf<ValidationViolated>()

        annotations.forEach { annotation: Annotation ->
            builders.forEach {
                val strategy = it.buildFromAnnotation(annotation)

                if (strategy != null) {
                    if (strategy.isValid(property).not()) {
                        val violation = ValidationViolated(strategy as ValidationStrategy<*>, annotation)

                        if (strategy is ViolationsMessage) {
                            violation.message = strategy.message
                        }

                        violations += violation
                    }
                }
            }
        }

        return violations
    }
}