import SwiftUI

public struct ReferralDialogView: View {
    @ObservedObject var vm: NewsViewModel
    public var onDismiss: () -> Void
    @State private var copied: Bool = false
    
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
                // Header Icon & Title
                HStack(spacing: 8) {
                    Text("🎁")
                        .font(.system(size: 24))
                    Text("మిత్రులను ఆహ్వానించండి")
                        .font(.system(size: 19, weight: .bold))
                        .foregroundColor(Color.textPrimary(dark: vm.darkMode))
                }
                
                Text("మీ స్నేహితులకు NAA TV తెలుగు న్యూస్ యాప్‌ను పరిచయం చేసి రివార్డులు పొందండి!")
                    .font(.system(size: 13, weight: .regular))
                    .foregroundColor(Color.textSecondary(dark: vm.darkMode))
                    .multilineTextAlignment(.center)
                    .padding(.horizontal, 8)
                
                // Gold Framed Referral Code Card
                VStack(spacing: 8) {
                    Text("మీ ప్రత్యేక రెఫరల్ కోడ్ (Unique Referral Code)")
                        .font(.system(size: 12, weight: .medium))
                        .foregroundColor(Color.textSecondary(dark: vm.darkMode))
                    
                    let code = vm.authStore.currentUser?.referralCode ?? "NAA-ADMI-8514"
                    Text(code)
                        .font(.system(size: 22, weight: .heavy, design: .monospaced))
                        .foregroundColor(Color.naaGold)
                        .padding(.vertical, 4)
                }
                .frame(maxWidth: .infinity)
                .padding(.vertical, 14)
                .background(Color.naaGold.opacity(0.08))
                .cornerRadius(14)
                .overlay(
                    RoundedRectangle(cornerRadius: 14)
                        .stroke(Color.naaGold, lineWidth: 1.5)
                )
                
                // Stats Banner
                HStack {
                    Text("ఆహ్వానించిన మిత్రులు: 0")
                        .font(.system(size: 12, weight: .semibold))
                        .foregroundColor(Color.textPrimary(dark: vm.darkMode))
                    
                    Spacer()
                    
                    Text("పాయింట్లు: \(vm.authStore.currentUser?.rewardPoints ?? 50) NAA")
                        .font(.system(size: 12, weight: .bold))
                        .foregroundColor(Color.naaEmerald)
                }
                .padding(.horizontal, 14)
                .padding(.vertical, 10)
                .background(Color.textSecondary(dark: vm.darkMode).opacity(0.08))
                .cornerRadius(10)
                
                // Copy Code Button
                Button(action: {
                    let code = vm.authStore.currentUser?.referralCode ?? "NAA-ADMI-8514"
                    UIPasteboard.general.string = code
                    copied = true
                    DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                        copied = false
                    }
                }) {
                    HStack(spacing: 8) {
                        Image(systemName: copied ? "checkmark.circle.fill" : "doc.on.doc")
                        Text(copied ? "కోడ్ కాపీ చేయబడింది! (Copied)" : "కోడ్ కాపీ చేయండి (Copy Code)")
                            .font(.system(size: 14, weight: .semibold))
                    }
                    .foregroundColor(Color.naaGold)
                    .frame(maxWidth: .infinity)
                    .frame(height: 44)
                    .background(Color.naaGold.opacity(0.12))
                    .cornerRadius(10)
                }
                
                // WhatsApp Invite Button
                Button(action: {
                    let code = vm.authStore.currentUser?.referralCode ?? "NAA-ADMI-8514"
                    let text = "NAA TV తెలుగు యాప్‌ను డౌన్‌లోడ్ చేసుకోండి! నా ప్రత్యేక రెఫరల్ కోడ్: \(code) ను ఉపయోగించి ఉచితంగా రివార్డులు పొందండి: \(NaaTvBrand.site)"
                    if let encoded = text.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed),
                       let url = URL(string: "whatsapp://send?text=\(encoded)") {
                        if UIApplication.shared.canOpenURL(url) {
                            UIApplication.shared.open(url)
                        }
                    }
                }) {
                    HStack(spacing: 8) {
                        Image(systemName: "square.and.arrow.up")
                        Text("వాట్సాప్‌లో ఆహ్వానించండి")
                            .font(.system(size: 14, weight: .bold))
                    }
                    .foregroundColor(.white)
                    .frame(maxWidth: .infinity)
                    .frame(height: 46)
                    .background(Color.naaEmerald)
                    .cornerRadius(12)
                }
                
                // Close Button
                Button(action: onDismiss) {
                    Text("ముగించు (Close)")
                        .font(.system(size: 14, weight: .bold))
                        .foregroundColor(Color.naaGold)
                        .padding(.top, 4)
                }
            }
            .padding(22)
            .background(Color.surface(dark: vm.darkMode))
            .cornerRadius(24)
            .shadow(color: Color.black.opacity(0.3), radius: 20, x: 0, y: 10)
            .padding(.horizontal, 28)
        }
    }
}
