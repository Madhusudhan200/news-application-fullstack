import Foundation
import SwiftUI
import Combine

@MainActor
public class NewsViewModel: ObservableObject {
    public let authStore = AuthStore()
    public let newsStore = NewsStore()
    public let adminStore = AdminSettingsStore()
    public let locationManager = LocationManager()
    
    @Published public var rawItems: [NewsItem] = []
    @Published public var selectedCategoryIndex: Int = 0
    @Published public var showOnlyUnread: Bool = false
    @Published public var currentLanguage: AppLanguage = .telugu
    @Published public var darkMode: Bool = false
    @Published public var searchQuery: String = ""
    @Published public var isLoading: Bool = false
    @Published public var activeStoryIndex: Int = 0
    
    private var cancellables = Set<AnyCancellable>()
    
    public init() {
        // Observe stores to trigger UI updates
        authStore.objectWillChange.sink { [weak self] _ in self?.objectWillChange.send() }.store(in: &cancellables)
        newsStore.objectWillChange.sink { [weak self] _ in self?.objectWillChange.send() }.store(in: &cancellables)
        adminStore.objectWillChange.sink { [weak self] _ in self?.objectWillChange.send() }.store(in: &cancellables)
        locationManager.objectWillChange.sink { [weak self] _ in self?.objectWillChange.send() }.store(in: &cancellables)
        
        Task {
            await loadNews()
        }
    }
    
    public func loadNews() async {
        isLoading = true
        let fetched = await NewsRepository.shared.fetchLiveNews()
        self.rawItems = fetched
        self.isLoading = false
    }
    
    // Combine fetched stories with approved custom posts and admin edits
    public var allCombinedItems: [NewsItem] {
        let approvedCustom = newsStore.customPosts
            .filter { $0.status == .approved }
            .map { $0.toNewsItem() }
        
        let merged = approvedCustom + rawItems
        
        // Apply Admin story edits if any
        return merged.map { item in
            if let edited = adminStore.editedStories[item.id] {
                var copy = item
                copy.title = edited.title
                copy.summary = edited.summary
                copy.category = edited.category
                copy.contentHtml = edited.contentHtml
                return copy
            }
            return item
        }
    }
    
    public var filteredItems: [NewsItem] {
        var result = allCombinedItems
        
        // Category filtering (0 is "తాజా వార్తలు" / All)
        if selectedCategoryIndex > 0 && selectedCategoryIndex < Categories.list.count {
            let cat = Categories.list[selectedCategoryIndex]
            result = result.filter { item in
                item.category.lowercased().contains(cat.label.lowercased()) ||
                (cat.slug != nil && item.category.lowercased().contains(cat.slug!))
            }
        }
        
        // Unread filtering
        if showOnlyUnread {
            result = result.filter { !newsStore.isRead($0.id) }
        }
        
        // Search query
        let query = searchQuery.trimmingCharacters(in: .whitespacesAndNewlines).lowercased()
        if !query.isEmpty {
            result = result.filter {
                $0.title.lowercased().contains(query) ||
                $0.summary.lowercased().contains(query) ||
                $0.category.lowercased().contains(query)
            }
        }
        
        return result
    }
    
    public var unreadCount: Int {
        allCombinedItems.filter { !newsStore.isRead($0.id) }.count
    }
    
    public var reelsItems: [NewsItem] {
        allCombinedItems.filter { $0.isVideo }
    }
    
    public var localNewsItems: [NewsItem] {
        let loc = locationManager.userLocation.split(separator: ",").first?.trimmingCharacters(in: .whitespaces) ?? "పొన్నమండ"
        return allCombinedItems.filter {
            $0.title.contains(loc) || $0.summary.contains(loc) || $0.category.contains("పొన్నమండ")
        }
    }
    
    public func selectCategory(_ index: Int) {
        self.selectedCategoryIndex = index
        self.showOnlyUnread = false
    }
    
    public func setLanguage(_ lang: AppLanguage) {
        self.currentLanguage = lang
    }
    
    public func toggleDarkMode() {
        self.darkMode.toggle()
    }
    
    public func getCategoryLabel(for slug: String?) -> String {
        guard let slug = slug else { return "తాజా వార్తలు" }
        return Categories.list.first(where: { $0.slug == slug })?.label ?? slug
    }
    
    public func getPostCount(for email: String) -> Int {
        return newsStore.customPosts.filter { $0.authorEmail.lowercased() == email.lowercased() }.count
    }
    
    // WhatsApp Share: Sends headline, image link, and app link (Without full body text)
    public func shareToWhatsApp(item: NewsItem) {
        let text = """
        *\(item.title)*
        
        📷 ఫోటో: \(item.imageUrl ?? "")
        
        📲 పూర్తి వార్తల కోసం NAA TV తెలుగు యాప్‌ను ఇన్‌స్టాల్ చేసుకోండి:
        \(NaaTvBrand.site)
        """
        
        guard let encoded = text.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed),
              let url = URL(string: "whatsapp://send?text=\(encoded)") else {
            return
        }
        
        if UIApplication.shared.canOpenURL(url) {
            UIApplication.shared.open(url)
        } else {
            // Fallback to standard iOS UIActivityViewController
            let activityVC = UIActivityViewController(activityItems: [text], applicationActivities: nil)
            if let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
               let rootVC = windowScene.windows.first?.rootViewController {
                rootVC.present(activityVC, animated: true)
            }
        }
    }
}
