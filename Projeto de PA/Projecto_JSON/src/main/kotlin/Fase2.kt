import kotlin.reflect.KClass
import kotlin.reflect.full.*

@Target(AnnotationTarget.PROPERTY)
annotation class EXCLUDE

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class INSCRITO(val name: String)

@INSCRITO("Aluno")
data class Inscrito(
    @INSCRITO("ID")
    val numero: Int,
    val nome: String,
    @EXCLUDE
    val internacional: Boolean)

fun JSONWITHCOLLECTION(collection: List<Any>): JSONObject {
    val jsonobject = JSONObject()
    collection.forEach { element ->
        when(element) {
            is JSONData -> jsonobject.addElement(element)
            else -> throw IllegalArgumentException("Not correct type")
        }
    }
    return jsonobject
}

fun JSONWITHMAP(map: Map<*,*>): JSONObject {
    val jsonobject = JSONObject()
    map.forEach {
        jsonobject.addElement(JSONData(it.key.toString(), it.value))
    }
    return jsonobject
}

fun JSONWITHPRIMITIVETYPES(key: String, value: Any?): JSONObject {
    val jsonobject = JSONObject()
    jsonobject.addElement(JSONData(key, value))
    return jsonobject
}

fun JSONWITHDATACLASS(dataclass: Any): JSONObject {
    val c = dataclass::class as KClass<Any>
    val cname = c.simpleName!!
    val v = c.declaredMemberProperties
    val propertyOrder = listOf("numero", "nome", "internacional")
    val jsonobject = JSONObject()
    v.sortedBy { propertyOrder.indexOf(it.name) }.forEach { property ->
        if(!property.hasAnnotation<EXCLUDE>()) {
            if (property.hasAnnotation<INSCRITO>()) {
                val name = property.findAnnotation<INSCRITO>()!!.name
                jsonobject.addElement(JSONData(name, property.get(dataclass)))
            } else {
                jsonobject.addElement(JSONData(property.name, property.get(dataclass)))
            }
        }
    }
    val jsonobject2 = JSONObject()
    jsonobject2.addElement(JSONData(cname, jsonobject))
    return jsonobject2
}

interface PropertyEnum {
    val displayName : String
}

object NUMERO: PropertyEnum {
    override val displayName: String = "numero"
}

object NOME: PropertyEnum {
    override val displayName: String = "nome"
}

object INTERNACIONAL: PropertyEnum {
    override val displayName: String = "internacional"
}

fun JSONWITHENUMERATION(dataclass: Any): JSONObject {
    val c = dataclass::class as KClass<Any>
    val propertyOrder = listOf("numero", "nome", "internacional")
    val jsonobject = JSONObject()

    val v = c.declaredMemberProperties
            .sortedBy { propertyOrder.indexOf(it.name) }
            .zip(listOf(NUMERO, NOME, INTERNACIONAL))

    v.forEach { (property, enumeration) ->
        val propertyName = if(property.name == "nome") enumeration.displayName else property.name
        if(!property.hasAnnotation<EXCLUDE>()) {
            if (property.hasAnnotation<INSCRITO>()) {
                val name = property.findAnnotation<INSCRITO>()!!.name
                jsonobject.addElement(JSONData(name, property.get(dataclass)))
            } else {
                jsonobject.addElement(JSONData(propertyName, property.get(dataclass)))
            }
        }
    }
    val jsonobject2 = JSONObject()
    jsonobject2.addElement(JSONData(c.simpleName!!, jsonobject))
    return jsonobject2
}

fun main() {
    val inscrito = Inscrito(82799, "Pedro", false)
    val collection = listOf(
            JSONData("numero", inscrito.numero),
            JSONData("nome", inscrito.nome),
            JSONData("internacional", inscrito.internacional)
    )
    val jawithcollection = JSONWITHCOLLECTION(collection)
    println(jawithcollection.serialize())

    val map = mapOf("numero" to inscrito.numero, "nome" to inscrito.nome, "internacional" to inscrito.internacional)
    val jowithmap = JSONWITHMAP(map)
    println(jowithmap.serialize())

    val primitive1 = JSONWITHPRIMITIVETYPES("numero", inscrito.numero)
    val primitive2 = JSONWITHPRIMITIVETYPES("nome", inscrito.nome)
    val primitive3 = JSONWITHPRIMITIVETYPES("internacional", inscrito.internacional)
    println(primitive1.serialize())
    println(primitive2.serialize())
    println(primitive3.serialize())

    val jowithdataclass = JSONWITHDATACLASS(inscrito)
    println(jowithdataclass.serialize())

    val jowithenumeration = JSONWITHENUMERATION(inscrito)
    println(jowithenumeration.serialize())
}

