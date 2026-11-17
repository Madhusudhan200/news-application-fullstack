import SwiftUI

public struct LocationPickerDialog: View {
    @ObservedObject var vm: NewsViewModel
    public var onDismiss: () -> Void
    
    private let locations = [
        "పొన్నమండ, కోనసీమ",
        "హైదరాబాద్, తెలంగాణ",
        "విజయవాడ, కృష్ణా",
        "రాజమండ్రి, తూర్పు గోదావరి",
        "విశాఖపట్నం, విశాఖ",
        "తిరుపతి, తిరుపతి జిల్లా",
        "గుంటూరు, గుంటూరు",
        "కాకినాడ, కాకినాడ",
        "అమలాపురం, కోనసీమ",
        "వరంగల్, తెలంగాణ",
        "నెల్లూరు, నెల్లూరు"
    ]
    
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
                    Image(systemName: "location.fill")
                        .foregroundColor(Color.naaGold)
                    Text("స్థానాన్ని ఎంచుకోండి (Select Location)")
                        .font(.system(size: 17, weight: .bold))
                        .foregroundColor(Color.textPrimary(dark: vm.darkMode))
                    Spacer()
                    Button(action: onDismiss) {
                        Image(systemName: "xmark.circle.fill")
                            .foregroundColor(Color.textSecondary(dark: vm.darkMode))
                    }
                }
                
                // Live GPS Detection Button
                Button(action: {
                    vm.locationManager.setManualLocation("పొన్నమండ, కోనసీమ")
                    onDismiss()
                }) {
                    HStack {
                        Image(systemName: "location.north.circle.fill")
                            .foregroundColor(Color.naaEmerald)
                        Text("ప్రస్తుత లైవ్ స్థానం (Use GPS Location)")
                            .font(.system(size: 14, weight: .semibold))
                            .foregroundColor(Color.textPrimary(dark: vm.darkMode))
                        Spacer()
                        Text("GPS")
                            .font(.system(size: 11, weight: .bold))
                            .foregroundColor(.white)
                            .padding(.horizontal, 6)
                            .padding(.vertical, 2)
                            .background(Color.naaEmerald)
                            .cornerRadius(6)
                    }
                    .padding(12)
                    .background(Color.naaEmerald.opacity(0.12))
                    .cornerRadius(10)
                }
                
                Divider()
                
                ScrollView {
                    VStack(spacing: 8) {
                        ForEach(locations, id: \.self) { loc in
                            let isSelected = vm.locationManager.userLocation == loc
                            Button(action: {
                                vm.locationManager.setManualLocation(loc)
                                onDismiss()
                            }) {
                                HStack {
                                    Text(loc)
                                        .font(.system(size: 14, weight: isSelected ? .bold : .regular))
                                        .foregroundColor(isSelected ? Color.naaGold : Color.textPrimary(dark: vm.darkMode))
                                    Spacer()
                                    if isSelected {
                                        Image(systemName: "checkmark")
                                            .foregroundColor(Color.naaGold)
                                    }
                                }
                                .padding(.horizontal, 12)
                                .padding(.vertical, 10)
                                .background(isSelected ? Color.naaGold.opacity(0.1) : Color.clear)
                                .cornerRadius(8)
                            }
                        }
                    }
                }
                .frame(maxHeight: 280)
            }
            .padding(20)
            .background(Color.surface(dark: vm.darkMode))
            .cornerRadius(20)
            .shadow(color: Color.black.opacity(0.3), radius: 20, x: 0, y: 10)
            .padding(.horizontal, 28)
        }
    }
}
