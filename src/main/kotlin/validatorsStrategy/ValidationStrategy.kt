package validatorsStrategy

interface ValidationStrategy <T : Any> {
    fun isValid(value: T?): Boolean
}