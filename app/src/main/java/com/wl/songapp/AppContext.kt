package com.wl.songapp

import android.content.Context
import android.content.ContextWrapper

class AppContext(context: Context) : ContextWrapper(context), IResourceProvider