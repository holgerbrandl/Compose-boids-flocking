import kotlin.math.pow
import kotlin.math.sqrt

data class Vector(
    var x: Float,
    var y: Float,
    var z: Float,
) {
    var magnitude: Float = 0.0f
        get() = sqrt(x.pow(2) + y.pow(2) + z.pow(2))
        private set

    fun normalize() {
        if (magnitude != 0f) {
            this / magnitude
        }
    }

    fun limit(maxMagnitude: Float) {
        if (magnitude > maxMagnitude) {
            setMagnitude(maxMagnitude)
        }
    }

    fun setMagnitude(times: Float) {
        normalize()
        this * times
    }


    operator fun plus(that: Vector) {
        this.x += that.x
        this.y += that.y
        this.z += that.z
    }

    operator fun minus(that: Vector) {
        this.x -= that.x
        this.y -= that.y
        this.z -= that.z
    }

    operator fun times(value: Float) {
        this.x *= value
        this.y *= value
        this.z *= value
    }

    operator fun div(value: Float) {
        if (value > 0) {
            this.x /= value
            this.y /= value
            this.z /= value
        }
    }
}

fun vector(x: Float = 0f, y: Float = 0f, z: Float = 0f) = Vector(x, y, z)

// random 2D unit vector
fun vectorRandom2D(): Vector {
    val vector = vector(randomFloat(randomSign = true), randomFloat(randomSign = true), 0f)
    vector.normalize()
    return vector
}