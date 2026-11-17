import Foundation

public enum UserRole: String, Codable, CaseIterable {
    case admin = "ADMIN"
    case employee = "EMPLOYEE"
    case user = "USER"
    case guest = "GUEST"
    
    public var title: String {
        switch self {
        case .admin: return "Admin"
        case .employee: return "Employee"
        case .user: return "User"
        case .guest: return "Guest"
        }
    }
}

public struct AppUser: Identifiable, Codable, Equatable {
    public var id: String
    public var name: String
    public var email: String
    public var password: String
    public var role: UserRole
    public var joinedDate: String
    public var workStatus: String // "డ్యూటీలో ఉన్నారు (On Duty)", "సెలవులో ఉన్నారు (On Leave)", "రిపోర్టింగ్ ఫీల్డ్‌లో (Field)"
    public var location: String
    public var referralCode: String
    public var rewardPoints: Int
    
    public init(
        id: String = UUID().uuidString,
        name: String,
        email: String,
        password: String = "",
        role: UserRole = .user,
        joinedDate: String = "2026-11-16",
        workStatus: String = "డ్యూటీలో ఉన్నారు (On Duty)",
        location: String = "పొన్నమండ, కోనసీమ",
        referralCode: String = "",
        rewardPoints: Int = 50
    ) {
        self.id = id
        self.name = name
        self.email = email
        self.password = password
        self.role = role
        self.joinedDate = joinedDate
        self.workStatus = workStatus
        self.location = location
        self.referralCode = referralCode.isEmpty ? "NAA-\(String(email.prefix(4)).uppercased())-\(Int.random(in: 1000...9999))" : referralCode
        self.rewardPoints = rewardPoints
    }
}
