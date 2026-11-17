import SwiftUI

public struct MoreProfileView: View {
    @ObservedObject var vm: NewsViewModel
    public var onNavigateNews: () -> Void
    public var onNavigateAdmin: () -> Void
    public var onNavigateEmployee: () -> Void
    public var onNavigateSaved: () -> Void
    
    @State private var showLanguageDialog: Bool = false
    @State private var showLocationDialog: Bool = false
    @State private var showReferralDialog: Bool = false
    
    public init(
        viewModel: NewsViewModel,
        onOpenCategories: @escaping () -> Void,
        onOpenLanguageDialog: @escaping () -> Void,
        onOpenLocationPicker: @escaping () -> Void,
        onOpenReferral: @escaping () -> Void,
        onOpenAdminPanel: @escaping () -> Void,
        onOpenEmployeeDesk: @escaping () -> Void,
        onOpenSavedStories: @escaping () -> Void,
        onOpenAuth: @escaping () -> Void,
        onSelectCategory: @escaping (Int) -> Void
    ) {
        self.vm = viewModel
        self.onNavigateNews = { onSelectCategory(0) }
        self.onNavigateAdmin = onOpenAdminPanel
        self.onNavigateEmployee = onOpenEmployeeDesk
        self.onNavigateSaved = onOpenSavedStories
    }
    
    public init(
        vm: NewsViewModel,
        onNavigateNews: @escaping () -> Void,
        onNavigateAdmin: @escaping () -> Void,
        onNavigateEmployee: @escaping () -> Void,
        onNavigateSaved: @escaping () -> Void
    ) {
        self.vm = vm
        self.onNavigateNews = onNavigateNews
        self.onNavigateAdmin = onNavigateAdmin
        self.onNavigateEmployee = onNavigateEmployee
        self.onNavigateSaved = onNavigateSaved
    }
    
    public var body: some View {
        ZStack {
            Color.screenBg(dark: vm.darkMode)
                .ignoresSafeArea()
            
            VStack(spacing: 0) {
                // Top Bar (Matches Image 3)
                HStack {
                    Button(action: {}) {
                        Image(systemName: "gearshape")
                            .font(.system(size: 20))
                            .foregroundColor(Color.textPrimary(dark: vm.darkMode))
                    }
                    
                    Button(action: {}) {
                        Image(systemName: "magnifyingglass")
                            .font(.system(size: 20))
                            .foregroundColor(Color.textPrimary(dark: vm.darkMode))
                    }
                    .padding(.leading, 12)
                    
                    Spacer()
                    
                    Button(action: onNavigateNews) {
                        HStack(spacing: 4) {
                            Text("న్యూస్")
                                .font(.system(size: 15, weight: .bold))
                            Image(systemName: "arrow.right")
                                .font(.system(size: 13, weight: .bold))
                        }
                        .foregroundColor(Color.naaGold)
                    }
                }
                .padding(.horizontal, 18)
                .padding(.vertical, 12)
                .background(Color.surface(dark: vm.darkMode))
                
                ScrollView {
                    VStack(spacing: 16) {
                        // 1. User Profile Card (Matches Image 3)
                        HStack(spacing: 14) {
                            // Avatar Circle
                            ZStack {
                                Circle()
                                    .fill(Color.textSecondary(dark: vm.darkMode).opacity(0.15))
                                    .frame(width: 52, height: 52)
                                Image(systemName: "person.fill")
                                    .font(.system(size: 24))
                                    .foregroundColor(Color.textSecondary(dark: vm.darkMode))
                            }
                            
                            VStack(alignment: .leading, spacing: 3) {
                                Text(vm.authStore.currentUser?.name ?? "Guest")
                                    .font(.system(size: 17, weight: .bold))
                                    .foregroundColor(Color.textPrimary(dark: vm.darkMode))
                                
                                Text(vm.authStore.currentUser?.email ?? "Click here to select email id")
                                    .font(.system(size: 12))
                                    .foregroundColor(Color.textSecondary(dark: vm.darkMode))
                            }
                            
                            Spacer()
                            
                            if vm.authStore.currentUser?.role == .admin {
                                Text("👑 Admin")
                                    .font(.system(size: 11, weight: .bold))
                                    .foregroundColor(Color.naaGold)
                                    .padding(.horizontal, 8)
                                    .padding(.vertical, 4)
                                    .background(Color.naaGold.opacity(0.15))
                                    .cornerRadius(6)
                            }
                        }
                        .padding(16)
                        .background(Color.surface(dark: vm.darkMode))
                        .cornerRadius(16)
                        .overlay(RoundedRectangle(cornerRadius: 16).stroke(Color.border(dark: vm.darkMode), lineWidth: 1))
                        
                        // 2. Language Picker Row (Matches Image 3)
                        Button(action: { showLanguageDialog = true }) {
                            HStack(spacing: 14) {
                                Image(systemName: "globe")
                                    .font(.system(size: 18))
                                    .foregroundColor(Color.naaGold)
                                    .frame(width: 24)
                                
                                VStack(alignment: .leading, spacing: 2) {
                                    Text("మీ భాషను ఎంచుకోండి")
                                        .font(.system(size: 12))
                                        .foregroundColor(Color.textSecondary(dark: vm.darkMode))
                                    Text("\(vm.currentLanguage.nativeName) \(vm.currentLanguage.englishName)")
                                        .font(.system(size: 15, weight: .bold))
                                        .foregroundColor(Color.naaGold)
                                }
                                
                                Spacer()
                                
                                Image(systemName: "chevron.right")
                                    .foregroundColor(Color.textSecondary(dark: vm.darkMode))
                            }
                            .padding(14)
                            .background(Color.surface(dark: vm.darkMode))
                            .cornerRadius(14)
                            .overlay(RoundedRectangle(cornerRadius: 14).stroke(Color.border(dark: vm.darkMode), lineWidth: 1))
                        }
                        
                        // 3. Location Picker Row (Matches Image 3)
                        Button(action: { showLocationDialog = true }) {
                            HStack(spacing: 14) {
                                Image(systemName: "mappin.and.ellipse")
                                    .font(.system(size: 18))
                                    .foregroundColor(Color.naaGold)
                                    .frame(width: 24)
                                
                                VStack(alignment: .leading, spacing: 2) {
                                    Text("మీరు ఎంచుకున్న స్థానం")
                                        .font(.system(size: 12))
                                        .foregroundColor(Color.textSecondary(dark: vm.darkMode))
                                    Text("\(vm.locationManager.userLocation) (Change)")
                                        .font(.system(size: 14, weight: .bold))
                                        .foregroundColor(Color.naaGold)
                                }
                                
                                Spacer()
                                
                                Image(systemName: "chevron.right")
                                    .foregroundColor(Color.textSecondary(dark: vm.darkMode))
                            }
                            .padding(14)
                            .background(Color.surface(dark: vm.darkMode))
                            .cornerRadius(14)
                            .overlay(RoundedRectangle(cornerRadius: 14).stroke(Color.border(dark: vm.darkMode), lineWidth: 1))
                        }
                        
                        // 4. Categories Horizontal Row (Matches Image 3: "మీకోసం" removed, "క్రీడలు" added)
                        VStack(alignment: .leading, spacing: 10) {
                            Text("Categories")
                                .font(.system(size: 15, weight: .bold))
                                .foregroundColor(Color.textPrimary(dark: vm.darkMode))
                            
                            ScrollView(.horizontal, showsIndicators: false) {
                                HStack(spacing: 14) {
                                    CategoryCircleItem(icon: "newspaper.fill", title: "తాజా వార్తలు", color: Color.naaGold) {
                                        vm.selectCategory(0)
                                        onNavigateNews()
                                    }
                                    CategoryCircleItem(icon: "doc.text.fill", title: "వార్తలు", color: Color.blue) {
                                        vm.selectCategory(1)
                                        onNavigateNews()
                                    }
                                    CategoryCircleItem(icon: "trophy.fill", title: "క్రీడలు", color: Color.orange) {
                                        vm.selectCategory(2)
                                        onNavigateNews()
                                    }
                                    CategoryCircleItem(icon: "film.fill", title: "సినిమా", color: Color.purple) {
                                        vm.selectCategory(3)
                                        onNavigateNews()
                                    }
                                    CategoryCircleItem(icon: "building.columns.fill", title: "రాజకీయాలు", color: Color.red) {
                                        vm.selectCategory(4)
                                        onNavigateNews()
                                    }
                                    CategoryCircleItem(icon: "chart.line.uptrend.xyaxis", title: "వ్యాపారం", color: Color.green) {
                                        vm.selectCategory(5)
                                        onNavigateNews()
                                    }
                                }
                                .padding(.vertical, 4)
                            }
                        }
                        .padding(.vertical, 4)
                        
                        // 5. Quick Links (Matches Image 3)
                        VStack(spacing: 0) {
                            // Fact Check (CHECK NOW)
                            QuickActionRow(
                                icon: "checkmark.seal.fill",
                                title: "Fact Check",
                                actionText: "CHECK NOW",
                                dark: vm.darkMode
                            ) {
                                if let url = URL(string: "https://naatvtelugu.com/fact-check/") {
                                    UIApplication.shared.open(url)
                                }
                            }
                            
                            Divider().background(Color.border(dark: vm.darkMode))
                            
                            // Referral (REFER NOW)
                            QuickActionRow(
                                icon: "square.and.arrow.up.fill",
                                title: "రెఫరల్ (Refer & Earn)",
                                actionText: "REFER NOW",
                                dark: vm.darkMode
                            ) {
                                showReferralDialog = true
                            }
                            
                            Divider().background(Color.border(dark: vm.darkMode))
                            
                            // Saved Stories
                            StandardLinkRow(icon: "bookmark.fill", title: "సేవ్ చేసిన వార్తలు (Saved Stories)", dark: vm.darkMode, action: onNavigateSaved)
                            
                            Divider().background(Color.border(dark: vm.darkMode))
                            
                            // Status Videos
                            StandardLinkRow(icon: "play.rectangle.fill", title: "స్టేటస్ వీడియోలు", dark: vm.darkMode) {
                                onNavigateNews()
                            }
                            
                            Divider().background(Color.border(dark: vm.darkMode))
                            
                            // NAA TV Exclusive
                            StandardLinkRow(icon: "star.fill", title: "నా టీవీ ఎక్స్‌క్లూజివ్", dark: vm.darkMode) {
                                onNavigateNews()
                            }
                            
                            // Admin Panel link (for admin)
                            if vm.authStore.currentUser?.role == .admin {
                                Divider().background(Color.border(dark: vm.darkMode))
                                StandardLinkRow(icon: "shield.checkerboard", title: "అడ్మిన్ ప్యానెల్ & కస్టమైజేషన్ (Admin Panel)", dark: vm.darkMode, action: onNavigateAdmin)
                            }
                            
                            // Employee Reporter Desk link (for employee or admin)
                            if vm.authStore.currentUser?.role == .employee || vm.authStore.currentUser?.role == .admin {
                                Divider().background(Color.border(dark: vm.darkMode))
                                StandardLinkRow(icon: "square.and.pencil", title: "రిపోర్టర్ డెస్క్ (Post News)", dark: vm.darkMode, action: onNavigateEmployee)
                            }
                        }
                        .background(Color.surface(dark: vm.darkMode))
                        .cornerRadius(16)
                        .overlay(RoundedRectangle(cornerRadius: 16).stroke(Color.border(dark: vm.darkMode), lineWidth: 1))
                        
                        // Dark Mode Toggle & Logout
                        VStack(spacing: 12) {
                            HStack {
                                Image(systemName: vm.darkMode ? "moon.stars.fill" : "sun.max.fill")
                                    .foregroundColor(Color.naaGold)
                                Text("డార్క్ మోడ్ (Dark Mode)")
                                    .font(.system(size: 14, weight: .semibold))
                                    .foregroundColor(Color.textPrimary(dark: vm.darkMode))
                                Spacer()
                                Toggle("", isOn: Binding(
                                    get: { vm.darkMode },
                                    set: { _ in vm.toggleDarkMode() }
                                ))
                                .labelsHidden()
                            }
                            .padding(14)
                            .background(Color.surface(dark: vm.darkMode))
                            .cornerRadius(14)
                            .overlay(RoundedRectangle(cornerRadius: 14).stroke(Color.border(dark: vm.darkMode), lineWidth: 1))
                            
                            Button(action: { vm.authStore.logout() }) {
                                HStack {
                                    Image(systemName: "rectangle.portrait.and.arrow.right")
                                    Text("ఖాతా మార్చండి / లాగౌట్ (Logout)")
                                        .font(.system(size: 14, weight: .bold))
                                }
                                .foregroundColor(Color.naaRed)
                                .frame(maxWidth: .infinity)
                                .frame(height: 44)
                                .background(Color.naaRed.opacity(0.1))
                                .cornerRadius(12)
                            }
                        }
                        
                        // Copyright Footer
                        VStack(spacing: 4) {
                            Text(NaaTvBrand.copyright)
                                .font(.system(size: 11))
                                .foregroundColor(Color.textSecondary(dark: vm.darkMode))
                            Text(NaaTvBrand.portal)
                                .font(.system(size: 11, weight: .medium))
                                .foregroundColor(Color.naaGold)
                        }
                        .padding(.vertical, 12)
                    }
                    .padding(16)
                }
            }
            
            // Overlays
            if showLanguageDialog {
                LanguageSelectionDialog(vm: vm) { showLanguageDialog = false }
            }
            if showLocationDialog {
                LocationPickerDialog(vm: vm) { showLocationDialog = false }
            }
            if showReferralDialog {
                ReferralDialogView(vm: vm) { showReferralDialog = false }
            }
        }
    }
}

// Subcomponents for More screen
struct CategoryCircleItem: View {
    let icon: String
    let title: String
    let color: Color
    let action: () -> Void
    
    var body: some View {
        Button(action: action) {
            VStack(spacing: 6) {
                ZStack {
                    Circle()
                        .fill(color.opacity(0.15))
                        .frame(width: 50, height: 50)
                    Image(systemName: icon)
                        .font(.system(size: 20))
                        .foregroundColor(color)
                }
                Text(title)
                    .font(.system(size: 11, weight: .medium))
                    .foregroundColor(Color.primary)
                    .lineLimit(1)
            }
            .frame(width: 68)
        }
    }
}

struct QuickActionRow: View {
    let icon: String
    let title: String
    let actionText: String
    let dark: Bool
    let action: () -> Void
    
    var body: some View {
        Button(action: action) {
            HStack(spacing: 12) {
                Image(systemName: icon)
                    .foregroundColor(Color.naaEmerald)
                    .frame(width: 20)
                Text(title)
                    .font(.system(size: 14, weight: .semibold))
                    .foregroundColor(Color.textPrimary(dark: dark))
                Spacer()
                Text(actionText)
                    .font(.system(size: 12, weight: .heavy))
                    .foregroundColor(Color.naaGold)
            }
            .padding(.horizontal, 14)
            .padding(.vertical, 14)
        }
    }
}

struct StandardLinkRow: View {
    let icon: String
    let title: String
    let dark: Bool
    let action: () -> Void
    
    var body: some View {
        Button(action: action) {
            HStack(spacing: 12) {
                Image(systemName: icon)
                    .foregroundColor(Color.naaGold)
                    .frame(width: 20)
                Text(title)
                    .font(.system(size: 14, weight: .medium))
                    .foregroundColor(Color.textPrimary(dark: dark))
                Spacer()
                Image(systemName: "chevron.right")
                    .font(.system(size: 12, weight: .bold))
                    .foregroundColor(Color.textSecondary(dark: dark).opacity(0.6))
            }
            .padding(.horizontal, 14)
            .padding(.vertical, 14)
        }
    }
}
