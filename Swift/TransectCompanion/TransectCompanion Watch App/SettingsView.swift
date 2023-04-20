//
//  SettingsView.swift
//  TransectCompanion Watch App
//
//  Created by Tyler Meester on 4/19/23.
//

import SwiftUI


// Custom view modifier for settings menu
struct SettingsMenuModifier: ViewModifier {
    func body(content: Content) -> some View {
        content
            .padding()
            .background(Color.gray.opacity(0.2))
            .cornerRadius(10)
            .shadow(color: Color.black.opacity(0.2), radius: 1, x: 0, y: 1)

    }
}

// Reusable function to apply the modifier easily
extension View {
    func settingsMenuStyle() -> some View {
        self.modifier(SettingsMenuModifier())
    }
}

struct SettingsView: View {
    @EnvironmentObject var settings: Settings
    @AppStorage("vibrationEnabled") var vibrationEnabled: Bool = true
    @AppStorage("backgroundLocationEnabled") var backgroundLocationEnabled: Bool = true

    func settingsMenuStyle() -> some View {
        self.modifier(SettingsMenuModifier())
    }
    var body: some View {
        ScrollView {
            VStack(spacing: 10) {
                // Vibration
                VStack {
                    Toggle(isOn: $vibrationEnabled) {
                        Text("Vibration")
                            .font(.system(size: 18))
                    }
                }
                .settingsMenuStyle()

                // Text Size
                VStack {
                    HStack {
                        Text("Text Size")
                            .font(.system(size: 18))
                        Spacer()
                    }

                    Slider(value: $settings.textSize, in: 16...26, step: 1)
                        .padding(.horizontal)
                }
                .settingsMenuStyle()

                // Distance Threshold
                VStack {
                    HStack {
                        Text("Distance Threshold")
                            .font(.system(size: 18))
                        Spacer()
                    }

                    Picker(selection: $settings.distanceThreshold, label: Text("")) {
                        ForEach(1...20, id: \.self) { i in
                            Text("\(i)").tag(Double(i))
                        }
                    }
                    .pickerStyle(WheelPickerStyle())
                    .frame(width: 75, height: 75, alignment: .center)
                }
                .settingsMenuStyle()
                
                // Allow location data in background (off saves battery life, but doesn't notify user when they aren't looking at watch)
                
                // Dark/Light mode?

                Spacer()
            }
            .padding()
        }
    }
}
struct SettingsView_Previews: PreviewProvider {
    static var previews: some View {
        SettingsView()
    }
}
