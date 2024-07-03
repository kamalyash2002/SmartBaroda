package com.example.infuxion_alternate.utils

import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.ActivityResultLauncher
import kotlin.jvm.JvmOverloads
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.app.ActivityOptionsCompat

class BetterActivityResult<Input, Result> private constructor(
    caller : ActivityResultCaller,
    contract : ActivityResultContract<Input, Result>,
    var onActivityResult : ((Result) -> Unit)?,
) {

    private val launcher : ActivityResultLauncher<Input> =
        caller.registerForActivityResult(contract) { onActivityResult?.invoke(it) }

    /**
     * Launch activity, same as [ActivityResultLauncher.launch] except that it
     * allows a callback
     * executed after receiving a result from the target activity.
     */
    /**
     * Same as [.launch] with last parameter set to `null`.
     */
    @JvmOverloads
    fun launch(
        input : Input,
        animation: ActivityOptionsCompat?= null,
        onActivityResult : ((Result) -> Unit)? = this.onActivityResult,
    ) {
        this.onActivityResult = onActivityResult
        if (animation != null)
            launcher.launch(input,animation)
        else
            launcher.launch(input)
    }

    companion object {
        /**1
         * Register activity result using a [ActivityResultContract] and an in-place
         * activity result callback like
         * the default approach. You can still customise callback using [.launch].
         */
        fun <Input, Result> registerForActivityResult(
            caller : ActivityResultCaller,
            contract : ActivityResultContract<Input, Result>,
            onActivityResult : ((Result) -> Unit)?,
        ) : BetterActivityResult<Input, Result> {
            return BetterActivityResult(caller, contract, onActivityResult)
        }

        /**
         * Same as [.registerForActivityResult] except
         * the last argument is set to `null`.
         */
        fun <Input, Result> registerForActivityResult(
            caller : ActivityResultCaller,
            contract : ActivityResultContract<Input, Result>,
        ) : BetterActivityResult<Input, Result> {
            return registerForActivityResult(caller, contract, null)
        }

        /**
         * Specialised method for launching new activities.
         */
        @JvmStatic
        fun registerActivityForResult(
            caller : ActivityResultCaller,
        ) : BetterActivityResult<Intent, ActivityResult> {
            return registerForActivityResult(caller, StartActivityForResult())
        }
    }
}