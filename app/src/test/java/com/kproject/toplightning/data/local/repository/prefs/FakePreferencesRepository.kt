package com.kproject.toplightning.data.local.repository.prefs

class FakePreferencesRepository : PreferenceRepository {

    override fun <T> getPreference(key: String, defaultValue: T): T {
        return defaultValue
    }

    override fun <T> savePreference(key: String, value: T) {}
}