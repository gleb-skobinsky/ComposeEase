import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import material3.ApplicationTheme

@Composable
@Preview
fun App(viewModel: MainViewModel) {
    val creativePanelHovered by viewModel.creativePanelHovered.collectAsState()
    val creativePanelRect by viewModel.creativePanelRect.collectAsState()
    val generatedContent by viewModel.generatedContent.collectAsState()
    ApplicationTheme {
        Row(Modifier.fillMaxSize()) {
            // Left panel: for creating stuff
            Column(
                Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .weight(7f)
                    .fillMaxHeight()
            ) {
                val density = LocalDensity.current
                val dashedColor = MaterialTheme.colorScheme.onPrimary
                Column(
                    Modifier
                        .padding(32.dp)
                        .drawDashedBorder(density, dashedColor)
                        .background(if (creativePanelHovered) MaterialTheme.colorScheme.secondary else Color.Transparent)
                        .fillMaxSize()
                        .onGloballyPositioned { layoutCoordinates ->
                            viewModel.setCreativePanelRect(layoutCoordinates.boundsInRoot())
                        }
                ) {
                    for (composable in generatedContent) {
                        composable()
                    }
                }
            }
            // Right panel for widgets
            Column(
                Modifier
                    .fillMaxHeight()
                    .shadow(12.dp)
                    .background(Color.White)
                    .weight(3f)
                    .padding(12.dp)
            ) {
                var buttonCoordX by remember { mutableStateOf(0f) }
                var buttonCoordY by remember { mutableStateOf(0f) }
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        MaterialTheme.colorScheme.primary
                    ),
                    elevation = ButtonDefaults.buttonElevation(4.dp, 4.dp, 4.dp, 4.dp, 4.dp),
                    modifier = Modifier
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    val truePositionX = creativePanelRect!!.size.width + 64.dp.toPx() + offset.x
                                    buttonCoordX = truePositionX
                                    buttonCoordY = offset.y
                                },
                                onDragEnd = {
                                    if (creativePanelHovered) {
                                        viewModel.addContent {
                                            BasicTextField(
                                                value = TextFieldValue(text = "Text Field"),
                                                onValueChange = {},
                                                modifier = Modifier.border(1.dp, Color.Red)
                                            )
                                        }
                                    }
                                    viewModel.setCreativePanelHovered(false)
                                },
                                onDragCancel = {
                                    if (creativePanelHovered) {
                                        viewModel.addContent {
                                            BasicTextField(
                                                value = TextFieldValue(text = "Text Field"),
                                                onValueChange = {},
                                                modifier = Modifier.border(1.dp, Color.Red)
                                            )
                                        }
                                    }
                                    viewModel.setCreativePanelHovered(false)
                                },
                                onDrag = { _, offset ->
                                    buttonCoordX += offset.x
                                    buttonCoordY += offset.y
                                    creativePanelRect?.let {
                                        val leftCondition = buttonCoordX > it.left && buttonCoordX < (it.left + it.size.width)
                                        val rightCondition = buttonCoordY > it.top && buttonCoordY < (it.top + it.size.height)
                                        val debugBool = leftCondition && rightCondition
                                        viewModel.setCreativePanelHovered(debugBool)
                                    }
                                }
                            )
                        }
                ) {
                    Text("Text field")
                }
            }
        }
    }
}

fun Modifier.drawDashedBorder(density: Density, color: Color) = this.drawBehind {
    with(density) {
        drawRect(
            color = color,
            style = Stroke(
                width = 2.dp.toPx(),
                pathEffect = PathEffect
                    .dashPathEffect(
                        floatArrayOf(
                            2.dp.toPx(),
                            2.dp.toPx()
                        ),
                        0f
                    )
            )
        )
    }
}

fun main() = application {
    Window(
        title = "ComposeEase",
        onCloseRequest = ::exitApplication
    ) {
        val viewModel = MainViewModel()
        App(viewModel)
    }
}
