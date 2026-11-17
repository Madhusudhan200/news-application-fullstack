package com.naatvtelugu.news.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

enum class UserRole {
    ADMIN,
    EMPLOYEE,
    USER,
    GUEST,
}

data class AppUser(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val email: String,
    val password: String = "",
    val role: UserRole = UserRole.USER,
    val joinedDate: String = "2026-11-16",
    val workStatus: String = "Active", // "Active", "On Leave", "Inactive"
    val location: String = "ఆంధ్రప్రదేశ్",
)

class AuthStore(context: Context) {
    private val prefs = context.getSharedPreferences("auth_store", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val userListType = object : TypeToken<List<AppUser>>() {}.type

    private val _currentUser = MutableStateFlow<AppUser?>(loadCurrentUser())
    val currentUser: StateFlow<AppUser?> = _currentUser.asStateFlow()

    private val _allUsers = MutableStateFlow<List<AppUser>>(loadAllUsers())
    val allUsers: StateFlow<List<AppUser>> = _allUsers.asStateFlow()

    init {
        // Ensure default admin and demo reporter exist
        if (_allUsers.value.none { it.role == UserRole.ADMIN }) {
            val defaultAdmin = AppUser(
                name = "Admin (NAA TV)",
                email = "admin@naatvtelugu.com",
                password = "admin123",
                role = UserRole.ADMIN,
                joinedDate = "2026-01-10",
                workStatus = "Active",
                location = "హైదరాబాద్",
            )
            val defaultReporter = AppUser(
                name = "Reporter Ravi",
                email = "reporter@naatvtelugu.com",
                password = "reporter123",
                role = UserRole.EMPLOYEE,
                joinedDate = "2026-03-15",
                workStatus = "Active",
                location = "విజయవాడ",
            )
            val demoUser = AppUser(
                name = "Suresh Kumar",
                email = "suresh@naatvtelugu.com",
                password = "user123",
                role = UserRole.USER,
                joinedDate = "2026-06-20",
                workStatus = "Active",
                location = "పొన్నమండ",
            )
            saveUsers(listOf(defaultAdmin, defaultReporter, demoUser) + _allUsers.value)
        }
    }

    private fun loadCurrentUser(): AppUser? {
        val json = prefs.getString("current_user", null) ?: return null
        return try {
            gson.fromJson(json, AppUser::class.java)
        } catch (e: Exception) {
            null
        }
    }

    private fun loadAllUsers(): List<AppUser> {
        val json = prefs.getString("users_list", null) ?: return emptyList()
        return try {
            gson.fromJson<List<AppUser>>(json, userListType) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun saveUsers(users: List<AppUser>) {
        _allUsers.value = users
        prefs.edit().putString("users_list", gson.toJson(users)).apply()
    }

    private fun setCurrentUser(user: AppUser?) {
        _currentUser.value = user
        if (user == null) {
            prefs.edit().remove("current_user").apply()
        } else {
            prefs.edit().putString("current_user", gson.toJson(user)).apply()
        }
    }

    fun login(email: String, pass: String): Result<AppUser> {
        val cleanEmail = email.trim().lowercase()
        val user = _allUsers.value.firstOrNull {
            it.email.lowercase() == cleanEmail && it.password == pass
        }
        return if (user != null) {
            setCurrentUser(user)
            Result.success(user)
        } else {
            Result.failure(Exception("ఇమెయిల్ లేదా పాస్‌వర్డ్ సరైనది కాదు (Invalid email or password)"))
        }
    }

    fun signUp(name: String, email: String, pass: String): Result<AppUser> {
        val cleanEmail = email.trim().lowercase()
        val cleanName = name.trim()
        if (cleanName.isBlank() || cleanEmail.isBlank() || pass.length < 4) {
            return Result.failure(Exception("దయచేసి సరైన వివరాలు నమోదు చేయండి (కనీసం 4 అక్షరాల పాస్‌వర్డ్)"))
        }
        if (_allUsers.value.any { it.email.lowercase() == cleanEmail }) {
            return Result.failure(Exception("ఈ ఇమెయిల్‌తో ఇప్పటికే ఖాతా ఉంది (Account already exists)"))
        }
        val newUser = AppUser(
            name = cleanName,
            email = cleanEmail,
            password = pass,
            role = UserRole.USER,
            joinedDate = "2026-11-16",
            workStatus = "Active",
            location = "ఆంధ్రప్రదేశ్",
        )
        val updated = _allUsers.value + newUser
        saveUsers(updated)
        setCurrentUser(newUser)
        return Result.success(newUser)
    }

    fun addEmployee(name: String, email: String, pass: String, location: String = "ఆంధ్రప్రదేశ్"): Result<AppUser> {
        val cleanEmail = email.trim().lowercase()
        val cleanName = name.trim()
        if (cleanName.isBlank() || cleanEmail.isBlank() || pass.length < 4) {
            return Result.failure(Exception("సరైన పేరు, ఇమెయిల్ మరియు పాస్‌వర్డ్ ఇవ్వండి"))
        }
        if (_allUsers.value.any { it.email.lowercase() == cleanEmail }) {
            return Result.failure(Exception("ఈ ఇమెయిల్‌తో ఇప్పటికే ఖాతా ఉంది"))
        }
        val newEmployee = AppUser(
            name = cleanName,
            email = cleanEmail,
            password = pass,
            role = UserRole.EMPLOYEE,
            joinedDate = "2026-11-16",
            workStatus = "Active",
            location = location,
        )
        saveUsers(_allUsers.value + newEmployee)
        return Result.success(newEmployee)
    }

    fun updateWorkStatus(email: String, newStatus: String) {
        val updated = _allUsers.value.map {
            if (it.email.equals(email, ignoreCase = true)) it.copy(workStatus = newStatus) else it
        }
        saveUsers(updated)
        if (_currentUser.value?.email?.equals(email, ignoreCase = true) == true) {
            _currentUser.value = _currentUser.value?.copy(workStatus = newStatus)
        }
    }

    fun updateUserRole(email: String, newRole: UserRole) {
        val updated = _allUsers.value.map {
            if (it.email.equals(email, ignoreCase = true)) it.copy(role = newRole) else it
        }
        saveUsers(updated)
        if (_currentUser.value?.email?.equals(email, ignoreCase = true) == true) {
            _currentUser.value = _currentUser.value?.copy(role = newRole)
        }
    }

    fun loginAsGuest(): AppUser {
        val guest = AppUser(
            name = "అతిథి (Guest)",
            email = "guest@naatvtelugu.com",
            role = UserRole.GUEST,
        )
        setCurrentUser(guest)
        return guest
    }

    fun logout() {
        setCurrentUser(null)
    }

    fun getEmployees(): List<AppUser> =
        _allUsers.value.filter { it.role == UserRole.EMPLOYEE }
}
