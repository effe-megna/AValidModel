package core

import validatorsStrategy.StrategyBuilder
import validatorsStrategy.integer.BetweenStrategy
import validatorsStrategy.integer.LessThanStrategy
import validatorsStrategy.list.EntriesNotEmptyStringStrategy
import validatorsStrategy.string.MinEightCharactersStrategy
import validatorsStrategy.string.NotBlankStrategy
import validatorsStrategy.string.NotEmptyStrategy
import kotlin.reflect.full.*

interface INotifier {
    fun validationSuccess(propertyName: String, annotations: List<Annotation>)

    fun validationFail(propertyName: String, violations: List<ValidationViolated>)

    fun stopValidation():Boolean
}

class Validator <T : Any>(
        private val model: T,
        private val notifier: INotifier? = null
) {
    private val constraintViolations = ConstraintViolations()

    val strategyBuilders: MutableList<StrategyBuilder<out Any>> = mutableListOf(
            NotEmptyStrategy,
            NotBlankStrategy,
            MinEightCharactersStrategy,
            LessThanStrategy,
            BetweenStrategy,
            EntriesNotEmptyStringStrategy
    )

    val violations = constraintViolations.violations

    var isValidModel: Boolean = false
        get() = violations.isEmpty()
        private set

    fun fifoValidation() {
        model::class.memberProperties.forEachIndexed { index, kProperty ->

            val name = kProperty.name
            val annotations = kProperty.annotations

            if (annotations.isEmpty()) return

            val value = readProperty<Any>(model, name)

            @Suppress("UNCHECKED_CAST")
            val buildersBasedOnKClass = strategyBuilders.filter {
                kProperty.returnType.isSubtypeOf(it.kType)
            } as List<StrategyBuilder<in Any>>

            val validationsViolated = PropertyValidator(
                    value,
                    name,
                    buildersBasedOnKClass
            ).executeValidations(model::class.memberProperties.elementAt(index).annotations)

            constraintViolations[kProperty.name] = validationsViolated

            notifier?.run {
                if (validationsViolated.isEmpty()) {
                    validationSuccess(name, annotations)
                } else {
                    validationFail(name, validationsViolated)
                }

                if (notifier.stopValidation()) return
            }
        }
    }

    companion object {
        fun <T : Any> validate(
                model: T,
                notifier: INotifier? = null
        ): Validator<T> {
            return Validator(model, notifier).apply {
                fifoValidation()
            }
        }

        fun <R: Any?> readProperty(
                instance: Any,
                propertyName: String
        ): R {
            val clazz = instance.javaClass.kotlin
            @Suppress("UNCHECKED_CAST")
            return clazz.declaredMemberProperties.first {
                it.name == propertyName
            }.get(instance) as R
        }
    }
}