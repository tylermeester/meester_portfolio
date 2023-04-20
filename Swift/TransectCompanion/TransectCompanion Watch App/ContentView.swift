//
//  ContentView.swift
//  TransectCompanion Watch App
//
//  Created by Tyler Meester on 4/17/23.
//

import SwiftUI
import WatchKit


struct ContentView: View {
    @StateObject private var locationManager = LocationManager()
    
    private func checkDistance() {
        guard let easting = locationManager.easting, let northing = locationManager.northing else {
            return
        }
        
        if let targetEasting = locationManager.targetEasting,
           abs(targetEasting - easting) > 1 {
            WKInterfaceDevice.current().play(.directionUp)
        } else if let targetNorthing = locationManager.targetNorthing,
                  abs(targetNorthing - northing) > 1 {
            WKInterfaceDevice.current().play(.directionDown)
        }
    }

    var body: some View {
        VStack {
            
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
            
            Spacer().frame(height: 16)
            
            if let easting = locationManager.easting {
                Text(String(format: "%.0f", easting))
                    .font(.system(size: 16, design: .monospaced))
                    .padding()
                    .onTapGesture {
                        locationManager.setTargetEasting(easting: easting)
                    }
            }
            
            if let northing = locationManager.northing {
                Text(String(format: "%.0f", northing))
                    .font(.system(size: 16, design: .monospaced))
                    .onTapGesture {
                        locationManager.setTargetNorthing(northing: northing)
                    }
            }
            
            
            else {
                Text("Loading...")
                    .font(.system(size: 16, design: .monospaced))
            }
        }
        
        .onReceive(locationManager.$easting.combineLatest(locationManager.$northing)) { _, _ in
            checkDistance()
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}



