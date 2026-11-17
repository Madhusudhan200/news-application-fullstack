import SwiftUI
import AVFoundation

public struct FullStorySheetView: View {
    @ObservedObject var vm: NewsViewModel
    public let item: NewsItem
    public var onDismiss: () -> Void
    
    @State private var isSpeaking: Bool = false
    @State private var synthesizer = AVSpeechSynthesizer()
    
    public init(item: NewsItem, viewModel: NewsViewModel) {
        self.vm = viewModel
        self.item = item
        self.onDismiss = {}
    }
    
    public init(vm: NewsViewModel, item: NewsItem, onDismiss: @escaping () -> Void) {
        self.vm = vm
        self.item = item
        self.onDismiss = onDismiss
    }
    
    public var body: some View {
        NavigationView {
            ScrollView {
                VStack(alignment: .leading, spacing: 16) {
                    // Category & Date Pill
                    HStack {
                        Text(item.category)
                            .font(.system(size: 12, weight: .bold))
                            .foregroundColor(.white)
                            .padding(.horizontal, 10)
                            .padding(.vertical, 4)
                            .background(Color.naaGold)
                            .cornerRadius(6)
                        
                        Text("🕒 \(item.timeAgo)")
                            .font(.system(size: 12))
                            .foregroundColor(Color.textSecondary(dark: vm.darkMode))
                        
                        Spacer()
                        
                        // Audio Speech Synthesizer Button
                        Button(action: toggleSpeech) {
                            HStack(spacing: 4) {
                                Image(systemName: isSpeaking ? "stop.fill" : "speaker.wave.2.fill")
                                Text(isSpeaking ? "ఆపండి (Stop)" : "వినండి (Listen)")
                                    .font(.system(size: 12, weight: .bold))
                            }
                            .foregroundColor(.white)
                            .padding(.horizontal, 10)
                            .padding(.vertical, 4)
                            .background(Color.naaEmerald)
                            .cornerRadius(6)
                        }
                    }
                    
                    // Headline
                    Text(item.title)
                        .font(.system(size: vm.adminStore.settings.headlineFontSize, weight: .bold, design: vm.adminStore.settings.fontDesign))
                        .foregroundColor(Color.textPrimary(dark: vm.darkMode))
                        .lineSpacing(4)
                    
                    // Hero Image
                    if let img = item.imageUrl, let url = URL(string: img) {
                        AsyncImage(url: url) { phase in
                            switch phase {
                            case .success(let image):
                                image.resizable()
                                    .scaledToFill()
                                    .frame(maxWidth: .infinity)
                                    .frame(height: 220)
                                    .clipped()
                                    .cornerRadius(12)
                            default:
                                RoundedRectangle(cornerRadius: 12)
                                    .fill(Color.gray.opacity(0.2))
                                    .frame(height: 220)
                            }
                        }
                    }
                    
                    // Full Body Content
                    Text(cleanHtml(item.contentHtml))
                        .font(.system(size: vm.adminStore.settings.bodyFontSize, weight: .regular, design: vm.adminStore.settings.fontDesign))
                        .foregroundColor(Color.textPrimary(dark: vm.darkMode).opacity(0.9))
                        .lineSpacing(6)
                    
                    Divider()
                        .padding(.vertical, 8)
                    
                    // Bottom Actions
                    HStack(spacing: 16) {
                        Button(action: { vm.newsStore.toggleBookmark(item) }) {
                            HStack {
                                Image(systemName: vm.newsStore.isBookmarked(item.id) ? "bookmark.fill" : "bookmark")
                                Text(vm.newsStore.isBookmarked(item.id) ? "సేవ్ చేయబడింది" : "సేవ్ చేయండి")
                                    .font(.system(size: 13, weight: .semibold))
                            }
                            .foregroundColor(Color.naaGold)
                        }
                        
                        Spacer()
                        
                        Button(action: { vm.shareToWhatsApp(item: item) }) {
                            HStack {
                                Image(systemName: "square.and.arrow.up")
                                Text("వాట్సాప్ షేర్")
                                    .font(.system(size: 13, weight: .bold))
                            }
                            .foregroundColor(.white)
                            .padding(.horizontal, 16)
                            .padding(.vertical, 8)
                            .background(Color.naaEmerald)
                            .cornerRadius(8)
                        }
                    }
                }
                .padding(18)
            }
            .background(Color.screenBg(dark: vm.darkMode))
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button(action: {
                        synthesizer.stopSpeaking(at: .immediate)
                        onDismiss()
                    }) {
                        Image(systemName: "xmark")
                            .foregroundColor(Color.textPrimary(dark: vm.darkMode))
                    }
                }
                ToolbarItem(placement: .principal) {
                    Text("పూర్తి కథనం (Full Article)")
                        .font(.system(size: 16, weight: .bold))
                        .foregroundColor(Color.textPrimary(dark: vm.darkMode))
                }
            }
        }
    }
    
    private func toggleSpeech() {
        if isSpeaking {
            synthesizer.stopSpeaking(at: .immediate)
            isSpeaking = false
        } else {
            let utterance = AVSpeechUtterance(string: "\(item.title). \(cleanHtml(item.summary))")
            utterance.voice = AVSpeechSynthesisVoice(language: "te-IN") ?? AVSpeechSynthesisVoice(language: "en-IN")
            utterance.rate = 0.45
            synthesizer.speak(utterance)
            isSpeaking = true
        }
    }
    
    private func cleanHtml(_ text: String) -> String {
        return text.replacingOccurrences(of: "<[^>]+>", with: "", options: .regularExpression, range: nil)
            .trimmingCharacters(in: .whitespacesAndNewlines)
    }
}
