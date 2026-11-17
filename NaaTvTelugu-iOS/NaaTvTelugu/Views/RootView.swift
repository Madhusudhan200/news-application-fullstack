import SwiftUI

public struct RootView: View {
    @StateObject private var viewModel = NewsViewModel()
    
    // Splash screen state
    @State private var showSplash: Bool = true
    
    // Active navigation tab:
    // 0: Home
    // 1: News (Watching)
    // 2: Reels
    // 3: Local / Location
    // 4: More / Profile
    @State private var activeTab: Int = 0
    
    // Sheet & Dialog presentations
    @State private var showCategoriesSheet: Bool = false
    @State private var showReferralDialog: Bool = false
    @State private var showLocationDialog: Bool = false
    @State private var showLanguageDialog: Bool = false
    @State private var showAdminPanel: Bool = false
    @State private var showEmployeeDesk: Bool = false
    @State private var showSavedStories: Bool = false
    @State private var showAuthSheet: Bool = false
    @State private var fullStoryItem: NewsItem? = nil
    
    public init() {}
    
    public var body: some View {
        ZStack {
            Theme.darkBackground.ignoresSafeArea()
            
            if showSplash {
                SplashScreenView(onFinished: {
                    withAnimation(.easeInOut(duration: 0.5)) {
                        showSplash = false
                    }
                })
                .transition(.opacity)
                .zIndex(10)
            } else {
                VStack(spacing: 0) {
                    // Main Screen Area
                    ZStack {
                        switch activeTab {
                        case 0:
                            HomeExploreView(
                                viewModel: viewModel,
                                onOpenMore: { activeTab = 4 },
                                onOpenNews: { activeTab = 1 },
                                onOpenCategories: { showCategoriesSheet = true },
                                onOpenReels: { activeTab = 2 },
                                onOpenLocationPicker: { showLocationDialog = true },
                                onOpenCreatePost: {
                                    if let user = viewModel.authStore.currentUser, (user.role == .admin || user.role == .employee) {
                                        showEmployeeDesk = true
                                    } else {
                                        showAuthSheet = true
                                    }
                                },
                                onSelectStory: { story in
                                    if let idx = viewModel.filteredItems.firstIndex(where: { $0.id == story.id }) {
                                        viewModel.activeStoryIndex = idx
                                    }
                                    activeTab = 1
                                }
                            )
                            
                        case 1:
                            NewsWatchingView(
                                viewModel: viewModel,
                                onBackToHome: { activeTab = 0 },
                                onOpenCategories: { showCategoriesSheet = true },
                                onOpenFullStory: { story in fullStoryItem = story }
                            )
                            
                        case 2:
                            // Reels Watching
                            reelsView
                            
                        case 3:
                            // Local News Feed
                            localNewsView
                            
                        case 4:
                            // More / Profile Screen (Reference Image 3)
                            MoreProfileView(
                                viewModel: viewModel,
                                onOpenCategories: { showCategoriesSheet = true },
                                onOpenLanguageDialog: { showLanguageDialog = true },
                                onOpenLocationPicker: { showLocationDialog = true },
                                onOpenReferral: { showReferralDialog = true },
                                onOpenAdminPanel: { showAdminPanel = true },
                                onOpenEmployeeDesk: { showEmployeeDesk = true },
                                onOpenSavedStories: { showSavedStories = true },
                                onOpenAuth: { showAuthSheet = true },
                                onSelectCategory: { index in
                                    viewModel.selectCategory(index)
                                    activeTab = 1
                                }
                            )
                            
                        default:
                            EmptyView()
                        }
                    }
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                    
                    // Persistent 5-Tab Custom Navigation Bar (Matches Reference Image 2)
                    bottom5TabBar
                }
            }
            
            // Popups & Dialogs
            if showReferralDialog {
                ReferralDialogView(viewModel: viewModel, isPresented: $showReferralDialog)
                    .zIndex(20)
            }
            
            if showLocationDialog {
                LocationPickerDialog(viewModel: viewModel, isPresented: $showLocationDialog)
                    .zIndex(21)
            }
            
            if showLanguageDialog {
                LanguageSelectionDialog(viewModel: viewModel, isPresented: $showLanguageDialog)
                    .zIndex(22)
            }
        }
        // Sheets
        .sheet(isPresented: $showCategoriesSheet) {
            CategoriesSheetView(
                viewModel: viewModel,
                isPresented: $showCategoriesSheet,
                onSelectCategory: { index in
                    viewModel.selectCategory(index)
                    activeTab = 1
                },
                onSelectLocation: { _ in
                    activeTab = 3
                }
            )
        }
        .sheet(item: $fullStoryItem) { story in
            FullStorySheetView(item: story, viewModel: viewModel)
        }
        .fullScreenCover(isPresented: $showAdminPanel) {
            AdminPanelView(viewModel: viewModel, onDismiss: { showAdminPanel = false })
        }
        .sheet(isPresented: $showEmployeeDesk) {
            EmployeePostView(viewModel: viewModel, onDismiss: { showEmployeeDesk = false })
        }
        .sheet(isPresented: $showSavedStories) {
            SavedStoriesView(
                viewModel: viewModel,
                onDismiss: { showSavedStories = false },
                onSelectStory: { story in
                    showSavedStories = false
                    if let idx = viewModel.filteredItems.firstIndex(where: { $0.id == story.id }) {
                        viewModel.activeStoryIndex = idx
                    }
                    activeTab = 1
                }
            )
        }
        .sheet(isPresented: $showAuthSheet) {
            AuthView(viewModel: viewModel, onDismiss: { showAuthSheet = false })
        }
    }
    
    // MARK: - 5-Tab Custom Bottom Bar (Reference Image 2)
    private var bottom5TabBar: some View {
        HStack(alignment: .bottom, spacing: 0) {
            // Tab 1: హోమ్ (Home)
            tabItem(
                title: "హోమ్",
                icon: "house.fill",
                isSelected: activeTab == 0
            ) {
                activeTab = 0
            }
            
            // Tab 2: న్యూస్ (News)
            tabItem(
                title: "న్యూస్",
                icon: "newspaper.fill",
                isSelected: activeTab == 1
            ) {
                activeTab = 1
            }
            
            // Tab 3: Elevated Circular Button (కేటగిరీలు / Categories)
            Button(action: {
                showCategoriesSheet = true
            }) {
                VStack(spacing: 2) {
                    ZStack {
                        Circle()
                            .fill(Theme.navyPrimary)
                            .frame(width: 52, height: 52)
                            .overlay(
                                Circle()
                                    .stroke(Theme.goldColor, lineWidth: 2)
                            )
                            .shadow(color: Theme.goldColor.opacity(0.3), radius: 6, x: 0, y: 3)
                        
                        Image(systemName: "square.grid.2x2")
                            .font(.system(size: 22, weight: .bold))
                            .foregroundColor(Theme.goldColor)
                    }
                    .offset(y: -12)
                    
                    Text("కేటగిరీలు")
                        .font(.system(size: 11, weight: .bold))
                        .foregroundColor(Theme.goldColor)
                        .offset(y: -10)
                }
                .frame(maxWidth: .infinity)
            }
            .buttonStyle(PlainButtonStyle())
            
            // Tab 4: రీల్స్ (Reels)
            tabItem(
                title: "రీల్స్",
                icon: "play.rectangle.fill",
                isSelected: activeTab == 2
            ) {
                activeTab = 2
            }
            
            // Tab 5: పొన్నమండ / Local Location
            let locShort = viewModel.locationManager.userLocation.split(separator: ",").first.map(String.init) ?? "పొన్నమండ"
            tabItem(
                title: locShort,
                icon: "mappin.and.ellipse",
                isSelected: activeTab == 3
            ) {
                activeTab = 3
            }
        }
        .padding(.horizontal, 8)
        .padding(.top, 6)
        .padding(.bottom, 6)
        .background(
            Theme.navyPrimary
                .shadow(color: Color.black.opacity(0.5), radius: 6, x: 0, y: -2)
        )
    }
    
    private func tabItem(title: String, icon: String, isSelected: Bool, action: @escaping () -> Void) -> some View {
        Button(action: action) {
            VStack(spacing: 4) {
                Image(systemName: icon)
                    .font(.system(size: 20, weight: isSelected ? .bold : .regular))
                    .foregroundColor(isSelected ? Theme.goldColor : .white.opacity(0.65))
                
                Text(title)
                    .font(.system(size: 11, weight: isSelected ? .bold : .medium))
                    .foregroundColor(isSelected ? Theme.goldColor : .white.opacity(0.65))
                    .lineLimit(1)
            }
            .frame(maxWidth: .infinity)
            .padding(.vertical, 4)
        }
        .buttonStyle(PlainButtonStyle())
    }
    
    // MARK: - Reels View
    private var reelsView: some View {
        ZStack {
            Theme.darkBackground.ignoresSafeArea()
            
            let reels = viewModel.reelsItems.isEmpty ? viewModel.filteredItems : viewModel.reelsItems
            
            if reels.isEmpty {
                VStack(spacing: 12) {
                    Image(systemName: "video.slash")
                        .font(.system(size: 48))
                        .foregroundColor(Theme.goldColor.opacity(0.5))
                    Text("రీల్స్ వీడియోలు లోడ్ అవుతున్నాయి...")
                        .font(.system(size: 15))
                        .foregroundColor(.white.opacity(0.7))
                }
            } else {
                TabView {
                    ForEach(reels) { reel in
                        ZStack(alignment: .bottomLeading) {
                            if let urlStr = reel.imageUrl, let url = URL(string: urlStr) {
                                AsyncImage(url: url) { phase in
                                    switch phase {
                                    case .success(let img):
                                        img.resizable().scaledToFill()
                                    default:
                                        Theme.navyCard
                                    }
                                }
                                .ignoresSafeArea()
                            }
                            
                            // Dark gradient
                            LinearGradient(
                                colors: [Color.clear, Color.black.opacity(0.85)],
                                startPoint: .center,
                                endPoint: .bottom
                            )
                            .ignoresSafeArea()
                            
                            // Center Play Icon
                            Circle()
                                .fill(Color.black.opacity(0.6))
                                .frame(width: 56, height: 56)
                                .overlay(
                                    Image(systemName: "play.fill")
                                        .font(.system(size: 22))
                                        .foregroundColor(Theme.goldColor)
                                )
                                .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
                            
                            // Reel Description & Controls
                            VStack(alignment: .leading, spacing: 8) {
                                Text(reel.title)
                                    .font(.system(size: 16, weight: .bold))
                                    .foregroundColor(.white)
                                    .lineLimit(3)
                                
                                Text(reel.summary)
                                    .font(.system(size: 13))
                                    .foregroundColor(.white.opacity(0.8))
                                    .lineLimit(2)
                                
                                HStack(spacing: 14) {
                                    Button(action: { viewModel.shareToWhatsApp(item: reel) }) {
                                        HStack(spacing: 6) {
                                            Image(systemName: "arrow.turn.up.right")
                                            Text("షేర్ చేయండి")
                                        }
                                        .font(.system(size: 13, weight: .bold))
                                        .foregroundColor(.black)
                                        .padding(.horizontal, 14)
                                        .padding(.vertical, 8)
                                        .background(Theme.goldColor)
                                        .cornerRadius(20)
                                    }
                                    
                                    Button(action: { fullStoryItem = reel }) {
                                        Text("పూర్తి వివరాలు")
                                            .font(.system(size: 13, weight: .semibold))
                                            .foregroundColor(.white)
                                            .padding(.horizontal, 14)
                                            .padding(.vertical, 8)
                                            .background(Color.white.opacity(0.15))
                                            .cornerRadius(20)
                                    }
                                }
                            }
                            .padding(20)
                            .padding(.bottom, 60)
                        }
                    }
                }
                .tabViewStyle(PageTabViewStyle(indexDisplayMode: .never))
            }
        }
    }
    
    // MARK: - Local News View
    private var localNewsView: some View {
        VStack(spacing: 0) {
            // Header
            HStack {
                Image(systemName: "mappin.circle.fill")
                    .foregroundColor(Theme.goldColor)
                
                Text(viewModel.locationManager.userLocation)
                    .font(.system(size: 15, weight: .bold))
                    .foregroundColor(.white)
                
                Spacer()
                
                Button(action: { showLocationDialog = true }) {
                    Text("మార్చండి")
                        .font(.system(size: 12, weight: .bold))
                        .foregroundColor(Theme.goldColor)
                }
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 12)
            .background(Theme.navyPrimary)
            
            let localItems = viewModel.localNewsItems.isEmpty ? Array(viewModel.filteredItems.prefix(8)) : viewModel.localNewsItems
            
            ScrollView {
                VStack(spacing: 12) {
                    ForEach(localItems) { item in
                        Button(action: {
                            if let idx = viewModel.filteredItems.firstIndex(where: { $0.id == item.id }) {
                                viewModel.activeStoryIndex = idx
                            }
                            activeTab = 1
                        }) {
                            HStack(spacing: 12) {
                                if let urlStr = item.imageUrl, let url = URL(string: urlStr) {
                                    AsyncImage(url: url) { phase in
                                        switch phase {
                                        case .success(let img):
                                            img.resizable().scaledToFill()
                                        default:
                                            Color.gray.opacity(0.3)
                                        }
                                    }
                                    .frame(width: 90, height: 75)
                                    .cornerRadius(8)
                                    .clipped()
                                }
                                
                                VStack(alignment: .leading, spacing: 4) {
                                    Text(item.title)
                                        .font(.system(size: 13, weight: .bold))
                                        .foregroundColor(.white)
                                        .lineLimit(2)
                                    
                                    HStack(spacing: 6) {
                                        Text(item.category)
                                            .font(.system(size: 10, weight: .bold))
                                            .foregroundColor(Theme.goldColor)
                                        
                                        Text("•")
                                            .foregroundColor(.white.opacity(0.4))
                                        
                                        Text(item.publishedDate)
                                            .font(.system(size: 10))
                                            .foregroundColor(.white.opacity(0.6))
                                    }
                                }
                                
                                Spacer()
                            }
                            .padding(12)
                            .background(Theme.navyCard)
                            .cornerRadius(12)
                            .padding(.horizontal, 16)
                        }
                        .buttonStyle(PlainButtonStyle())
                    }
                }
                .padding(.vertical, 12)
            }
        }
    }
}
