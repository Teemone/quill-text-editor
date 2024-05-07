import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yourandroidguy.quil.components.AnimatedGradientText
import com.yourandroidguy.quil.ui.theme.QuilTheme
import kotlinx.coroutines.launch

@Composable
fun TextWithScrollScale() {
    val text by remember { mutableStateOf("Sample Text") }
    val scrollState = rememberLazyListState()

    Column(Modifier.fillMaxSize()) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(100) {
                Text("Hello World $it")
            }
        }

        TextWithScaleOnScroll(
            text = text,
            scrollState = scrollState
        )

    }
}

@Composable
fun TextWithScaleOnScroll(modifier: Modifier= Modifier,text: String = "Quil", scrollState: LazyListState) {
    val currentPos = 0f
    val goalPos = 100f
    val maxScrollPosition = 1 // Adjust this based on your content length

    val scrollFraction by
        remember {
            derivedStateOf {
                (scrollState.firstVisibleItemIndex.toFloat()/ maxScrollPosition.toFloat()).coerceIn(0f, 1f) } }
    val pos = (currentPos + goalPos) * scrollFraction


    AnimatedGradientText(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                this.translationX = -(size.width) * scrollFraction
            }
    )
}

@Composable
fun ElementWithAnimatedHeight(
    state: LazyListState,
    modifier: Modifier=Modifier
) {
    val animatedHeight = remember { Animatable(initialValue = 100f) }
    val coroutineScope = rememberCoroutineScope()
    val key by remember {
        derivedStateOf { state.firstVisibleItemIndex }
    }

    LaunchedEffect(key) {
        val targetHeight = calculateHeight(state)
        coroutineScope.launch {
            animatedHeight.animateTo(targetHeight.toFloat(), animationSpec = tween(durationMillis = 100))
        }
    }

    AnimatedGradientText(text = "Quil", modifier = modifier.size(animatedHeight.value.dp))
}

fun calculateHeight(scrollState: LazyListState): Int {
    val initialHeight = 100 // Initial height of the element
    val maxScrollPosition = 1 // Adjust this based on your content length

    val scrollFraction =
        (scrollState.firstVisibleItemIndex.toFloat() / maxScrollPosition.toFloat()).coerceIn(0f, 1f)

    return (initialHeight - initialHeight * 0.3 * scrollFraction).toInt()
}

@Preview
@Composable
private fun SomePrev() {
    QuilTheme {
        TextWithScrollScale()
    }
}
