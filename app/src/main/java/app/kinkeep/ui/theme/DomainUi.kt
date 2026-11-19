package app.kinkeep.ui.theme

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bedtime
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FiberNew
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.TrendingDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import app.kinkeep.R
import app.kinkeep.domain.model.Circle
import app.kinkeep.domain.model.HealthCategory

@Composable
fun HealthCategory.color(): Color {
    val colors = LocalHealthColors.current
    return when (this) {
        HealthCategory.NEW -> colors.fresh
        HealthCategory.THRIVING -> colors.thriving
        HealthCategory.HEALTHY -> colors.healthy
        HealthCategory.NEEDS_ATTENTION -> colors.needsAttention
        HealthCategory.FADING -> colors.fading
        HealthCategory.DORMANT -> colors.dormant
    }
}

fun HealthCategory.icon(): ImageVector = when (this) {
    HealthCategory.NEW -> Icons.Rounded.FiberNew
    HealthCategory.THRIVING -> Icons.Rounded.Favorite
    HealthCategory.HEALTHY -> Icons.Rounded.CheckCircle
    HealthCategory.NEEDS_ATTENTION -> Icons.Rounded.Schedule
    HealthCategory.FADING -> Icons.Rounded.TrendingDown
    HealthCategory.DORMANT -> Icons.Rounded.Bedtime
}

@StringRes
fun HealthCategory.labelRes(): Int = when (this) {
    HealthCategory.NEW -> R.string.health_new
    HealthCategory.THRIVING -> R.string.health_thriving
    HealthCategory.HEALTHY -> R.string.health_healthy
    HealthCategory.NEEDS_ATTENTION -> R.string.health_needs_attention
    HealthCategory.FADING -> R.string.health_fading
    HealthCategory.DORMANT -> R.string.health_dormant
}

@StringRes
fun HealthCategory.descriptionRes(): Int = when (this) {
    HealthCategory.NEW -> R.string.health_new_desc
    HealthCategory.THRIVING -> R.string.health_thriving_desc
    HealthCategory.HEALTHY -> R.string.health_healthy_desc
    HealthCategory.NEEDS_ATTENTION -> R.string.health_needs_attention_desc
    HealthCategory.FADING -> R.string.health_fading_desc
    HealthCategory.DORMANT -> R.string.health_dormant_desc
}

@StringRes
fun Circle.labelRes(): Int = when (this) {
    Circle.INNER -> R.string.circle_inner
    Circle.CLOSE -> R.string.circle_close
    Circle.FRIENDS -> R.string.circle_friends
    Circle.ACQUAINTANCES -> R.string.circle_acquaintances
    Circle.IGNORE -> R.string.circle_ignore
}
