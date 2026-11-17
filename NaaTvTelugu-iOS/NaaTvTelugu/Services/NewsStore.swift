import Foundation

public class NewsStore: ObservableObject {
    private let bookmarksKey = "naa_tv_bookmarks"
    private let readStoriesKey = "naa_tv_read_stories"
    private let customPostsKey = "naa_tv_custom_posts"
    
    @Published public var savedStories: [NewsItem] = []
    @Published public var readStoryIds: Set<Int> = []
    @Published public var customPosts: [CustomPost] = []
    @Published public var likesMap: [Int: Int] = [:]
    @Published public var dislikesMap: [Int: Int] = [:]
    
    public init() {
        self.savedStories = loadBookmarks()
        self.readStoryIds = loadReadStories()
        self.customPosts = loadCustomPosts()
        
        // Seed default demo custom post if empty
        if customPosts.isEmpty {
            let demo = CustomPost(
                id: 99101,
                title: "పొన్నమండలో ఘనంగా స్థానిక ఉత్సవాలు ప్రారంభం",
                summary: "కోనసీమ జిల్లా పొన్నమండ గ్రామంలో గ్రామదేవత ఉత్సవాలు ఘనంగా ప్రారంభమయ్యాయి. భక్తులు భారీ సంఖ్యలో తరలివచ్చారు.",
                imageUrl: "https://images.unsplash.com/photo-1544717305-2782549b5136?w=800",
                category: "తాజా వార్తలు",
                contentHtml: "<p>కోనసీమ జిల్లా పొన్నమండ గ్రామంలో వార్షిక ఉత్సవాలు అత్యంత వైభవంగా జరుగుతున్నాయి.</p>",
                authorName: "Reporter Ravi",
                authorEmail: "reporter@naatvtelugu.com",
                authorRole: .employee,
                status: .approved
            )
            self.customPosts = [demo]
            saveCustomPosts(self.customPosts)
        }
    }
    
    private func loadBookmarks() -> [NewsItem] {
        guard let data = UserDefaults.standard.data(forKey: bookmarksKey) else { return [] }
        return (try? JSONDecoder().decode([NewsItem].self, from: data)) ?? []
    }
    
    private func saveBookmarks(_ items: [NewsItem]) {
        if let data = try? JSONEncoder().encode(items) {
            UserDefaults.standard.set(data, forKey: bookmarksKey)
        }
    }
    
    private func loadReadStories() -> Set<Int> {
        let array = UserDefaults.standard.array(forKey: readStoriesKey) as? [Int] ?? []
        return Set(array)
    }
    
    private func saveReadStories(_ set: Set<Int>) {
        UserDefaults.standard.set(Array(set), forKey: readStoriesKey)
    }
    
    private func loadCustomPosts() -> [CustomPost] {
        guard let data = UserDefaults.standard.data(forKey: customPostsKey) else { return [] }
        return (try? JSONDecoder().decode([CustomPost].self, from: data)) ?? []
    }
    
    private func saveCustomPosts(_ posts: [CustomPost]) {
        if let data = try? JSONEncoder().encode(posts) {
            UserDefaults.standard.set(data, forKey: customPostsKey)
        }
    }
    
    public func isBookmarked(_ id: Int) -> Bool {
        savedStories.contains(where: { $0.id == id })
    }
    
    public func toggleBookmark(_ item: NewsItem) {
        if let idx = savedStories.firstIndex(where: { $0.id == item.id }) {
            savedStories.remove(at: idx)
        } else {
            savedStories.append(item)
        }
        saveBookmarks(savedStories)
    }
    
    public func isRead(_ id: Int) -> Bool {
        readStoryIds.contains(id)
    }
    
    public func markAsRead(_ id: Int) {
        readStoryIds.insert(id)
        saveReadStories(readStoryIds)
    }
    
    public func addCustomPost(_ post: CustomPost) {
        customPosts.insert(post, at: 0)
        saveCustomPosts(customPosts)
    }
    
    public func approvePost(_ id: Int) {
        if let idx = customPosts.firstIndex(where: { $0.id == id }) {
            customPosts[idx].status = .approved
            saveCustomPosts(customPosts)
        }
    }
    
    public func rejectPost(_ id: Int) {
        if let idx = customPosts.firstIndex(where: { $0.id == id }) {
            customPosts[idx].status = .rejected
            saveCustomPosts(customPosts)
        }
    }
    
    public func getLikes(for id: Int) -> Int {
        likesMap[id] ?? 380
    }
    
    public func getDislikes(for id: Int) -> Int {
        dislikesMap[id] ?? 22
    }
    
    public func incrementLike(for id: Int) {
        likesMap[id] = getLikes(for: id) + 1
    }
    
    public func incrementDislike(for id: Int) {
        dislikesMap[id] = getDislikes(for: id) + 1
    }
}
