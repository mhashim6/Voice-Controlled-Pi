/**
 *@author mhashim6 on 09/01/19
 */

import com.pi4j.io.gpio.*
import kotlinx.coroutines.*

private val gpio: GpioController = GpioFactory.getInstance()
val pins = mapOf<String, GpioPinDigitalOutput>(
    "1" to gpio.provisionDigitalOutputPin(RaspiPin.GPIO_15, PinState.LOW).apply { properties["color"] = "yellow" },
    "2" to gpio.provisionDigitalOutputPin(RaspiPin.GPIO_16, PinState.LOW).apply { properties["color"] = "yellow" },
    "3" to gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW).apply { properties["color"] = "red" },
    "4" to gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW).apply { properties["color"] = "red" },
    "5" to gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, PinState.LOW).apply { properties["color"] = "green" },
    "6" to gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, PinState.LOW).apply { properties["color"] = "green" },
    "7" to gpio.provisionDigitalOutputPin(RaspiPin.GPIO_10, PinState.LOW).apply { properties["color"] = "yellow" },
    "8" to gpio.provisionDigitalOutputPin(RaspiPin.GPIO_11, PinState.LOW).apply { properties["color"] = "yellow" },
    "9" to gpio.provisionDigitalOutputPin(RaspiPin.GPIO_26, PinState.LOW).apply { properties["color"] = "red" },
    "10" to gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, PinState.LOW).apply { properties["color"] = "red" }
)

fun state(pin: String): String {
    if (pins.containsKey(pin).not())
        return PinState.LOW.toString()

    return pins[pin]?.state.toString()
}

fun toggle(pin: String) {
    currentDance?.cancel()
    pins[pin]?.toggle()
}

fun toggleColor(color: String) {
    currentDance?.cancel()
    pins.values.filter { it.properties["color"] == color }.forEach { it.toggle() }
}

fun stateAll() = pins.map { it.key to it.value.state.toString() }
//fun stateAll() = pins.values.map { it.state.toString() }

fun all() {
    currentDance?.cancel()
    pins.values.forEach { it.high() }
}

fun none() {
    currentDance?.cancel()
    turnAllOff()
}

fun turnAllOff() {
    pins.values.forEach { it.low() }
}

var currentDance: Job? = null
val background = CoroutineScope(Dispatchers.Default)

val dances = mapOf(
    "0" to suspend {
        turnAllOff()
        while (true)
            pins.values.forEach { it.high(); delay(200); it.low(); delay(200) }
    },
    "1" to suspend {
        turnAllOff()
        while (true)
            (1..11).forEach { leftLed ->
                val rightLed = (11 - leftLed).toString()
                pins[leftLed.toString()]?.high()
                pins[rightLed]?.high()
                delay(200)
                pins[leftLed.toString()]?.low()
                pins[rightLed]?.low()
                delay(200)
            }
    }
)

fun dance(type: String) {
    currentDance?.cancel()
    currentDance = background.async {
        dances[type]?.invoke()
    }
}
