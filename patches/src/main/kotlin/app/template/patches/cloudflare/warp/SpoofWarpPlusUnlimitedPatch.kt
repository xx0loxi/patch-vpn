package app.template.patches.cloudflare.warp

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.template.patches.shared.Constants.WARP_COMPATIBILITY

@Suppress("unused")
val spoofWarpPlusUnlimitedPatch = bytecodePatch(
    name = "Spoof WARP+ Unlimited UI",
    description = "Forces the account type to WARP+ UNLIMITED on every AccountData instance, " +
        "so the app displays the WARP+ Unlimited interface.",
    default = true,
) {
    compatibleWith(WARP_COMPATIBILITY)

    execute {
        AccountDataConstructorFingerprint.method.addInstructions(
            0,
            "sget-object p2, Lcom/cloudflare/app/data/warpapi/WarpPlusState;->UNLIMITED:Lcom/cloudflare/app/data/warpapi/WarpPlusState;",
        )
    }
}
