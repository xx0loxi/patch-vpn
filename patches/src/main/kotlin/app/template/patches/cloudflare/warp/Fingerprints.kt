package app.template.patches.cloudflare.warp

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

/**
 * Primary constructor of the Moshi data class deserialised from the
 * "account_type" field of the Cloudflare /accounts response.
 */
object AccountDataConstructorFingerprint : Fingerprint(
    definingClass = "Lcom/cloudflare/app/data/warpapi/AccountData;",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR),
    returnType = "V",
    parameters = listOf(
        "Ljava/lang/String;",
        "Lcom/cloudflare/app/data/warpapi/WarpPlusState;",
        "Ljava/lang/Long;",
        "Ljava/lang/Long;",
        "Lorg/threeten/bp/LocalDateTime;",
        "Ljava/lang/Integer;",
        "Ljava/lang/String;",
        "Lcom/cloudflare/app/data/warpapi/Managed;",
        "Lcom/cloudflare/app/data/warpapi/AccountRole;",
    ),
)
