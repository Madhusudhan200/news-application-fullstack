import SwiftUI

public struct NewsWatchingView: View {
    @ObservedObject var vm: NewsViewModel
    public var onNavigateHome: () -> Void
    public var onOpenCategories: () -> Void
    public var onOpenFullStory: ((NewsItem) -> Void)?
    
    @State private var selectedStory: NewsItem? = nil
    @State private var showFullStorySheet: Bool = false
    
    public init(
        viewModel: NewsViewModel,
        onBackToHome: @escaping () -> Void,
        onOpenCategories: @escaping () -> Void,
        onOpenFullStory: @escaping (NewsItem) -> Void
    ) {
        self.vm = viewModel
        self.onNavigateHome = onBackToHome
        self.onOpenCategories = onOpenCategories
        self.onOpenFullStory = onOpenFullStory
    }
    
    public init(vm: NewsViewModel, onNavigateHome: @escaping () -> Void, onOpenCategories: @escaping () -> Void) {
        self.vm = vm
        self.onNavigateHome = onNavigateHome
        self.onOpenCategories = onOpenCategories
        self.onOpenFullStory = nil
    }
    
    public var body: some View {
        ZStack {
            Color.screenBg(dark: vm.darkMode)
                .ignoresSafeArea()
            
            VStack(spacing: 0) {
                // Top Segmented Pill Bar (Matches Image 4)
                HStack {
                    // Left Profile Avatar
                    Button(action: onNavigateHome) {
                        ZStack {
                            Circle()
                                .fill(Color.black.opacity(0.7))
                                .frame(width: 38, height: 38)
                            Image(systemName: "person.fill")
                                .font(.system(size: 16))
                                .foregroundColor(.white)
                        }
                    }
                    
                    Spacer()
                    
                    // Center Segmented Toggle Pill (Matches Image 4: న్యూస్ | చదవనివి [14])
                    HStack(spacing: 0) {
                        Button(action: { vm.showOnlyUnread = false }) {
                            Text("న్యూస్")
                                .font(.system(size: 13, weight: .bold))
                                .foregroundColor(!vm.showOnlyUnread ? .black : Color.textSecondary(dark: vm.darkMode))
                                .padding(.horizontal, 14)
                                .padding(.vertical, 6)
                                .background(!vm.showOnlyUnread ? Color.naaGold : Color.clear)
                                .clipShape(Capsule())
                        }
                        
                        Button(action: { vm.showOnlyUnread = true }) {
                            HStack(spacing: 4) {
                                Text("చదవనివి")
                                    .font(.system(size: 13, weight: .bold))
                                    .foregroundColor(vm.showOnlyUnread ? .black : Color.textSecondary(dark: vm.darkMode))
                                
                                if vm.unreadCount > 0 {
                                    Text("\(vm.unreadCount)")
                                        .font(.system(size: 10, weight: .heavy))
                                        .foregroundColor(.white)
                                        .padding(.horizontal, 5)
                                        .padding(.vertical, 1)
                                        .background(Color.naaRed)
                                        .clipShape(Capsule())
                                }
                            }
                            .padding(.horizontal, 10)
                            .padding(.vertical, 6)
                            .background(vm.showOnlyUnread ? Color.naaGold : Color.clear)
                            .clipShape(Capsule())
                        }
                    }
                    .padding(3)
                    .background(Color.black.opacity(0.6))
                    .clipShape(Capsule())
                    
                    Spacer()
                    
                    // Bookmark Quick Action
                    Button(action: {
                        if let currentItem = currentStory {
                            vm.newsStore.toggleBookmark(currentItem)
                        }
                    }) {
                        ZStack {
                            Circle()
                                .fill(Color.black.opacity(0.7))
                                .frame(width: 38, height: 38)
                            Image(systemName: (currentStory != nil && vm.newsStore.isBookmarked(currentStory!.id)) ? "bookmark.fill" : "bookmark")
                                .font(.system(size: 15))
                                .foregroundColor(Color.naaGold)
                        }
                    }
                }
                .padding(.horizontal, 16)
                .padding(.top, 8)
                .padding(.bottom, 6)
                
                // Story Pager (Matches Image 4)
                if vm.filteredItems.isEmpty {
                    VStack(spacing: 12) {
                        Spacer()
                        Image(systemName: "tray.fill")
                            .font(.system(size: 44))
                            .foregroundColor(Color.textSecondary(dark: vm.darkMode))
                        Text(vm.showOnlyUnread ? "అన్ని వార్తలు చదివారు!" : "వార్తలు లోడ్ అవుతున్నాయి..")
                            .font(.system(size: 16, weight: .semibold))
                            .foregroundColor(Color.textPrimary(dark: vm.darkMode))
                        Spacer()
                    }
                } else {
                    TabView(selection: $vm.activeStoryIndex) {
                        ForEach(Array(vm.filteredItems.enumerated()), id: \.element.id) { index, item in
                            SingleNewsStoryCard(
                                item: item,
                                index: index,
                                totalCount: vm.filteredItems.count,
                                vm: vm,
                                onOpenFullStory: {
                                    self.selectedStory = item
                                    self.showFullStorySheet = true
                                    vm.newsStore.markAsRead(item.id)
                                }
                            )
                            .tag(index)
                            .onAppear {
                                vm.newsStore.markAsRead(item.id)
                            }
                        }
                    }
                    .tabViewStyle(.page(indexDisplayMode: .never))
                }
                
                // Bottom Bar (Matches Image 4: ← హోమ్ | తాజా వార్తలు ⌃ | ⟳ రీలోడ్)
                HStack {
                    // Left: ← హోమ్
                    Button(action: onNavigateHome) {
                        HStack(spacing: 6) {
                            Image(systemName: "arrow.left")
                                .font(.system(size: 14, weight: .bold))
                            Text("హోమ్")
                                .font(.system(size: 14, weight: .bold))
                        }
                        .foregroundColor(Color.naaGold)
                    }
                    
                    Spacer()
                    
                    // Center: Category Dropdown Pill
                    Button(action: onOpenCategories) {
                        HStack(spacing: 4) {
                            let label = Categories.list[safe: vm.selectedCategoryIndex]?.label ?? "తాజా వార్తలు"
                            Text(label)
                                .font(.system(size: 13, weight: .bold))
                                .foregroundColor(Color.textPrimary(dark: vm.darkMode))
                            Image(systemName: "chevron.up")
                                .font(.system(size: 11, weight: .bold))
                                .foregroundColor(Color.naaGold)
                        }
                        .padding(.horizontal, 14)
                        .padding(.vertical, 7)
                        .background(Color.textSecondary(dark: vm.darkMode).opacity(0.12))
                        .cornerRadius(16)
                    }
                    
                    Spacer()
                    
                    // Right: ⟳ రీలోడ్
                    Button(action: {
                        Task { await vm.loadNews() }
                    }) {
                        HStack(spacing: 4) {
                            Image(systemName: "arrow.clockwise")
                                .font(.system(size: 14, weight: .bold))
                            Text("రీలోడ్")
                                .font(.system(size: 14, weight: .bold))
                        }
                        .foregroundColor(Color.textPrimary(dark: vm.darkMode))
                    }
                }
                .padding(.horizontal, 20)
                .padding(.vertical, 10)
                .background(Color.surface(dark: vm.darkMode))
                .shadow(color: Color.black.opacity(0.08), radius: 6, x: 0, y: -2)
            }
        }
        .sheet(item: $selectedStory) { item in
            FullStorySheetView(vm: vm, item: item) {
                selectedStory = nil
            }
        }
    }
    
    private var currentStory: NewsItem? {
        guard vm.activeStoryIndex >= 0 && vm.activeStoryIndex < vm.filteredItems.count else { return nil }
        return vm.filteredItems[vm.activeStoryIndex]
    }
}

// Single News Story Card (Matches Image 4)
struct SingleNewsStoryCard: View {
    let item: NewsItem
    let index: Int
    let totalCount: Int
    @ObservedObject var vm: NewsViewModel
    let onOpenFullStory: () -> Void
    
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            // 1. Hero Image with NAA TV Badge (NO PLAY BUTTON ON PHOTOS)
            ZStack(alignment: .bottomLeading) {
                if let img = item.imageUrl, let url = URL(string: img) {
                    AsyncImage(url: url) { phase in
                        switch phase {
                        case .success(let image):
                            image.resizable()
                                .scaledToFill()
                                .frame(maxWidth: .infinity)
                                .frame(height: 250)
                                .clipped()
                        default:
                            Rectangle()
                                .fill(Color.gray.opacity(0.2))
                                .frame(height: 250)
                        }
                    }
                } else {
                    Rectangle()
                        .fill(Color.gray.opacity(0.2))
                        .frame(height: 250)
                }
                
                // Play button strictly shown ONLY on actual video stories
                if item.isVideo {
                    ZStack {
                        Circle()
                            .fill(Color.black.opacity(0.65))
                            .frame(width: 54, height: 54)
                        Image(systemName: "play.fill")
                            .font(.system(size: 22))
                            .foregroundColor(.white)
                    }
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                }
                
                // NAA TV Logo Badge in Bottom-Left (Matches Image 4)
                HStack(spacing: 5) {
                    Image("naa_tv_logo")
                        .resizable()
                        .scaledToFit()
                        .frame(height: 14)
                    Text("NAA TV")
                        .font(.system(size: 10, weight: .black))
                        .foregroundColor(.white)
                }
                .padding(.horizontal, 6)
                .padding(.vertical, 3)
                .background(Color.black.opacity(0.85))
                .cornerRadius(4)
                .overlay(RoundedRectangle(cornerRadius: 4).stroke(Color.naaGold, lineWidth: 1))
                .padding(10)
            }
            .frame(maxWidth: .infinity)
            .frame(height: 250)
            
            // 2. Headline & Summary (Admin Customizable Typography)
            VStack(alignment: .leading, spacing: 8) {
                Text(item.title)
                    .font(.system(size: vm.adminStore.settings.headlineFontSize, weight: .bold, design: vm.adminStore.settings.fontDesign))
                    .foregroundColor(Color.textPrimary(dark: vm.darkMode))
                    .lineLimit(2)
                    .padding(.top, 10)
                
                Text(item.summary)
                    .font(.system(size: vm.adminStore.settings.bodyFontSize, weight: .regular, design: vm.adminStore.settings.fontDesign))
                    .foregroundColor(Color.textPrimary(dark: vm.darkMode).opacity(0.85))
                    .lineLimit(6)
                    .lineSpacing(4)
            }
            .padding(.horizontal, 16)
            
            Spacer()
            
            // 3. Metadata & Center Green Progress Bar (Matches Image 4)
            HStack {
                Text("🕒 \(item.timeAgo)")
                    .font(.system(size: 11, weight: .medium))
                    .foregroundColor(Color.textSecondary(dark: vm.darkMode))
                
                Spacer()
                
                // Green Progress Indicator Pill
                Capsule()
                    .fill(Color.naaEmerald)
                    .frame(width: 32, height: 4)
                
                Spacer()
                
                Text("\(index + 1) of \(totalCount) Pages")
                    .font(.system(size: 11, weight: .medium))
                    .foregroundColor(Color.textSecondary(dark: vm.darkMode))
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 8)
            
            Divider()
                .padding(.horizontal, 16)
            
            // 4. Action Row (Matches Image 4)
            HStack(spacing: 20) {
                // Thumbs Up
                Button(action: { vm.newsStore.incrementLike(for: item.id) }) {
                    HStack(spacing: 4) {
                        Image(systemName: "hand.thumbsup")
                            .font(.system(size: 15))
                        Text("\(vm.newsStore.getLikes(for: item.id))")
                            .font(.system(size: 12, weight: .semibold))
                    }
                    .foregroundColor(Color.textPrimary(dark: vm.darkMode))
                }
                
                // Thumbs Down
                Button(action: { vm.newsStore.incrementDislike(for: item.id) }) {
                    HStack(spacing: 4) {
                        Image(systemName: "hand.thumbsdown")
                            .font(.system(size: 15))
                        Text("\(vm.newsStore.getDislikes(for: item.id))")
                            .font(.system(size: 12, weight: .semibold))
                    }
                    .foregroundColor(Color.textPrimary(dark: vm.darkMode))
                }
                
                // Bookmark
                Button(action: { vm.newsStore.toggleBookmark(item) }) {
                    Image(systemName: vm.newsStore.isBookmarked(item.id) ? "bookmark.fill" : "bookmark")
                        .font(.system(size: 16))
                        .foregroundColor(vm.newsStore.isBookmarked(item.id) ? Color.naaGold : Color.textPrimary(dark: vm.darkMode))
                }
                
                // Share to WhatsApp (Reliable heading + image + link)
                Button(action: { vm.shareToWhatsApp(item: item) }) {
                    Image(systemName: "square.and.arrow.up")
                        .font(.system(size: 16))
                        .foregroundColor(Color.textPrimary(dark: vm.darkMode))
                }
                
                Spacer()
                
                // Full Story Button: పూర్ణ వార్త →
                Button(action: onOpenFullStory) {
                    HStack(spacing: 4) {
                        Text(AppStrings.fullStory(lang: vm.currentLanguage))
                            .font(.system(size: 13, weight: .bold))
                    }
                    .foregroundColor(Color.naaGold)
                }
            }
            .padding(.horizontal, 18)
            .padding(.vertical, 10)
        }
        .background(Color.surface(dark: vm.darkMode))
    }
}

extension Collection {
    subscript(safe index: Index) -> Element? {
        indices.contains(index) ? self[index] : nil
    }
}
