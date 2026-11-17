import SwiftUI

public struct LanguageSelectionDialog: View {
    @ObservedObject var vm: NewsViewModel
    public var onDismiss: () -> Void
    
    public init(viewModel: NewsViewModel, isPresented: Binding<Bool>) {
        self.vm = viewModel
        self.onDismiss = { isPresented.wrappedValue = false }
    }
    
    public init(vm: NewsViewModel, onDismiss: @escaping () -> Void) {
        self.vm = vm
        self.onDismiss = onDismiss
    }
    
    public var body: some View {
        ZStack {
            Color.black.opacity(0.55)
                .ignoresSafeArea()
                .onTapGesture { onDismiss() }
            
            VStack(spacing: 16) {
                HStack {
                    Image(systemName: "globe")
                        .foregroundColor(Color.naaGold)
                    Text("భాషను ఎంచుకోండి (Select Language)")
                        .font(.system(size: 17, weight: .bold))
                        .foregroundColor(Color.textPrimary(dark: vm.darkMode))
                    Spacer()
                    Button(action: onDismiss) {
                        Image(systemName: "xmark.circle.fill")
                            .foregroundColor(Color.textSecondary(dark: vm.darkMode))
                    }
                }
                
                Divider()
                
                ScrollView {
                    VStack(spacing: 6) {
                        ForEach(AppLanguage.allCases) { lang in
                            let isSelected = vm.currentLanguage == lang
                            Button(action: {
                                vm.setLanguage(lang)
                                onDismiss()
                            }) {
                                HStack {
                                    VStack(alignment: .leading, spacing: 2) {
                                        Text(lang.nativeName)
                                            .font(.system(size: 15, weight: isSelected ? .bold : .semibold))
                                            .foregroundColor(isSelected ? Color.naaGold : Color.textPrimary(dark: vm.darkMode))
                                        Text(lang.englishName)
                                            .font(.system(size: 12))
                                            .foregroundColor(Color.textSecondary(dark: vm.darkMode))
                                    }
                                    Spacer()
                                    if isSelected {
                                        Image(systemName: "checkmark.circle.fill")
                                            .foregroundColor(Color.naaGold)
                                    }
                                }
                                .padding(.horizontal, 14)
                                .padding(.vertical, 10)
                                .background(isSelected ? Color.naaGold.opacity(0.12) : Color.clear)
                                .cornerRadius(10)
                            }
                        }
                    }
                }
                .frame(maxHeight: 320)
            }
            .padding(20)
            .background(Color.surface(dark: vm.darkMode))
            .cornerRadius(20)
            .shadow(color: Color.black.opacity(0.3), radius: 20, x: 0, y: 10)
            .padding(.horizontal, 28)
        }
    }
}
