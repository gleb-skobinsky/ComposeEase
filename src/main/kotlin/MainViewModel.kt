import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Rect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Stable
class MainViewModel {
    private val _creativePanelHovered: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val creativePanelHovered: StateFlow<Boolean> = _creativePanelHovered
    fun setCreativePanelHovered(hovered: Boolean) {
        _creativePanelHovered.value = hovered
    }

    private val _creativePanelRect: MutableStateFlow<Rect?> = MutableStateFlow(null)
    val creativePanelRect: StateFlow<Rect?> = _creativePanelRect
    fun setCreativePanelRect(rect: Rect) {
        _creativePanelRect.value = rect
    }

    private val _generatedContent: MutableStateFlow<MutableList<@Composable () -> Unit>> = MutableStateFlow(mutableListOf())
    val generatedContent: StateFlow<List<@Composable () -> Unit>> = _generatedContent

    fun addContent(newContent: @Composable () -> Unit) {
        _generatedContent.value.add(newContent)
    }
}