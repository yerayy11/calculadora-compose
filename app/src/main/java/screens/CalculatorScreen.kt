package screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.CalculatorButton
import components.CalculatorDisplay

@Composable
fun CalculatorScreen() {

    val pantalla = remember {
        mutableStateOf("0")
    }

    val numero1 = remember {
        mutableStateOf(0.0)
    }

    val operador = remember {
        mutableStateOf("")
    }

    val esperandoSegundoNumero = remember {
        mutableStateOf(false)
    }

    val mostrarNuevoNumero = remember {
        mutableStateOf(false)
    }

    val historial = remember {
        mutableStateOf(listOf<String>())
    }

    val expresion = remember {
        mutableStateOf("")
    }

    fun agregarCaracter(caracter: String) {

        if (mostrarNuevoNumero.value) {

            pantalla.value = caracter
            expresion.value = caracter

            mostrarNuevoNumero.value = false

        } else if (esperandoSegundoNumero.value) {

            pantalla.value = caracter
            expresion.value += caracter

            esperandoSegundoNumero.value = false

        } else {

            if (pantalla.value == "0") {

                pantalla.value = caracter

            } else {

                pantalla.value += caracter

            }
            expresion.value += caracter
        }
    }

    fun guardarOperacion(signo: String) {

        numero1.value = pantalla.value.toDouble()

        operador.value = signo

        expresion.value += signo

        esperandoSegundoNumero.value = true
    }

    fun calcularResultado() {

        if (operador.value.isEmpty()) {
            return
        }

        if (pantalla.value == "Error") {
            return
        }

        val numero2 = pantalla.value.toDoubleOrNull()

        if (numero2 == null) {
            pantalla.value = "Error"
            return
        }

        val resultado = when (operador.value) {

                "+" -> numero1.value + numero2

                "-" -> numero1.value - numero2

                "*" -> numero1.value * numero2

                "/" -> {

                if (numero2 == 0.0) {

                    expresion.value = "Error"
                    pantalla.value = "Error"

                    mostrarNuevoNumero.value = true

                    return
                }

                numero1.value / numero2
            }

            else -> 0.0
        }

        historial.value =
            historial.value +
                    "${numero1.value} ${operador.value} $numero2 = $resultado"

        if (resultado % 1 == 0.0) {

            pantalla.value = resultado.toInt().toString()

        } else {

            pantalla.value = resultado.toString()

        }

        expresion.value = pantalla.value
        mostrarNuevoNumero.value = true
        operador.value = ""
    }

    fun limpiar() {

        pantalla.value = "0"

        expresion.value = ""

        numero1.value = 0.0

        operador.value = ""

        esperandoSegundoNumero.value = false

        mostrarNuevoNumero.value = false
    }

    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),

            verticalArrangement = Arrangement.spacedBy(8.dp),

            horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Historial"
        )

        historial.value.takeLast(5).forEach {

            Text(it)

        }

        CalculatorDisplay(
            text = expresion.value.ifEmpty { "0" }
        )

        Row {

            CalculatorButton("7") {
                agregarCaracter("7")
            }

            CalculatorButton("8") {
                agregarCaracter("8")
            }

            CalculatorButton("9") {
                agregarCaracter("9")
            }

            CalculatorButton("/") {
                guardarOperacion("/")
            }
        }

        Row {

            CalculatorButton("4") {
                agregarCaracter("4")
            }

            CalculatorButton("5") {
                agregarCaracter("5")
            }

            CalculatorButton("6") {
                agregarCaracter("6")
            }

            CalculatorButton("*") {
                guardarOperacion("*")
            }
        }

        Row {

            CalculatorButton("1") {
                agregarCaracter("1")
            }

            CalculatorButton("2") {
                agregarCaracter("2")
            }

            CalculatorButton("3") {
                agregarCaracter("3")
            }

            CalculatorButton("-") {
                guardarOperacion("-")
            }
        }

        Row {

            CalculatorButton("0") {
                agregarCaracter("0")
            }

            CalculatorButton("C") {
                limpiar()
            }

            CalculatorButton("=") {
                calcularResultado()
            }

            CalculatorButton("+") {
                guardarOperacion("+")
            }
        }
    }
}