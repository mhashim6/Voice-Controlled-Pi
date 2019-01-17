/**
 *@author mhashim6 on 09/01/19
 */

import com.pi4j.io.gpio.*

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

fun status(pin: String): String {
    if (pins.containsKey(pin).not())
        return PinState.LOW.toString()

    return pins[pin]?.state.toString()
}

fun toggle(pin: String) {
    pins[pin]?.toggle()
}

fun toggleColor(color: String) {
    pins.values.filter { it.properties["color"] == color }.forEach { it.toggle() }
}

fun statusAll() = pins.map { it.key to it.value.state.toString() }
//fun statusAll() = pins.values.map { it.state.toString() }

fun all() {
    pins.values.forEach { it.high() }
}

fun none() {
    pins.values.forEach { it.low() }
}