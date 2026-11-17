package com.naatvtelugu.news.data

import android.content.Context
import androidx.compose.ui.text.font.FontFamily
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class EditedStory(
    val id: Int,
    val title: String,
    val summary: String,
    val category: String,
    val contentHtml: String = "",
)

data class AdminSettings(
    val headlineFontSize: Int = 18,
    val bodyFontSize: Int = 15,
    val fontFamilyChoice: String = "Default",
    val logoHeightDp: Int = 28,
    val customLogoUrl: String = "",
    val imageRatioPercent: Int = 42,
    val guestModeEnabled: Boolean = true,
    val guestCanBookmark: Boolean = true,
    val guestCanShare: Boolean = true,
    val employeeRequireApproval: Boolean = true,
    val employeeAllowBreaking: Boolean = true,
)

class AdminSettingsStore(context: Context) {
    private val prefs = context.getSharedPreferences("admin_settings", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val editedMapType = object : TypeToken<Map<Int, EditedStory>>() {}.type

    private val _settings = MutableStateFlow(loadSettings())
    val settings: StateFlow<AdminSettings> = _settings.asStateFlow()

    private val _editedStories = MutableStateFlow<Map<Int, EditedStory>>(loadEditedStories())
    val editedStories: StateFlow<Map<Int, EditedStory>> = _editedStories.asStateFlow()

    private fun loadSettings(): AdminSettings {
        return AdminSettings(
            headlineFontSize = prefs.getInt("headline_font_size", 18),
            bodyFontSize = prefs.getInt("body_font_size", 15),
            fontFamilyChoice = prefs.getString("font_family_choice", "Default") ?: "Default",
            logoHeightDp = prefs.getInt("logo_height_dp", 28),
            customLogoUrl = prefs.getString("custom_logo_url", "") ?: "",
            imageRatioPercent = prefs.getInt("image_ratio_percent", 42),
            guestModeEnabled = prefs.getBoolean("guest_mode_enabled", true),
            guestCanBookmark = prefs.getBoolean("guest_can_bookmark", true),
            guestCanShare = prefs.getBoolean("guest_can_share", true),
            employeeRequireApproval = prefs.getBoolean("employee_require_approval", true),
            employeeAllowBreaking = prefs.getBoolean("employee_allow_breaking", true),
        )
    }

    private fun loadEditedStories(): Map<Int, EditedStory> {
        val json = prefs.getString("edited_stories", null) ?: return emptyMap()
        return try {
            gson.fromJson(json, editedMapType) ?: emptyMap()
        } catch (e: Exception) {
            emptyMap()
        }
    }

    fun updateSettings(newSettings: AdminSettings) {
        _settings.value = newSettings
        prefs.edit()
            .putInt("headline_font_size", newSettings.headlineFontSize)
            .putInt("body_font_size", newSettings.bodyFontSize)
            .putString("font_family_choice", newSettings.fontFamilyChoice)
            .putInt("logo_height_dp", newSettings.logoHeightDp)
            .putString("custom_logo_url", newSettings.customLogoUrl)
            .putInt("image_ratio_percent", newSettings.imageRatioPercent)
            .putBoolean("guest_mode_enabled", newSettings.guestModeEnabled)
            .putBoolean("guest_can_bookmark", newSettings.guestCanBookmark)
            .putBoolean("guest_can_share", newSettings.guestCanShare)
            .putBoolean("employee_require_approval", newSettings.employeeRequireApproval)
            .putBoolean("employee_allow_breaking", newSettings.employeeAllowBreaking)
            .apply()
    }

    fun saveEditedStory(edited: EditedStory) {
        val current = _editedStories.value.toMutableMap()
        current[edited.id] = edited
        _editedStories.value = current
        prefs.edit().putString("edited_stories", gson.toJson(current)).apply()
    }

    fun getFontFamily(): FontFamily {
        return when (_settings.value.fontFamilyChoice) {
            "Serif" -> FontFamily.Serif
            "Sans" -> FontFamily.SansSerif
            "Monospace" -> FontFamily.Monospace
            else -> FontFamily.Default
        }
    }
}
