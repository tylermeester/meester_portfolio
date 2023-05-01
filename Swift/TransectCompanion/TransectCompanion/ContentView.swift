import SwiftUI
import UTMConversion
import AudioToolbox
import GoogleMaps

struct CustomButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .frame(width: 110, height: 50)
            .background(Color(.darkGray))
            .cornerRadius(10)
            .foregroundColor(.white)
            .shadow(color: Color.black.opacity(0.2), radius: 5, x: 0, y: 2)
            .scaleEffect(configuration.isPressed ? 0.95 : 1)
            .animation(.easeInOut(duration: 0.1), value: configuration.isPressed)
    }
}

struct ContentView: View {
    @StateObject private var locationManager = LocationManager()
    @State private var showMap = true
    @State private var showSettingsView = false
    @State private var showCompassView = false
    @State private var rotation: Double = 0
    @State private var compassHeading: CLLocationDirection? = nil

    
    
    
    var body: some View {
        GeometryReader { geometry in
            ZStack {
                
                if locationManager.initialLocation != nil {
                    VStack {
                        /**-------------------------------------------
                         -------------------------------------- MAP  ----------------------------------
                         ---------------------------------------------
                         */
                        MapView(locationManager: locationManager, rotation: $rotation)
                            .frame(height: geometry.size.height * (2/3))
                        
                        
                        
                        /**-------------------------------------------
                         ------------------------------ DASHBOARD  -------------------------------
                         ---------------------------------------------
                         */
                        VStack {
                            
                            if let currentLocationCoordinate2D = locationManager.currentLocationCoordinate2D  {
                        
                                HStack {
                                    VStack(alignment: .center) {
                                        
                                        
                                        Text("Easting:")
                                            .foregroundColor(.white)
                                            .font(.system(size: 18, weight: .bold))
                                        Text("Northing:")
                                            .foregroundColor(.white)
                                            .font(.system(size: 18, weight: .bold))
                                    }
                                    
                                    VStack(alignment: .leading) {
                                        Text(String(format: "%.0f", locationManager.easting ?? 0))
                                            .foregroundColor(.white)
                                            .font(.system(size: 18, weight: .medium))
                                            .onTapGesture {
                                                locationManager.setTargetEasting(location: currentLocationCoordinate2D)
                                            }
                                        Text(String(format: "%.0f", locationManager.northing ?? 0))
                                            .foregroundColor(.white)
                                            .font(.system(size: 18, weight: .medium))

                                            .onTapGesture {
                                                locationManager.setTargetNorthing(location: currentLocationCoordinate2D)
                                            }
                                    }
                                }
                                .padding()
                                
                                if let targetEasting = locationManager.targetEasting {
                                    Text("Target Easting: \(String(format: "%.0f", targetEasting))")
                                        .foregroundColor(.green)
                                        .font(.system(size: 18, weight: .medium))
                                        .multilineTextAlignment(.center)
                                    
                                } else if let targetNorthing = locationManager.targetNorthing {
                                    Text("Target Northing: \(String(format: "%.0f", targetNorthing))")
                                        .foregroundColor(.green)
                                        .font(.system(size: 18, weight: .medium))
                                        .multilineTextAlignment(.center)
                                }
                                Spacer()
                                
                            } else {
                                Text("Loading...")
                                    .font(.system(size: 16, design: .monospaced))
                            }
                            
                            
                            
                            
                            /**---------------------------------------------
                             ----------------------------------BOTTOM BAR ----------------------------------
                             -----------------------------------------------
                             */
                            HStack {
                                /**-------------------------------------------
                                 -------------------DRAW TARGET LINE BUTTON ---------------------
                                 ---------------------------------------------
                                 */
                                Button(action: {
                                    locationManager.toggleTargetLineDisplay()
                                }
                                ) {
                                    VStack {
                                        Text("Target \n Line")
                                    }
                                }
                                .buttonStyle(CustomButtonStyle())

                                
                                
                                /**-------------------------------------------
                                 ----------------------- COMPASS BUTTON -------------------------------
                                 ---------------------------------------------
                                 */
                                Button(action: {
                                    withAnimation {
                                        showCompassView.toggle()
                                    }
                                }) {
                                    Text("Compass")
                                }
                                .buttonStyle(CustomButtonStyle())

                                
                                
                                /**-------------------------------------------
                                 -------------------------- SETTINGS BUTTON -----------------------------
                                 ---------------------------------------------
                                 */
                                Button(action: {
                                    // Button 3 action
                                    showSettingsView.toggle()
                                }) {
                                    VStack {
                                        Image(systemName: "gear")
                                        Text("Settings")
                                    }
                                }
                                .sheet(isPresented: $showSettingsView) {
                                    SettingsView(locationManager: locationManager)
                                }
                                .buttonStyle(CustomButtonStyle())

                                
                            }
                            .padding(.bottom)
                        }
                        .frame(height: geometry.size.height * (1/3))
                        .background(Color.black)
                    }
                } else {
                    LoadingView()
                }
                
                
            }
  
            .edgesIgnoringSafeArea(.all)
            
            /**-------------------------------------------
             ----------------------------- COMPASS VIEW -----------------------------
             ---------------------------------------------
             */
            .overlay(
                    Group {
                        if showCompassView {
                            VStack {
                                Spacer()
                                CompassView(locationManager: locationManager)
                                    .padding(.bottom, 50)
                                    .frame(width: geometry.size.width, height: geometry.size.height * (2/5))
                                    .background(Color.black)
                                    .cornerRadius(15)
                                    .onTapGesture { // Add onTapGesture to the compass view
                                        withAnimation {
                                            showCompassView = false
                                        }
                                    }
                            }
                            .transition(.opacity)
//                            .transition(.move(edge: .bottom))
                        }
                    }
                )
            .onTapGesture { // Add onTapGesture to the main ZStack
                withAnimation {
                    showCompassView = false
                }
            }
            
        }
        
    }
    
    struct LoadingView: View {
        var body: some View {
            VStack {
                Text("Loading map...")
                    .font(.system(size: 20))
                Spacer()
            }
        }
    }
    
    
    
    struct ContentView_Previews: PreviewProvider {
        static var previews: some View {
            ContentView()
        }
    }
}

