import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * To support instant preview (replacement for android's @Preview annotation)
 */

fun <T> withRandomSign(action: (sign: Int) -> T): T {
    val sign = if (Random.nextInt() % 2 == 0) 1 else -1
    return action.invoke(sign)
}

fun randomFloat(from: Float, to: Float, randomSign: Boolean = false): Float {
    return withRandomSign { Random.nextDouble(from.toDouble(), to.toDouble()).toFloat() * if (randomSign) it else 1 }
}

fun randomFloat(value: Float = 0f, randomSign: Boolean = false): Float = when (value) {
    0f -> withRandomSign { Random.nextFloat() * if (randomSign) it else 1 }
    else -> withRandomSign { Random.nextDouble(value.toDouble()).toFloat() * if (randomSign) it else 1 }
}

fun distance(x1: Float, x2: Float, y1: Float, y2: Float): Float = sqrt((y2 - y1).pow(2) + (x2 - x1).pow(2))
