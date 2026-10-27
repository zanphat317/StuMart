package com.example.stumart.data.supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    private const val SUPABASE_URL = "https://uxxovmvbcqlqhviuqond.supabase.co"
    private const val SUPABASE_KEY = "sb_publishable_vgko5qZHJ_At325-H7D_sA_Sr2MxIhl"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Auth)
        install(Postgrest)
    }
}
