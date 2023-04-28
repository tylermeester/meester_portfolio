//
//  SettingsView.swift
//  TransectCompanion
//
//  Created by Tyler Meester on 4/27/23.
//

import SwiftUI

struct SettingsView: View {
    @Environment(\.presentationMode) var presentationMode
    @ObservedObject var locationManager: LocationManager

    var body: some View {
        NavigationView {
            VStack {
                Text("Settings")
                    .font(.largeTitle)
                    .padding()


                Slider(value: $locationManager.targetRange, in: 1...50, step: 1) {
                    Text("Target Range")
                }
                Text("Target Range: \(locationManager.targetRange, specifier: "%.0f")")
         
                
                Toggle("Vibration On/Off", isOn: $locationManager.vibrationEnabled)
                    .padding(.horizontal)
                    .onChange(of: locationManager.vibrationEnabled) { newValue in
                        if !newValue {
                            locationManager.backgroundVibrationEnabled = false
                        }
                    }
                
                
                Toggle("Background Vibration On/Off", isOn: $locationManager.backgroundVibrationEnabled)
                    .padding(.horizontal)
                    .onChange(of: locationManager.backgroundVibrationEnabled) { newValue in
                        if newValue {
                            locationManager.vibrationEnabled = true
                        }
                    }

                
                Spacer()
            }
            .navigationBarItems(trailing: Button(action: {
                presentationMode.wrappedValue.dismiss()
            }) {
                Text("Done")
                    .fontWeight(.bold)
            })
        }
    }
}

struct SettingsView_Previews: PreviewProvider {
    static var previews: some View {
        SettingsView(locationManager: LocationManager())
    }
}
