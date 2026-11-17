import Foundation

public class AuthStore: ObservableObject {
    private let userDefaultsKey = "naa_tv_current_user"
    private let allUsersKey = "naa_tv_all_users"
    
    @Published public var currentUser: AppUser?
    @Published public var allUsers: [AppUser] = []
    
    public init() {
        self.allUsers = loadAllUsers()
        if allUsers.isEmpty {
            let defaultAdmin = AppUser(
                name: "Admin (NAA TV)",
                email: "admin@naatvtelugu.com",
                password: "admin123",
                role: .admin,
                joinedDate: "2026-01-10",
                workStatus: "డ్యూటీలో ఉన్నారు (On Duty)",
                location: "హైదరాబాద్"
            )
            let defaultReporter = AppUser(
                name: "Reporter Ravi",
                email: "reporter@naatvtelugu.com",
                password: "reporter123",
                role: .employee,
                joinedDate: "2026-03-15",
                workStatus: "రిపోర్టింగ్ ఫీల్డ్‌లో (Field)",
                location: "విజయవాడ"
            )
            let demoUser = AppUser(
                name: "Suresh Kumar",
                email: "suresh@naatvtelugu.com",
                password: "user123",
                role: .user,
                joinedDate: "2026-06-20",
                workStatus: "డ్యూటీలో ఉన్నారు (On Duty)",
                location: "పొన్నమండ"
            )
            self.allUsers = [defaultAdmin, defaultReporter, demoUser]
            saveAllUsers(self.allUsers)
        }
        self.currentUser = loadCurrentUser() ?? self.allUsers.first(where: { $0.role == .admin })
    }
    
    private func loadCurrentUser() -> AppUser? {
        guard let data = UserDefaults.standard.data(forKey: userDefaultsKey) else { return nil }
        return try? JSONDecoder().decode(AppUser.self, from: data)
    }
    
    private func saveCurrentUser(_ user: AppUser?) {
        if let user = user, let data = try? JSONEncoder().encode(user) {
            UserDefaults.standard.set(data, forKey: userDefaultsKey)
        } else {
            UserDefaults.standard.removeObject(forKey: userDefaultsKey)
        }
    }
    
    private func loadAllUsers() -> [AppUser] {
        guard let data = UserDefaults.standard.data(forKey: allUsersKey) else { return [] }
        return (try? JSONDecoder().decode([AppUser].self, from: data)) ?? []
    }
    
    private func saveAllUsers(_ users: [AppUser]) {
        if let data = try? JSONEncoder().encode(users) {
            UserDefaults.standard.set(data, forKey: allUsersKey)
        }
    }
    
    public func login(email: String, pass: String) -> Result<AppUser, Error> {
        let cleanEmail = email.trimmingCharacters(in: .whitespacesAndNewlines).lowercased()
        if let user = allUsers.first(where: { $0.email.lowercased() == cleanEmail && $0.password == pass }) {
            self.currentUser = user
            saveCurrentUser(user)
            return .success(user)
        } else {
            return .failure(NSError(domain: "Auth", code: 401, userInfo: [NSLocalizedDescriptionKey: "ఇమెయిల్ లేదా పాస్‌వర్డ్ సరైనది కాదు (Invalid credentials)"]))
        }
    }
    
    public func register(name: String, email: String, pass: String, role: UserRole) -> Result<AppUser, Error> {
        let cleanEmail = email.trimmingCharacters(in: .whitespacesAndNewlines).lowercased()
        if allUsers.contains(where: { $0.email.lowercased() == cleanEmail }) {
            return .failure(NSError(domain: "Auth", code: 409, userInfo: [NSLocalizedDescriptionKey: "ఈ ఇమెయిల్ ముందే నమోదు చేయబడింది (Email already exists)"]))
        }
        let newUser = AppUser(name: name, email: cleanEmail, password: pass, role: role)
        allUsers.append(newUser)
        saveAllUsers(allUsers)
        self.currentUser = newUser
        saveCurrentUser(newUser)
        return .success(newUser)
    }
    
    public func guestLogin() -> AppUser {
        let guest = AppUser(name: "Guest", email: "guest@naatvtelugu.com", role: .guest)
        self.currentUser = guest
        saveCurrentUser(guest)
        return guest
    }
    
    public func logout() {
        self.currentUser = nil
        saveCurrentUser(nil)
    }
    
    public func updateWorkStatus(email: String, newStatus: String) {
        if let idx = allUsers.firstIndex(where: { $0.email.lowercased() == email.lowercased() }) {
            allUsers[idx].workStatus = newStatus
            saveAllUsers(allUsers)
            if currentUser?.email.lowercased() == email.lowercased() {
                currentUser?.workStatus = newStatus
                saveCurrentUser(currentUser)
            }
        }
    }
    
    public func updateRole(email: String, newRole: UserRole) {
        if let idx = allUsers.firstIndex(where: { $0.email.lowercased() == email.lowercased() }) {
            allUsers[idx].role = newRole
            saveAllUsers(allUsers)
            if currentUser?.email.lowercased() == email.lowercased() {
                currentUser?.role = newRole
                saveCurrentUser(currentUser)
            }
        }
    }
}
