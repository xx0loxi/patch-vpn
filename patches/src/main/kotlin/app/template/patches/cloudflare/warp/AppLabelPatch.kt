package app.template.patches.cloudflare.warp

import app.morphe.patcher.patch.resourcePatch
import app.template.patches.shared.Constants.WARP_COMPATIBILITY
import org.w3c.dom.Element

@Suppress("unused")
val setAppLabelPatch = resourcePatch(
    name = "App label: 1.1.1.1",
    description = "Replaces the manifest's @string/four_ones label with the literal \"1.1.1.1\", " +
        "so Morphe Manager and package parsers show the app name instead of the raw resource reference.",
    default = true,
) {
    compatibleWith(WARP_COMPATIBILITY)

    execute {
        document("AndroidManifest.xml").use { document ->
            val application = document.getElementsByTagName("application").item(0) as? Element
                ?: return@use
            application.setAttribute("android:label", "1.1.1.1")
        }
    }
}
