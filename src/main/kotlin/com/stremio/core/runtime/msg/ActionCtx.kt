package com.stremio.core.runtime.msg

import com.stremio.core.types.api.AuthRequest
import com.stremio.core.types.profile.Profile
import com.stremio.core.types.resource.MetaItem

sealed class ActionCtx {
    data class Authenticate(val args: AuthRequest) : ActionCtx()
    class Logout : ActionCtx()
    data class UpdateSettings(val args: Profile.Settings) : ActionCtx()
    data class AddToLibrary(val args: MetaItem) : ActionCtx()
    data class RemoveFromLibrary(val args: String) : ActionCtx()
    data class RewindLibraryItem(val args: String) : ActionCtx()
    class PushUserToAPI : ActionCtx()
    class PullUserFromAPI : ActionCtx()
    class PushAddonsToAPI : ActionCtx()
    class PullAddonsFromAPI : ActionCtx()
    class SyncLibraryWithAPI : ActionCtx()

}
