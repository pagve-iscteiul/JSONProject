import org.junit.jupiter.api.Test
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.test.assertEquals

class Fase1Testes {

    @Test
    fun GetNumbers() {
        val teste1 = JSONData("numero", "82799")
        val teste2 = JSONData("nome", "Pedro")
        val teste3 = JSONData("numero", "112233")
        val teste4 = JSONData("numero", "370162")
        val jo = JSONObject()
        jo.addElement(teste1)
        jo.addElement(teste2)
        jo.addElement(teste3)
        jo.addElement(teste4)
        val number = jo.getnumber()
        //assertEquals("112233", number[2])
        assertEquals("370162", number[2])
    }

    @Test
    fun GetJSONObjectsNameNumber() {
        val teste1 = JSONData("numero", "82799")
        val teste2 = JSONData("nome", "Pedro")
        val teste3 = JSONData("internacional", "false")
        val teste4 = JSONData("numero", "38461")
        val teste5 = JSONData("nome", "João")
        val teste6 = JSONData("internacional", "true")
        val jo = JSONObject()
        jo.addElement(teste1)
        jo.addElement(teste2)
        jo.addElement(teste3)
        jo.addElement(teste4)
        jo.addElement(teste5)
        jo.addElement(teste6)
        val namenumber = jo.getnamenumber()
        assertEquals("numero : 82799", namenumber[0])
        //assertEquals("internacional : false", namenumber[2])
    }
}

class Fase2Testes {
    data class Inscrito(val numero: Int, val nome: String, val internacional: Boolean)
    @Test
    fun testJSONWITHDATACLASS() {
        val inscrito = Inscrito(82799, "Pedro", false)
        val a = JSONWITHDATACLASS(inscrito)
        val expected = """
            {
                 Inscrito : {
                         numero : 82799,
                         nome : Pedro,
                         internacional : false         
                     }
            }""".trimIndent()
        assertEquals(expected, a.serialize())
    }

    @Test
    fun testJSONWITHCOLLECTION() {
        val inscrito = Inscrito(82799, "Pedro", false)
        val collection = listOf(
            JSONData("numero", inscrito.numero),
            JSONData("nome", inscrito.nome),
            JSONData("internacional", inscrito.internacional)
        )
        val result = JSONWITHCOLLECTION(collection)
        val expected = """
            {
                 numero : 82799,
                 nome : Pedro,
                 internacional : false
            }""".trimIndent()
        assertEquals(expected, result.serialize())
    }

    @Test
    fun testJSONWITHMAP() {
        val inscrito = Inscrito(82799, "Pedro", false)
        val map = mapOf("numero" to inscrito.numero, "nome" to inscrito.nome, "internacional" to inscrito.internacional)
        val result = JSONWITHMAP(map)
        val expected = """
            {
                 numero : 82799,
                 nome : Pedro,
                 internacional : false
            }""".trimIndent()
        assertEquals(expected, result.serialize())
    }

    @Test
    fun testJSONWITHPRIMITIVETYPES() {
        val inscrito = Inscrito(82799, "Pedro", false)
        val result = JSONWITHPRIMITIVETYPES("numero", inscrito.numero)
        val expected = """
            {
                 numero : 82799
            }""".trimIndent()
        assertEquals(expected, result.serialize())
    }

    @Test
    fun testJSONWITHENUMERATION() {
        val inscrito = Inscrito(82799, "Pedro", false)
        val result = JSONWITHENUMERATION(inscrito)
        val expected = """
            {
                 Inscrito : {
                         numero : 82799,
                         nome : Pedro,
                         internacional : false         
                     }
            }""".trimIndent()
        assertEquals(expected, result.serialize())
    }
}