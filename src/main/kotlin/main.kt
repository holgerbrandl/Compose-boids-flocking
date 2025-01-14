import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

fun main()  =  application {
    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(width = 680.dp, height = 600.dp),
        title = "Compose-Ants-Debug",
        resizable = false,
    ) {
        MaterialTheme() {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                val scene = remember { Scene() }
                scene.setupScene()
                val frameState = stepFrame {
                    scene.update()
                }
                scene.render(frameState)
            }
        }
    }
}

class Scene {

    var sceneEntity = mutableStateListOf<SceneEntity>()

    var boids = mutableListOf<Boid>()

    fun setupScene() {
        sceneEntity.clear()

        repeat(200) { boids.add(Boid(it)) }

        sceneEntity.addAll(boids)
    }


    fun update() {
        for (entity in sceneEntity) {
            entity.update(this)
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun render(frameState: State<Long>) {
        var forces by remember {
            mutableStateOf(
                Forces(
                    randomFloat(0f, 10f),
                    randomFloat(0f, 10f),
                    randomFloat(0f, 10f),
                    randomFloat(50f, 75f)
                )
            )
        }
        val player by remember { mutableStateOf(vector(0f, 0f)) }

        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight()
        ) {
            Box {

                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Black)
                        .pointerMoveFilter(
                            onMove = { move ->
                                player.x = move.x
                                player.y = move.y
                                true
                            }
                        ),
                ) {
                    val step = frameState.value

                    drawPlayer(player)
                    for (boid in boids) {
                        if (boid.isConfigured) {
                            boid.applyNature(boids, forces, player)
                        }
                        drawBoid(boid)
                    }
                }

                Column(
                    modifier = Modifier.width(200.dp)
                        .padding(16.dp)
                        .offset(((720 + 200) / 2).dp)
                ) {


                    Text(text = "Ant Flocking System", color = Color.White, fontSize = 26.sp)
                    Spacer(Modifier.height(18.dp))

//                    Button(onClick = { /* Do something! */ }, colors = ButtonDefaults.textButtonColors(
//                        backgroundColor = Color.Red
//                    )) {
//                        Text("Button")
//                    }

                    Text(text = "Alignment \n [${forces.weightAlignment}]", color = Color.White)
                    Slider(
                        value = forces.weightAlignment,
                        valueRange = 0f..10f,
                        onValueChange = { forces = forces.copy(weightAlignment = it) },
                    )

//                    Spacer(Modifier.height(18.dp))
//                    var expanded by remember { mutableStateOf(false) }
//                    val items = listOf("A", "B", "C", "D", "E", "F")
//                    val disabledValue = "B"
//                    var selectedIndex by remember { mutableStateOf(0) }
//                    DropdownMenu(
//                        expanded = expanded,
//                        onDismissRequest = { expanded = false },
////                        modifier = Modifier.fillMaxWidth().background(
////                            Color.Red)
//                    ) {
//                        items.forEachIndexed { index, s ->
//                            DropdownMenuItem(onClick = {
//                                selectedIndex = index
//                                expanded = false
//                            }) {
//                                val disabledText = if (s == disabledValue) {
//                                    " (Disabled)"
//                                } else {
//                                    ""
//                                }
//                                Text(text = s + disabledText)
//                            }
//                        }
//                    }

                    Spacer(Modifier.height(18.dp))
                    Text(text = "Cohesion \n [${forces.weightCohesion}]", color = Color.White)
                    Slider(
                        value = forces.weightCohesion,
                        valueRange = 0f..10f,
                        onValueChange = { forces = forces.copy(weightCohesion = it) },
                    )

                    Spacer(Modifier.height(18.dp))
                    Text(text = "Separation \n [${forces.weightSeparation}]", color = Color.White)
                    Slider(
                        value = forces.weightSeparation,
                        valueRange = 0f..10f,
                        onValueChange = { forces = forces.copy(weightSeparation = it) },
                    )

                    Spacer(Modifier.height(18.dp))
                    Text(text = "Player Push \n [${forces.weightPush}]", color = Color.White)
                    Slider(
                        value = forces.weightPush,
                        valueRange = 50f..75f,
                        onValueChange = { forces = forces.copy(weightPush = it) },
                    )

                }
            }


        }


    }
}

fun Boolean.onFalse(action: () -> Unit) {
    if (!this) {
        action.invoke()
    }
}

fun Boolean.onTrue(action: () -> Unit) {
    if (this) {
        action.invoke()
    }
}

@Composable
fun stepFrame(callback: () -> Unit): State<Long> {
    val millis = remember { mutableStateOf(0L) }
    LaunchedEffect(Unit) {
        val startTime = withFrameMillis { it }
        while (true) {
            withFrameMillis { frameTime ->
                millis.value = frameTime - startTime
            }
            callback.invoke()
        }
    }
    return millis
}
