import Foundation

public class AdminSettingsStore: ObservableObject {
    private let settingsKey = "naa_tv_admin_settings"
    private let editedStoriesKey = "naa_tv_edited_stories"
    
    @Published public var settings: AdminSettings
    @Published public var editedStories: [Int: EditedStory] = [:]
    
    public init() {
        self.settings = Self.loadSettings(key: settingsKey)
        self.editedStories = Self.loadEditedStories(key: editedStoriesKey)
    }
    
    private static func loadSettings(key: String) -> AdminSettings {
        guard let data = UserDefaults.standard.data(forKey: key) else {
            return AdminSettings()
        }
        return (try? JSONDecoder().decode(AdminSettings.self, from: data)) ?? AdminSettings()
    }
    
    private static func loadEditedStories(key: String) -> [Int: EditedStory] {
        guard let data = UserDefaults.standard.data(forKey: key) else { return [:] }
        return (try? JSONDecoder().decode([Int: EditedStory].self, from: data)) ?? [:]
    }
    
    public func updateSettings(_ newSettings: AdminSettings) {
        self.settings = newSettings
        if let data = try? JSONEncoder().encode(newSettings) {
            UserDefaults.standard.set(data, forKey: settingsKey)
        }
    }
    
    public func saveEditedStory(_ edited: EditedStory) {
        self.editedStories[edited.id] = edited
        if let data = try? JSONEncoder().encode(editedStories) {
            UserDefaults.standard.set(data, forKey: editedStoriesKey)
        }
    }
}
