package com.wl.songapp.common

import android.content.Context
import android.content.ContextWrapper

class AppContext(context: Context) : ContextWrapper(context),
    IResourceProvider