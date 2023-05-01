import SwiftUI

struct SettingsView: View {
    @Environment(\.presentationMode) var presentationMode
    @ObservedObject var locationManager: LocationManager

    var body: some View {
        NavigationView {
            List {
                
                Section(header: Text("Vibration Settings")) {
                    /**-------------------------------------------
                     ------------------------- VIBRATION TOGGLE ----------------------------
                     ---------------------------------------------
                     */
                    Toggle("Vibration", isOn: $locationManager.vibrationEnabled)
                        .onChange(of: locationManager.vibrationEnabled) { newValue in
                            if !newValue {
                                locationManager.backgroundVibrationEnabled = false
                            }
                        }
                    
                    /**-------------------------------------------
                     -----------------BACKGROUND VIBRATION TOGGLE ---------------
                     ---------------------------------------------
                     */
                    Toggle("Background Vibration", isOn: $locationManager.backgroundVibrationEnabled)
                        .onChange(of: locationManager.backgroundVibrationEnabled) { newValue in
                            if newValue {
                                locationManager.vibrationEnabled = true
                            }
                        }
                }
                
                Section(header: Text("Map Settings")) {
                    /**-------------------------------------------
                     -----------------FOLLOW USER LOCATION TOGGLE ---------------
                     ---------------------------------------------
                     */
                    Toggle("Follow User Location", isOn: $locationManager.shouldFollowUser)
                }
                
                
                
                Section(header: Text("Target Settings")) {
                    /**-------------------------------------------
                     --------------------------TARGET RANGE SLIDER ----------------------
                     ---------------------------------------------
                     */
                    HStack {
                        Text("Target Range")
                        Spacer()
                        Text("\(locationManager.targetRange, specifier: "%.0f") \(locationManager.targetRange == 1 ? "Meter" : "Meters")")
                            .foregroundColor(.gray)
                    }
                    Slider(value: $locationManager.targetRange, in: 1...20, step: 1)
                        .onChange(of: locationManager.targetRange) { _ in
                            locationManager.targetLineDisplay = false
                        }
                }
            }
            .listStyle(GroupedListStyle())
            .navigationBarTitle("Settings", displayMode: .inline)
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
