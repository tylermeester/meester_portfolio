import SwiftUI
import UTMConversion
import AudioToolbox
import GoogleMaps

struct ContentView: View {
    @StateObject private var locationManager = LocationManager()
    @State private var showMap = true
    @State private var showSettingsView = false
    @State private var rotation: Double = 0
    


    
    var body: some View {
        GeometryReader { geometry in
            ZStack {
                
                if locationManager.initialLocation != nil {
                    VStack {
                        MapView(locationManager: locationManager, rotation: $rotation)
                            .frame(height: geometry.size.height * (2/3))
                        
                        VStack {
                            
                            if let currentLocationCoordinate2D = locationManager.currentLocationCoordinate2D  {
                                
                                
                                HStack {
                                    VStack(alignment: .leading) {
                                        
                                        
                                        Text("Easting:")
                                            .foregroundColor(.white)
                                            .font(.system(size: 16, weight: .bold))
                                        Text("Northing:")
                                            .foregroundColor(.white)
                                            .font(.system(size: 16, weight: .bold))
                                    }
                                    
                                    VStack(alignment: .leading) {
                                        Text(String(format: "%.0f", locationManager.easting ?? 0))
                                            .foregroundColor(.white)
                                            .font(.system(size: 16, weight: .medium))
                                            .onTapGesture {
                                                locationManager.setTargetEasting(location: currentLocationCoordinate2D)
                                            }
                                        Text(String(format: "%.0f", locationManager.northing ?? 0))
                                            .foregroundColor(.white)
                                            .font(.system(size: 16, weight: .medium))
                                            .onTapGesture {
                                                locationManager.setTargetNorthing(location: currentLocationCoordinate2D)
                                            }
                                    }
                                }
                                .padding()
                                
                                if let targetEasting = locationManager.targetEasting {
                                    Text("Target Easting: \(String(format: "%.0f", targetEasting))")
                                        .foregroundColor(.green)
                                        .font(.system(size: 16, design: .monospaced))
                                        .multilineTextAlignment(.center)
                                    
                                } else if let targetNorthing = locationManager.targetNorthing {
                                    Text("Target Northing: \(String(format: "%.0f", targetNorthing))")
                                        .foregroundColor(.green)
                                        .font(.system(size: 16, design: .monospaced))
                                        .multilineTextAlignment(.center)
                                }
                                Spacer()
                                
                            } else {
                                Text("Loading...")
                                    .font(.system(size: 16, design: .monospaced))
                            }
                            
                            
                            
                            HStack {
                                Button(action: {
                                    print("Button 1 tapped")
                                }) {
                                    VStack {
                                        Image(systemName: "1.circle")
                                        Text("Button 1")
                                    }
                                }
                                .padding()
                                .background(Color.white)
                                .cornerRadius(10)
                                .font(.system(size: 11))
                                
                                
                                Button(action: {
                                    // Button 1 action
                                    print("Button 2 tapped")
                                    locationManager.drawTargetLine()

                       
                                    }
                                ) {
                                    VStack {
                                        Image(systemName: "2.circle")
                                        Text("Draw Target Line")
                                    }
                                }
                                .padding()
                                .background(Color.white)
                                .cornerRadius(10)
                                .font(.system(size: 11))
                                
                                
                                
                                Button(action: {
                                    rotateMap()
                                }) {
                                    Text("Rotate \n Map")
                                }
                                .padding()
                                .background(Color.white)
                                .cornerRadius(10)
                                .font(.system(size: 11))
                                
                                
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
                                .padding()
                                .background(Color.white)
                                .cornerRadius(10)
                                .font(.system(size: 11))
                                
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
        }
        .edgesIgnoringSafeArea(.all)
    }
    
    func rotateMap() {
        rotation += 90
        if rotation == 360 {
            rotation = 0
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


