package com.example.lemonadeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lemonadeapp.ui.theme.LemonadeAppTheme
import kotlin.random.Random

/*Lemonade App Kotlin Android Studio
Todo el código deberá estar en castellano.
La cantidad de veces que hay que dar al limón será un número par.
Después de darle al vaso vacío, aparecerá un estado intermedio: la imagen será un cuadrado blanco y el texto será ¿Volver a empezar?. 
Deberás utilizar un enum si necesitas hacer referencia a las distintas pantallas. */

enum class EstadoLimonada {
    LIMON,          // Estado inicial con limón
    EXPRIMIENDO,    // Exprimiendo el limón
    BEBIENDO,       // Bebiendo la limonada
    VASO_VACIO,     // Vaso vacío
    REINICIAR       // Estado intermedio para reiniciar
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LemonadeAppTheme {
                // Scaffold es un contenedor que proporciona una estructura básica para la pantalla
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // AppLimonada es el componente principal que representa la lógica de la aplicación
                    AppLimonada(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppLimonadaPreview() {
    LemonadeAppTheme {
        AppLimonada()
    }
}

@Composable
// AppLimonada es el componente principal que representa la lógica de la aplicación, 
// modificado por el parámetro modifier para que pueda ser utilizado en otras pantallas
fun AppLimonada(modifier: Modifier = Modifier) {
    // estadoActual es el estado actual de la aplicación, se inicializa en EstadoLimonada.LIMON
    var estadoActual by remember { mutableStateOf(EstadoLimonada.LIMON) }
    // exprimidosNecesarios es el número de veces que se necesita exprimir el limón para llegar al estado siguiente
    var exprimidosNecesarios by remember { mutableStateOf(generarNumeroParAleatorio()) }
    // exprimidosActuales es el número de veces que se ha exprimido el limón hasta el momento
    var exprimidosActuales by remember { mutableStateOf(0) }

    // Column es un contenedor que organiza sus hijos en una columna
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // when es una expresión que evalúa el estadoActual y devuelve un par de valores (imagen, descripción), 
        // se utiliza para determinar la imagen y la descripción que se mostrarán en función del estadoActual
        val (imagen, descripcion) = when (estadoActual) {
            // Si el estadoActual es EstadoLimonada.LIMON, se muestra la imagen y la descripción para el estado inicial,
            // Pair es una función que crea un par de valores, en este caso, un par de imagen y descripción
            EstadoLimonada.LIMON -> Pair(
                R.drawable.lemon_tree,
                "Toca el limonero para seleccionar un limón"
            )
            // Si el estadoActual es EstadoLimonada.EXPRIMIENDO, se muestra la imagen y la descripción para el estado de exprimiendo,
            EstadoLimonada.EXPRIMIENDO -> Pair(
                R.drawable.lemon_squeeze,
                "Toca el limón para exprimirlo"
            )
            // Si el estadoActual es EstadoLimonada.BEBIENDO, se muestra la imagen y la descripción para el estado de bebiendo,
            EstadoLimonada.BEBIENDO -> Pair(
                R.drawable.lemon_drink,
                "Toca para beber la limonada"
            )
            // Si el estadoActual es EstadoLimonada.VASO_VACIO, se muestra la imagen y la descripción para el estado de vaso vacío,
            EstadoLimonada.VASO_VACIO -> Pair(
                R.drawable.lemon_restart,
                "Toca el vaso vacío"
            )
            // Si el estadoActual es EstadoLimonada.REINICIAR, se muestra la imagen y la descripción para el estado de reiniciar,
            EstadoLimonada.REINICIAR -> Pair(
                R.drawable.emoji,
                "¿Volver a empezar?"
            )
        }

        // Surface es un contenedor que proporciona una superficie con bordes redondeados,
        // se utiliza para crear un botón que se puede pulsar
        Surface(
            // shape es el radio de los bordes redondeados de la superficie
            shape = RoundedCornerShape(16.dp),
            // modifier es un modificador que se aplica a la superficie, en este caso, se aplica un modificador de clickable
            modifier = Modifier
                .clickable {
                    // Cuando se hace clic en la superficie, se actualiza el estadoActual en función del estadoActual actual
                    when (estadoActual) {
                        // Si el estadoActual es EstadoLimonada.LIMON, se cambia al estado de exprimiendo
                        EstadoLimonada.LIMON -> estadoActual = EstadoLimonada.EXPRIMIENDO
                        // Si el estadoActual es EstadoLimonada.EXPRIMIENDO, se incrementa el número de exprimidosActuales
                        EstadoLimonada.EXPRIMIENDO -> {
                            exprimidosActuales++
                            // Si el número de exprimidosActuales es igual o mayor que el número de exprimidosNecesarios,
                            // se cambia al estado de bebiendo y se reinicia el número de exprimidosActuales
                            if (exprimidosActuales >= exprimidosNecesarios) {
                                estadoActual = EstadoLimonada.BEBIENDO
                                exprimidosActuales = 0
                            }
                        }
                        // Si el estadoActual es EstadoLimonada.BEBIENDO, se cambia al estado de vaso vacío
                        EstadoLimonada.BEBIENDO -> estadoActual = EstadoLimonada.VASO_VACIO
                        // Si el estadoActual es EstadoLimonada.VASO_VACIO, se cambia al estado de reiniciar
                        EstadoLimonada.VASO_VACIO -> estadoActual = EstadoLimonada.REINICIAR
                        // Si el estadoActual es EstadoLimonada.REINICIAR, se cambia al estado de limón y se genera un nuevo número par aleatorio para exprimidosNecesarios
                        EstadoLimonada.REINICIAR -> {
                            estadoActual = EstadoLimonada.LIMON
                            exprimidosNecesarios = generarNumeroParAleatorio()
                        }
                    }
                }
        ) {
            // Image es un componente que muestra una imagen, se utiliza para mostrar la imagen seleccionada
            Image(
                painter = painterResource(imagen),
                contentDescription = descripcion,
                modifier = Modifier.size(128.dp)
            )
        }

        // Spacer es un componente que proporciona espacio vertical entre los componentes,
        // se utiliza para separar el texto de la imagen
        Spacer(modifier = Modifier.height(16.dp))
        // Text es un componente que muestra un texto, se utiliza para mostrar la descripción seleccionada
        Text(text = descripcion)
    }
}
// generarNumeroParAleatorio es una función que genera un número par aleatorio entre 2 y 8
private fun generarNumeroParAleatorio(): Int {
    // Genera un número par aleatorio entre 2 y 8
    // Random.nextInt(4) genera un número aleatorio entre 0 y 3
    // + 1 se suma 1 para que el número aleatorio esté entre 1 y 4
    // * 2 se multiplica por 2 para que el número aleatorio esté entre 2 y 8
    return (Random.nextInt(4) + 1) * 2
}
