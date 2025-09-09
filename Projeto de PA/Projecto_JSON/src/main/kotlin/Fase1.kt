abstract class JSONElement() {
    abstract fun accept(visitor: Visitor)
    abstract fun serialize(): String
}

// Verificar se o tipo de dados é válido

fun ValidType(dataType: Any?): Boolean {
    var valid = false
    if(dataType is Int) {
        valid = true
    }
    if(dataType is Double) {
        valid = true
    }
    if(dataType is Boolean) {
        valid = true
    }
    if(dataType is String) {
        valid = true
    }
    if(dataType is JSONArray) {
        valid = true
    }
    if(dataType is JSONObject) {
        valid = true
    }
    return valid
}

// Classe que cria um array em que se pode adicionar e remover elementos (mas só se pode adicionar se o tipo for válido)

class JSONArray: JSONElement() {

    val arraylist = mutableListOf<Any?>()

    fun addElement(ele: Any) {
        if (ValidType(ele)) {
            arraylist.add(ele)
        }
    }

    fun removeElement(ele: Any) {
        arraylist.remove(ele)
    }

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun serialize(): String {
        val AllValues = this.arraylist.joinToString {
            parsing(it)
        }
        return "{$AllValues}"
    }
}

// Classe que contem dois parametros: uma String e um dataType que pode ser qualquer tipo, e esta classe é usada na classe JSONObject

class JSONData(var name: String, dataType: Any?): JSONElement() {

    var value: Any? = null

    init {
        if(ValidType(dataType)) {
            value = dataType
        }
        if(dataType is Collection<*>) {
            val array = JSONArray()
            dataType.forEach {
                if(it != null) {
                    array.addElement(it)
                } else {
                    value = array
                }
            }
        }
    }

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun serialize(): String {
        return "$(this.name) : $(parsing(this.value))"
    }
}

// Classe que adiciona e remove dados da classe JSONData

@Suppress("MISSING_DEPENDENCY_SUPERCLASS_IN_TYPE_ARGUMENT")
class JSONObject: JSONElement() {
    val jsondata : MutableList<JSONData> = mutableListOf()

    fun addElement(data: JSONData) {
        jsondata.add(data)
    }

    fun removeElement(data: JSONData) {
        jsondata.remove(data)
    }

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
        jsondata.forEach {
            it.accept(visitor)
        }
        visitor.endVisit()
    }

    override fun serialize(): String {
        var parsingString = "{\n     "
        parsingString += this.jsondata.joinToString(separator = ",\n     ") {
            (if (it is JSONData) {
                it.name + " : " + parsing(it.value)
            } else {
            }).toString()
        }
        return parsingString + "\n}"
    }

    fun getnumber() : MutableList<String> {
        val number = GetNumber()
        this.accept(number)
        return number.numberfound
    }

    fun getnamenumber() : MutableList<String> {
        val namenumber = GetObjectsNameNumber()
        this.accept(namenumber)
        return namenumber.namenumberfound
    }

    fun getinscritosstructure() : MutableList<String> {
        val inscritosstructure = GetInscritosStructure()
        this.accept(inscritosstructure)
        return inscritosstructure.inscritossctructure
    }

}

fun parsing(value: Any?): String {
    if(value is JSONObject) {
        var parsingString = "{\n             "
        parsingString += value.jsondata.joinToString(separator = ",\n             ") {
            (if (it is JSONData) {
                it.name + " : " + parsing(it.value)
            } else {
            }).toString()
        }
        return parsingString + "         \n         }"
    }
    if(value is JSONArray) {
        var parsingString = "[\n         "
        parsingString += value.arraylist.joinToString(separator = ",\n         ") {
            parsing(it)
        }
        return parsingString + "\n     ]"
    }
    return "$value"
}

class GetNumber : Visitor {
    var numberfound = mutableListOf<String>()
    override fun visit(jo: JSONObject) {
        jo.jsondata.forEach {
            if(it.name == "numero") {
                val string = parsing(it.value)
                val number = string.toInt()
                if(number is Int) {
                    numberfound.add(parsing(it.value))
                }
            }
        }
    }

    override fun visit(ja: JSONArray) {
    }

    override fun visit(jd: JSONData) {
    }

    override fun endVisit() {
    }
}

class GetObjectsNameNumber : Visitor {
    var namenumberfound = mutableListOf<String>()
    var String = ""
    override fun visit(jo: JSONObject) {
        jo.jsondata.forEach {
            if(it.name == "numero" || it.name == "nome") {
                String = it.name + " : " + parsing(it.value)
                namenumberfound.add(String)
            }
        }
    }

    override fun visit(ja: JSONArray) {
    }

    override fun visit(jd: JSONData) {
    }

    override fun endVisit() {
    }
}

class GetInscritosStructure : Visitor {
    var inscritossctructure = UniqueArrayList<String>(3)
    var String = ""
    override fun visit(jo: JSONObject) {
        jo.jsondata.forEach {
            if(it.name == "numero" || it.name == "nome" || it.name == "internacional") {
                String = it.name + " : " + parsing(it.value)
                inscritossctructure.add(String)
            }
        }
    }

    override fun visit(ja: JSONArray) {
    }

    override fun visit(jd: JSONData) {
    }

    override fun endVisit() {
    }
}

class UniqueArrayList<String>(private val maxLenght: Int) : ArrayList<String>() {
    override fun add(element: String): Boolean {
        if(size < maxLenght && !contains(element)) {
            return super.add(element)
        }
        return false
    }
}

interface Visitor {
    fun visit(ja: JSONArray)
    fun visit(jd: JSONData)
    fun visit(jo: JSONObject)
    fun endVisit()
}

fun main() {
    val teste1 = JSONData("uc", "PA")
    val teste2 = JSONData("etcs", "6.0")
    val teste3 = JSONData("exame", "null")
    val teste4 = JSONData("numero", "101101")
    val teste5 = JSONData("nome", "Dave Farley")
    val teste6 = JSONData("internacional", "true")
    val teste7 = JSONData("numero", "101102")
    val teste8 = JSONData("nome", "Martin Fowler")
    val teste9 = JSONData("internacional", "true")
    val teste10 = JSONData("numero", "26502")
    val teste11 = JSONData("nome", "Andre")
    val teste12 = JSONData("internacional", "false")

    val jo = JSONObject()
    jo.addElement(teste4)
    jo.addElement(teste5)
    jo.addElement(teste6)
    jo.removeElement(teste6)
    jo.addElement(teste6)
    println(jo.serialize())
    println(jo.getnumber())
    println(jo.getnamenumber())
    println(jo.getinscritosstructure())
    val jo1 = JSONObject()
    jo1.addElement(teste7)
    jo1.addElement(teste8)
    jo1.addElement(teste9)
    val jo2 = JSONObject()
    jo2.addElement(teste10)
    jo2.addElement(teste11)
    jo2.addElement(teste12)
    val ja = JSONArray()
    ja.addElement(jo)
    ja.addElement(jo1)
    ja.addElement(jo2)
    val ja1 = JSONArray()
    ja1.addElement("MEI")
    ja1.addElement("MIG")
    ja1.addElement("METI")
    ja1.removeElement("METI")
    ja1.addElement("METI")

    val teste13 = JSONData("inscritos", ja)

    val jo3 = JSONObject()
    jo3.addElement(teste1)
    jo3.addElement(teste2)
    jo3.addElement(teste3)
    jo3.addElement(teste13)

    val teste14 = JSONData("cursos", ja1)

    jo3.addElement(teste14)

    println(jo3.serialize())
}

