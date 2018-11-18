package core

import validatorsStrategy.ValidationStrategy

class ConstraintViolations {
    var violations = mutableListOf<ConstraintViolation>()

    operator fun get(propertyName: String): ConstraintViolation? {
        return violations.singleOrNull { it.propertyName == propertyName }
    }

    operator fun set(propertyName: String, validationViolated: ValidationViolated) {
        if (violations.any { it.propertyName == propertyName }) {
            violations.add(ConstraintViolation(propertyName, mutableListOf()))
        }

        violations.single {
            it.propertyName == propertyName
        }.apply {
            validationStrategies += validationViolated
        }
    }

    operator fun set(propertyName: String, validationsViolated: List<ValidationViolated>) {
        if (violations.any { it.propertyName == propertyName }.not()) {
            violations.add(ConstraintViolation(propertyName, mutableListOf()))
        }

        violations.single {
            it.propertyName == propertyName
        }.apply {
            validationStrategies += validationsViolated
        }
    }
}

class ConstraintViolation(
        val propertyName: String,
        val validationStrategies: MutableList<ValidationViolated>
)

class ValidationViolated(
        val strategy: ValidationStrategy<*>,
        val annotation: Annotation,
        var message: String = "Message not provided"
) {
    val strategyName = strategy.javaClass.name
}
