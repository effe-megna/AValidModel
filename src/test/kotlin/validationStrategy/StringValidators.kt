package validationStrategy

import org.junit.jupiter.api.Test
import validatorsStrategy.string.NotBlankStrategy
import validatorsStrategy.string.NotEmptyStrategy
import kotlin.reflect.full.memberProperties

///// MEMOIZATION

class ModelNotEmpty(foo: String = "") {
    @NotEmptyStrategy.NotEmpty
    val foo = foo
}

class ModelNotBlank(foo: String = " ") {
    @NotBlankStrategy.NotBlank
    val foo = foo
}

class StringValidators {

    @Test
    fun ontEmptyString() {
        val annotation = ModelNotEmpty::class.memberProperties.find { it.name == "foo" }!!.annotations.first()

        val strategy = NotEmptyStrategy.buildFromAnnotation(annotation)

        assert(strategy!!.isValid(ModelNotEmpty().foo).not())
    }

    @Test
    fun onNotEmptyString() {
        val annotation = ModelNotEmpty::class.memberProperties.find { it.name == "foo" }!!.annotations.first()

        val strategy = NotEmptyStrategy.buildFromAnnotation(annotation)

        assert(strategy!!.isValid(ModelNotEmpty("foo").foo))
    }

    @Test
    fun onBlankString() {
        val annotation = ModelNotBlank::class.memberProperties.find { it.name == "foo" }!!.annotations.first()

        val strategy = NotBlankStrategy.buildFromAnnotation(annotation)

        assert(strategy!!.isValid(ModelNotBlank().foo).not())
    }

    @Test
    fun onNotBlankString() {
        val annotation = ModelNotBlank::class.memberProperties.find { it.name == "foo" }!!.annotations.first()

        val strategy = NotBlankStrategy.buildFromAnnotation(annotation)

        assert(strategy!!.isValid(ModelNotBlank("foo").foo))
    }

}