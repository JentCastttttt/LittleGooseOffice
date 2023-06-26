package little.goose.memorial.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import little.goose.memorial.data.entities.Memorial
import little.goose.memorial.ui.component.MemorialColumn
import little.goose.memorial.ui.component.MemorialColumnState
import little.goose.memorial.ui.component.MemorialTitle

@Composable
fun MemorialHome(
    modifier: Modifier,
    topMemorial: Memorial?,
    memorialColumnState: MemorialColumnState,
    onNavigateToMemorialShow: (Long) -> Unit,
) {
    MemorialScreen(
        modifier = modifier,
        memorialColumnState = memorialColumnState,
        topMemorial = topMemorial,
        onMemorialEdit = { memorial ->
            memorial.id?.run(onNavigateToMemorialShow)
        }
    )
}

@Composable
private fun MemorialScreen(
    modifier: Modifier,
    memorialColumnState: MemorialColumnState,
    topMemorial: Memorial?,
    onMemorialEdit: (Memorial) -> Unit
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (topMemorial != null) {
                MemorialTitle(
                    modifier = Modifier
                        .height(130.dp)
                        .fillMaxWidth(),
                    memorial = topMemorial
                )
            }
            MemorialColumn(
                modifier = Modifier
                    .weight(1F)
                    .fillMaxWidth(),
                state = memorialColumnState,
                onMemorialEdit = onMemorialEdit
            )
        }
    }
}