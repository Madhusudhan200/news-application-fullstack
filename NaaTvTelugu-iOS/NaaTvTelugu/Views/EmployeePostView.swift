import SwiftUI

public struct EmployeePostView: View {
    @ObservedObject var viewModel: NewsViewModel
    var onDismiss: () -> Void
    
    @State private var title: String = ""
    @State private var category: String = "తాజా వార్తలు"
    @State private var summary: String = ""
    @State private var contentHtml: String = ""
    @State private var imageUrl: String = ""
    @State private var videoUrl: String = ""
    @State private var isVideo: Bool = false
    @State private var submittedSuccess: Bool = false
    
    public init(viewModel: NewsViewModel, onDismiss: @escaping () -> Void) {
        self.viewModel = viewModel
        self.onDismiss = onDismiss
    }
    
    public var body: some View {
        NavigationView {
            ZStack {
                Theme.darkBackground.ignoresSafeArea()
                
                VStack(spacing: 0) {
                    // Header
                    headerBar
                    
                    ScrollView {
                        VStack(alignment: .leading, spacing: 16) {
                            if submittedSuccess {
                                successBanner
                            }
                            
                            // Reporter Info & Location Banner
                            reporterLocationBanner
                            
                            // Title
                            fieldSection(title: "వార్త శీర్షిక (Headline)") {
                                TextField("స్పష్టమైన శీర్షిక రాయండి...", text: $title)
                                    .padding(12)
                                    .background(Theme.navyCard)
                                    .cornerRadius(8)
                                    .foregroundColor(.white)
                            }
                            
                            // Category Picker
                            fieldSection(title: "కేటగిరీ (Category)") {
                                Picker("వర్గం", selection: $category) {
                                    ForEach(Categories.list, id: \.label) { cat in
                                        Text(cat.label).tag(cat.label)
                                    }
                                }
                                .pickerStyle(MenuPickerStyle())
                                .padding(8)
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .background(Theme.navyCard)
                                .cornerRadius(8)
                            }
                            
                            // Summary
                            fieldSection(title: "ముఖ్యాంశాలు / సారాంశం (Summary)") {
                                TextEditor(text: $summary)
                                    .frame(height: 70)
                                    .padding(6)
                                    .background(Theme.navyCard)
                                    .cornerRadius(8)
                                    .foregroundColor(.white)
                            }
                            
                            // Detailed Story
                            fieldSection(title: "వివరణాత్మక కథనం (Full Story)") {
                                TextEditor(text: $contentHtml)
                                    .frame(height: 110)
                                    .padding(6)
                                    .background(Theme.navyCard)
                                    .cornerRadius(8)
                                    .foregroundColor(.white)
                            }
                            
                            // Image URL
                            fieldSection(title: "చిత్రం URL (Image URL)") {
                                TextField("https://images.unsplash.com/...", text: $imageUrl)
                                    .padding(12)
                                    .background(Theme.navyCard)
                                    .cornerRadius(8)
                                    .foregroundColor(.white)
                            }
                            
                            // Video Toggle
                            Toggle(isOn: $isVideo) {
                                Text("ఇది వీడియో స్టోరీనా? (Video News / Reel)")
                                    .font(.system(size: 13, weight: .semibold))
                                    .foregroundColor(.white)
                            }
                            .padding(.vertical, 4)
                            
                            if isVideo {
                                fieldSection(title: "వీడియో లింక్ (Video Stream URL)") {
                                    TextField("https://...", text: $videoUrl)
                                        .padding(12)
                                        .background(Theme.navyCard)
                                        .cornerRadius(8)
                                        .foregroundColor(.white)
                                }
                            }
                            
                            // Submit Button
                            Button(action: submitPost) {
                                HStack(spacing: 8) {
                                    Image(systemName: "paperplane.fill")
                                    Text("సమీక్ష కోసం పంపండి (Submit Story)")
                                }
                                .font(.system(size: 15, weight: .bold))
                                .foregroundColor(.black)
                                .frame(maxWidth: .infinity)
                                .padding(.vertical, 14)
                                .background(Theme.goldColor)
                                .cornerRadius(10)
                            }
                            .disabled(title.trimmingCharacters(in: .whitespaces).isEmpty)
                            .opacity(title.trimmingCharacters(in: .whitespaces).isEmpty ? 0.5 : 1.0)
                            .padding(.top, 8)
                        }
                        .padding(16)
                    }
                }
            }
            .navigationBarHidden(true)
        }
    }
    
    private var headerBar: some View {
        HStack {
            Button(action: onDismiss) {
                Image(systemName: "xmark")
                    .font(.system(size: 16, weight: .bold))
                    .foregroundColor(.white)
            }
            
            Text("రిపోర్టర్ డెస్క్ (Submit News)")
                .font(.system(size: 16, weight: .bold))
                .foregroundColor(.white)
                .padding(.leading, 8)
            
            Spacer()
            
            Text("REPORTER")
                .font(.system(size: 10, weight: .heavy))
                .foregroundColor(.black)
                .padding(.horizontal, 8)
                .padding(.vertical, 3)
                .background(Color(hex: "38BDF8"))
                .cornerRadius(4)
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
        .background(Theme.navyPrimary)
    }
    
    private var reporterLocationBanner: some View {
        HStack(spacing: 8) {
            Image(systemName: "location.circle.fill")
                .font(.system(size: 16))
                .foregroundColor(Theme.goldColor)
            
            VStack(alignment: .leading, spacing: 2) {
                Text("రిపోర్టింగ్ లొకేషన్:")
                    .font(.system(size: 10))
                    .foregroundColor(.white.opacity(0.6))
                
                Text(viewModel.locationManager.userLocation)
                    .font(.system(size: 12, weight: .semibold))
                    .foregroundColor(.white)
            }
            
            Spacer()
            
            Text("GPS వెరిఫైడ్")
                .font(.system(size: 9, weight: .bold))
                .foregroundColor(.green)
                .padding(.horizontal, 6)
                .padding(.vertical, 2)
                .background(Color.green.opacity(0.15))
                .cornerRadius(4)
        }
        .padding(10)
        .background(Theme.navyCard)
        .cornerRadius(8)
    }
    
    private var successBanner: some View {
        HStack(spacing: 10) {
            Image(systemName: "checkmark.circle.fill")
                .foregroundColor(.green)
            
            VStack(alignment: .leading, spacing: 2) {
                Text("వార్త విజయవంతంగా పంపబడింది!")
                    .font(.system(size: 13, weight: .bold))
                    .foregroundColor(.green)
                
                Text("అడ్మిన్ ఆమోదం పొందిన తర్వాత లైవ్ అవుతుంది.")
                    .font(.system(size: 11))
                    .foregroundColor(.white.opacity(0.8))
            }
        }
        .padding(12)
        .background(Color.green.opacity(0.15))
        .cornerRadius(8)
    }
    
    private func fieldSection<Content: View>(title: String, @ViewBuilder content: () -> Content) -> some View {
        VStack(alignment: .leading, spacing: 6) {
            Text(title)
                .font(.system(size: 12, weight: .semibold))
                .foregroundColor(.white.opacity(0.85))
            
            content()
        }
    }
    
    private func submitPost() {
        let user = viewModel.authStore.currentUser
        let newPost = CustomPost(
            id: Int(Date().timeIntervalSince1970),
            title: title,
            summary: summary.isEmpty ? title : summary,
            imageUrl: imageUrl.isEmpty ? nil : imageUrl,
            videoUrl: videoUrl.isEmpty ? nil : videoUrl,
            category: category,
            contentHtml: contentHtml.isEmpty ? "<p>\(summary)</p>" : contentHtml,
            authorName: user?.name ?? "Reporter",
            authorEmail: user?.email ?? "reporter@naatvtelugu.com",
            authorRole: user?.role ?? .employee,
            status: (user?.role == .admin) ? .approved : .pending,
            isVideo: isVideo
        )
        viewModel.newsStore.addCustomPost(newPost)
        submittedSuccess = true
        title = ""
        summary = ""
        contentHtml = ""
        imageUrl = ""
        videoUrl = ""
        isVideo = false
    }
}
