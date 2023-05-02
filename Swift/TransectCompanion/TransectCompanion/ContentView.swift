import SwiftUI
import UTMConversion
import AudioToolbox
import GoogleMaps

struct CustomButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .frame(width: 100, height: 55)
            .background(Color(.systemGray5))
            .cornerRadius(10)
            .foregroundColor(.white)
            .shadow(color: Color.black.opacity(0.2), radius: 5, x: 0, y: 2)
            .scaleEffect(configuration.isPressed ? 0.95 : 1)
            .animation(.easeInOut(duration: 0.1), value: configuration.isPressed)
    }
}

struct CustomWiderButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .frame(minWidth: 200, minHeight: 30) // Adjust minWidth to make the buttons wider
            .background(Color(.systemGray5))
            .cornerRadius(10)
            .foregroundColor(.white)
            .shadow(color: Color.black.opacity(0.2), radius: 5, x: 0, y: 2)
            .scaleEffect(configuration.isPressed ? 0.95 : 1)
            .animation(.easeInOut(duration: 0.1), value: configuration.isPressed)
    }
}


struct LocationInfoView: View {
    let title: String
    let value: String
    let onTap: () -> Void

    var body: some View {
        Button(action: onTap) {
            HStack {
                Text(title)
                    .font(.system(size: 18, weight: .bold))
                Text(value)
                    .font(.system(size: 18, weight: .medium))
            }
            .foregroundColor(.white)
        }
        .buttonStyle(CustomWiderButtonStyle())
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
                         ---------------------------- FIRST LEVEL - MAP  --------------------------
                         ---------------------------------------------
                         */
                        VStack {
                            MapView(locationManager: locationManager, rotation: $rotation)
                                .frame(height: geometry.size.height * (20/30))
                            
                            
                            
                            
                        }
                        
                        /**-------------------------------------------
                         ------------------ SECOND LEVEL - DASHBOARD  -------------------
                         ---------------------------------------------
                         */
                        VStack {
                            
                            if let currentLocationCoordinate2D = locationManager.currentLocationCoordinate2D {
                                VStack {
                                    LocationInfoView(title: "Easting:", value: String(format: "%.0f", locationManager.easting ?? 0)) {
                                        locationManager.setTargetEasting(location: currentLocationCoordinate2D)
                                    }

                                    LocationInfoView(title: "Northing:", value: String(format: "%.0f", locationManager.northing ?? 0)) {
                                        locationManager.setTargetNorthing(location: currentLocationCoordinate2D)
                                    }
                                }
                                .padding(EdgeInsets(top: 10, leading: 15, bottom: 10, trailing: 15))
                                .background(Color(.systemGray6))
                                .cornerRadius(10)
                                .shadow(color: Color.black.opacity(0.2), radius: 5, x: 0, y: 2)
                                .padding()
                                
        
                                if let targetEasting = locationManager.targetEasting {
                                    VStack {
                                        Text("Target Easting: \(String(format: "%.0f", targetEasting))")
                                            .foregroundColor(.green)
                                            .font(.system(size: 18, weight: .bold))
                                            .multilineTextAlignment(.center)
                                    }
                                    .padding(EdgeInsets(top: 5, leading: 10, bottom: 5, trailing: 10))
                                    .background(Color(.systemGray6))
                                    .cornerRadius(10)
                                    .shadow(color: Color.black.opacity(0.2), radius: 5, x: 0, y: 2)

                                    
                                } else if let targetNorthing = locationManager.targetNorthing {
                                    VStack {
                                        Text("Target Northing: \(String(format: "%.0f", targetNorthing))")
                                            .foregroundColor(.green)
                                            .font(.system(size: 18, weight: .bold))
                                            .multilineTextAlignment(.center)
                                    }
                                    .padding(EdgeInsets(top: 5, leading: 10, bottom: 5, trailing: 10))

                                    .background(Color(.systemGray6))
                                    .cornerRadius(10)
                                    .shadow(color: Color.black.opacity(0.2), radius: 5, x: 0, y: 2)

                                }
                                Spacer()
                                
                            } else {
                                Text("Loading...")
                                    .font(.system(size: 16, design: .monospaced))
                            }
                        }
                        /**---------------------------------------------
                         ----------------- DASHBOARD VSTACK PROPERTIES------------------
                         -----------------------------------------------
                         */
                        .frame(height: geometry.size.height * (7/30))
                        .background(Color.black)
                        
                        
                    
                        
                        
                        /**---------------------------------------------
                         ----------------------- THIRD LEVEL - BOTTOM BAR-----------------------
                         -----------------------------------------------
                         */VStack {
                             // DIVIDER ON TOP EDGE OF BOTTOM BAR

                             
                                 HStack {
                                     /**---------------------------------------------
                                      ----------------------- DRAW TARGET BUTTON ----------------------------
                                      -----------------------------------------------
                                      */
                                     Button(action: {
                                         locationManager.toggleTargetLineDisplay()
                                     }) {
                                         VStack {
                                             Image(systemName: "rotate.3d")
                                             Text("Target")
                                         }
                                     }
                                     .buttonStyle(CustomButtonStyle())
                                     .padding(.top, 6) // Add horizontal padding to the HStack
                                     .padding(.leading, 20) // Add horizontal padding to the HStack
                                     
                                     
                                     // Divider line
                                     Divider()
                                         .frame(height: 40)
                                     
                                     /**---------------------------------------------
                                      ----------------------- SHOW COMPASS BUTTON -------------------------
                                      -----------------------------------------------
                                      */
                                     Button(action: {
                                         withAnimation {
                                             showCompassView.toggle()
                                         }
                                     }) {
                                         VStack{
                                             Image(systemName: "location.north.circle")
                                             Text("Compass")
                                         }
                                         
                                     }
                                     .buttonStyle(CustomButtonStyle())
                                     .padding(.top, 6) // Add horizontal padding to the HStack
                                     
                                     // Divider line
                                     Divider()
                                         .frame(height: 40)
                                     
                                     /**---------------------------------------------
                                      ----------------------------- SETTINGS BUTTON -----------------------------
                                      -----------------------------------------------
                                      */
                                     Button(action: {
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
                                     .padding(.top, 6) // Add horizontal padding to the HStack
                                     .padding(.trailing, 20) // Add horizontal padding to the HStack
                                 }
                                 /**---------------------------------------------
                                  ----------------- BOTTOM BAR HSTACK PROPERTIES------------------
                                  -----------------------------------------------
                                  */
                                     
                                 .background(Color(.systemGray6))

                         }
                        /**---------------------------------------------
                         ----------------- BOTTOM BAR VSTACK PROPERTIES------------------
                         -----------------------------------------------
                         */
                         .frame(height: geometry.size.height * (3/30))
                         .background(Color(.systemGray6))

                        
                    }
                    
                }
            

                else {
                    LoadingView()
                }
            }
            /**---------------------------------------------
             -------------------- GLOBAL VSTACK PROPERTIES------------------
             -----------------------------------------------
             */
            // Makes background black, and forces app to use dark mode
            .background(Color.black.edgesIgnoringSafeArea(.all))
            .preferredColorScheme(.dark)
            
            
            
            
            
            
            
            
            
            
            /**-------------------------------------------
             --------------------- COMPASS VIEW OVERLAY -----------------------
             ---------------------------------------------
             */
            .overlay(
                    Group {
                        if showCompassView {
                            VStack {
                                Spacer()
                                CompassView(locationManager: locationManager)
                                    .padding(.bottom, 20)
                                    .frame(width: geometry.size.width, height: geometry.size.height * (1/3))
                                    .background(Color.black)
//                                    .cornerRadius(15)
                                    .onTapGesture { // Add onTapGesture to the compass view
                                        withAnimation {
                                            showCompassView = false
                                        }
                                    }
                            }
                            .transition(.opacity)
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

