import SwiftUI

public struct CategoriesSheetView: View {
    @ObservedObject var vm: NewsViewModel
    public var onDismiss: () -> Void
    public var onSelectCategory: (Int) -> Void
    
    @State private var showLocationPicker: Bool = false
    
    public init(
        viewModel: NewsViewModel,
        isPresented: Binding<Bool>,
        onSelectCategory: @escaping (Int) -> Void,
        onSelectLocation: @escaping (String) -> Void
    ) {
        self.vm = viewModel
        self.onDismiss = { isPresented.wrappedValue = false }
        self.onSelectCategory = onSelectCategory
    }
    
    public init(vm: NewsViewModel, onDismiss: @escaping () -> Void, onSelectCategory: @escaping (Int) -> Void) {
        self.vm = vm
        self.onDismiss = onDismiss
        self.onSelectCategory = onSelectCategory
    }
    
    public var body: some View {
        ZStack {
            Color.surface(dark: vm.darkMode)
                .ignoresSafeArea()
            
            VStack(spacing: 12) {
                // Top Close 'X' Button (Matches Image 1)
                HStack {
                    Spacer()
                    Button(action: onDismiss) {
                        Image(systemName: "xmark.circle.fill")
                            .font(.system(size: 26))
                            .foregroundColor(Color.textSecondary(dark: vm.darkMode))
                    }
                }
                .padding(.horizontal, 16)
                .padding(.top, 8)
                
                // Search Input Bar (Matches Image 1)
                HStack {
                    TextField("సెర్చ్ చేయండి..", text: $vm.searchQuery)
                        .font(.system(size: 15))
                        .foregroundColor(Color.textPrimary(dark: vm.darkMode))
                    
                    Image(systemName: "magnifyingglass")
                        .foregroundColor(Color.textSecondary(dark: vm.darkMode))
                }
                .padding(.horizontal, 14)
                .frame(height: 44)
                .background(Color.screenBg(dark: vm.darkMode))
                .cornerRadius(10)
                .overlay(
                    RoundedRectangle(cornerRadius: 10)
                        .stroke(Color.border(dark: vm.darkMode), lineWidth: 1)
                )
                .padding(.horizontal, 16)
                
                ScrollView {
                    VStack(spacing: 18) {
                        // ── LOCAL CATEGORIES HEADER ──
                        HStack(spacing: 12) {
                            Rectangle()
                                .fill(Color.border(dark: vm.darkMode))
                                .frame(height: 1)
                            Text("LOCAL CATEGORIES")
                                .font(.system(size: 12, weight: .black))
                                .foregroundColor(Color.textPrimary(dark: vm.darkMode))
                            Rectangle()
                                .fill(Color.border(dark: vm.darkMode))
                                .frame(height: 1)
                        }
                        .padding(.horizontal, 16)
                        .padding(.top, 6)
                        
                        // Local Categories Cards Row (Matches Image 1)
                        HStack(alignment: .top, spacing: 12) {
                            // Left: Favourite Locations Card
                            VStack(alignment: .leading, spacing: 10) {
                                Text("Favourite Locations")
                                    .font(.system(size: 13, weight: .bold))
                                    .foregroundColor(Color.textPrimary(dark: vm.darkMode))
                                
                                // Selected Location Sub-card with Pencil
                                HStack {
                                    Image(systemName: "mappin.and.ellipse")
                                        .foregroundColor(Color.naaGold)
                                    VStack(alignment: .leading, spacing: 2) {
                                        Text(vm.locationManager.userLocation.split(separator: ",").first ?? "Ponnamanda")
                                            .font(.system(size: 13, weight: .bold))
                                            .foregroundColor(Color.textPrimary(dark: vm.darkMode))
                                        Text("Ambedkar Konaseema..")
                                            .font(.system(size: 10))
                                            .foregroundColor(Color.textSecondary(dark: vm.darkMode))
                                    }
                                    Spacer()
                                    Button(action: { showLocationPicker = true }) {
                                        Image(systemName: "pencil")
                                            .font(.system(size: 12))
                                            .foregroundColor(Color.textSecondary(dark: vm.darkMode))
                                    }
                                }
                                .padding(10)
                                .background(Color.screenBg(dark: vm.darkMode))
                                .cornerRadius(10)
                                
                                // + Add Location Button
                                Button(action: { showLocationPicker = true }) {
                                    HStack {
                                        Image(systemName: "plus")
                                            .font(.system(size: 12, weight: .bold))
                                        Text("Add Location")
                                            .font(.system(size: 12, weight: .bold))
                                    }
                                    .foregroundColor(Color.naaGold)
                                    .frame(maxWidth: .infinity)
                                    .frame(height: 36)
                                    .background(Color.naaGold.opacity(0.12))
                                    .cornerRadius(8)
                                }
                            }
                            .padding(12)
                            .frame(maxWidth: .infinity)
                            .background(Color.screenBg(dark: vm.darkMode))
                            .cornerRadius(14)
                            .overlay(RoundedRectangle(cornerRadius: 14).stroke(Color.border(dark: vm.darkMode), lineWidth: 1))
                            
                            // Right: Local News Card (Matches Image 1: 2 పొన్నమండ of 4)
                            ZStack(alignment: .bottomLeading) {
                                // Background Photo
                                Image(systemName: "photo.fill")
                                    .resizable()
                                    .scaledToFill()
                                    .frame(maxWidth: .infinity)
                                    .frame(height: 160)
                                    .foregroundColor(Color.gray.opacity(0.3))
                                    .background(Color.black.opacity(0.7))
                                    .clipped()
                                    .cornerRadius(14)
                                
                                // Gradient Overlay
                                LinearGradient(
                                    colors: [Color.clear, Color.black.opacity(0.85)],
                                    startPoint: .top,
                                    endPoint: .bottom
                                )
                                .cornerRadius(14)
                                
                                VStack(alignment: .leading, spacing: 4) {
                                    // Top Badge: 2 పొన్నమండ of 4
                                    Text("2 పొన్నమండ of 4")
                                        .font(.system(size: 13, weight: .heavy))
                                        .foregroundColor(Color.naaGold)
                                        .padding(.bottom, 24)
                                    
                                    // Local News Pill
                                    Text("Local News")
                                        .font(.system(size: 11, weight: .bold))
                                        .foregroundColor(.black)
                                        .padding(.horizontal, 6)
                                        .padding(.vertical, 2)
                                        .background(Color.naaGold)
                                        .cornerRadius(4)
                                    
                                    Text("మీ ఊరి వార్తలకోసం... >")
                                        .font(.system(size: 11, weight: .semibold))
                                        .foregroundColor(Color.naaGold)
                                }
                                .padding(12)
                            }
                            .frame(maxWidth: .infinity)
                            .frame(height: 160)
                            .onTapGesture {
                                onSelectCategory(0)
                                onDismiss()
                            }
                        }
                        .padding(.horizontal, 16)
                        
                        // ── NEWS CATEGORIES HEADER ──
                        HStack(spacing: 12) {
                            Rectangle()
                                .fill(Color.border(dark: vm.darkMode))
                                .frame(height: 1)
                            Text("NEWS CATEGORIES")
                                .font(.system(size: 12, weight: .black))
                                .foregroundColor(Color.textPrimary(dark: vm.darkMode))
                            Rectangle()
                                .fill(Color.border(dark: vm.darkMode))
                                .frame(height: 1)
                        }
                        .padding(.horizontal, 16)
                        
                        // News Categories Row (Matches Image 1)
                        HStack(alignment: .top, spacing: 12) {
                            // Left Column: Category List Container
                            VStack(spacing: 0) {
                                // 1. ఆల్ న్యూస్ (All News)
                                CategoryListRow(
                                    icon: "newspaper.fill",
                                    title: "ఆల్ న్యూస్",
                                    isSelected: vm.selectedCategoryIndex == 0 && !vm.showOnlyUnread,
                                    badge: nil,
                                    dark: vm.darkMode
                                ) {
                                    vm.showOnlyUnread = false
                                    onSelectCategory(0)
                                    onDismiss()
                                }
                                
                                Divider().background(Color.border(dark: vm.darkMode))
                                
                                // 2. చదవనివి (Unread Stories with dynamic badge count)
                                CategoryListRow(
                                    icon: "eye.slash.fill",
                                    title: "చదవనివి",
                                    isSelected: vm.showOnlyUnread,
                                    badge: "\(vm.unreadCount)",
                                    dark: vm.darkMode
                                ) {
                                    vm.showOnlyUnread = true
                                    onSelectCategory(0)
                                    onDismiss()
                                }
                                
                                Divider().background(Color.border(dark: vm.darkMode))
                                
                                // Rest of Categories ("మీకోసం" removed, "క్రీడలు" included)
                                ForEach(Array(Categories.list.enumerated()), id: \.offset) { index, cat in
                                    if index > 0 { // Skip 0 which is "తాజా వార్తలు" / all
                                        CategoryListRow(
                                            icon: categorySystemIcon(cat.slug),
                                            title: cat.label,
                                            isSelected: vm.selectedCategoryIndex == index && !vm.showOnlyUnread,
                                            badge: nil,
                                            dark: vm.darkMode
                                        ) {
                                            vm.showOnlyUnread = false
                                            onSelectCategory(index)
                                            onDismiss()
                                        }
                                        if index < Categories.list.count - 1 {
                                            Divider().background(Color.border(dark: vm.darkMode))
                                        }
                                    }
                                }
                            }
                            .padding(.vertical, 4)
                            .frame(maxWidth: .infinity)
                            .background(Color.screenBg(dark: vm.darkMode))
                            .cornerRadius(14)
                            .overlay(RoundedRectangle(cornerRadius: 14).stroke(Color.border(dark: vm.darkMode), lineWidth: 1))
                            
                            // Right Column: Breaking News Card (Matches Image 1: 2 న్యూస్ of 8)
                            ZStack(alignment: .bottomLeading) {
                                Image(systemName: "newspaper.fill")
                                    .resizable()
                                    .scaledToFill()
                                    .frame(maxWidth: .infinity)
                                    .frame(height: 240)
                                    .foregroundColor(Color.gray.opacity(0.3))
                                    .background(Color.black.opacity(0.8))
                                    .clipped()
                                    .cornerRadius(14)
                                
                                LinearGradient(
                                    colors: [Color.clear, Color.black.opacity(0.9)],
                                    startPoint: .top,
                                    endPoint: .bottom
                                )
                                .cornerRadius(14)
                                
                                VStack(alignment: .leading, spacing: 4) {
                                    Text("2 న్యూస్ of 8")
                                        .font(.system(size: 14, weight: .heavy))
                                        .foregroundColor(Color.naaGold)
                                        .padding(.bottom, 60)
                                    
                                    Text("Breaking News")
                                        .font(.system(size: 11, weight: .bold))
                                        .foregroundColor(.black)
                                        .padding(.horizontal, 6)
                                        .padding(.vertical, 2)
                                        .background(Color.naaGold)
                                        .cornerRadius(4)
                                    
                                    Text("News")
                                        .font(.system(size: 15, weight: .bold))
                                        .foregroundColor(.white)
                                    
                                    Text("న్యూస్ కొరకు... >")
                                        .font(.system(size: 11, weight: .semibold))
                                        .foregroundColor(Color.naaGold)
                                }
                                .padding(12)
                            }
                            .frame(maxWidth: .infinity)
                            .frame(height: 240)
                            .onTapGesture {
                                onSelectCategory(1)
                                onDismiss()
                            }
                        }
                        .padding(.horizontal, 16)
                        .padding(.bottom, 24)
                    }
                }
            }
            
            if showLocationPicker {
                LocationPickerDialog(vm: vm) {
                    showLocationPicker = false
                }
            }
        }
    }
    
    private func categorySystemIcon(_ slug: String?) -> String {
        switch slug {
        case "news": return "newspaper"
        case "sports": return "trophy.fill"
        case "cinema": return "film.fill"
        case "politics": return "building.columns.fill"
        case "business": return "chart.line.uptrend.xyaxis"
        case "health-fitness": return "heart.fill"
        case "ayur_vedam": return "leaf.fill"
        default: return "newspaper"
        }
    }
}

// Subview for Category Row in sheet
struct CategoryListRow: View {
    let icon: String
    let title: String
    let isSelected: Bool
    let badge: String?
    let dark: Bool
    let action: () -> Void
    
    var body: some View {
        Button(action: action) {
            HStack(spacing: 10) {
                Image(systemName: icon)
                    .font(.system(size: 14))
                    .foregroundColor(isSelected ? Color.naaGold : Color.textSecondary(dark: dark))
                    .frame(width: 20)
                
                Text(title)
                    .font(.system(size: 13, weight: isSelected ? .bold : .medium))
                    .foregroundColor(isSelected ? Color.naaGold : Color.textPrimary(dark: dark))
                    .lineLimit(1)
                
                Spacer()
                
                if let badge = badge {
                    Text(badge)
                        .font(.system(size: 10, weight: .heavy))
                        .foregroundColor(.white)
                        .padding(.horizontal, 6)
                        .padding(.vertical, 2)
                        .background(Color.naaRed)
                        .clipShape(Capsule())
                }
                
                Image(systemName: "chevron.right")
                    .font(.system(size: 10, weight: .bold))
                    .foregroundColor(Color.textSecondary(dark: dark).opacity(0.6))
            }
            .padding(.horizontal, 12)
            .padding(.vertical, 9)
            .background(isSelected ? Color.naaGold.opacity(0.12) : Color.clear)
        }
    }
}
