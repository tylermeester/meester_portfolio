//
//  LocationView.swift
//  TransectCompanion Watch App
//
//  Created by Tyler Meester on 4/19/23.
//

import SwiftUI
import WatchKit


struct LocationView: View {
    @StateObject private var locationManager = LocationManager()
    @AppStorage("vibrationEnabled") var vibrationEnabled: Bool = true
    @EnvironmentObject var settings: Settings
    
    
    /**
     Checks the user's distance form the UTM target,
     */
    private func checkDistance() {
        guard let easting = locationManager.easting, let northing = locationManager.northing else {
            return
        }
        
        if let targetEasting = locationManager.targetEasting,
           abs(targetEasting - easting) > settings.distanceThreshold {
            if vibrationEnabled{
                WKInterfaceDevice.current().play(.notification)
            }
        } else if let targetNorthing = locationManager.targetNorthing,
                  abs(targetNorthing - northing) > settings.distanceThreshold {
            if vibrationEnabled{
                WKInterfaceDevice.current().play(.notification)
            }
        }
    }

    var body: some View {
        VStack {
            
            if let targetEasting = locationManager.targetEasting {
                Text("Target Easting:\n \(String(format: "%.0f", targetEasting))")
                    .foregroundColor(.green)
                    .font(.system(size: (16), design: .monospaced))
                    .multilineTextAlignment(.center)
            }
            
            if let targetNorthing = locationManager.targetNorthing {
                Text("Target Northing:\n \(String(format: "%.0f", targetNorthing))")
                    .foregroundColor(.green)
                    .font(.system(size: (16), design: .monospaced))
                    .multilineTextAlignment(.center)
            }
            
            Spacer().frame(height: 14)
            
            if let easting = locationManager.easting {
                Text(       String(format: "%.0f", easting))
                    .font(.system(size: settings.textSize, design: .monospaced))
                    .padding()
                    .onTapGesture {
                        locationManager.setTargetEasting(easting: easting)
                    }
            }
            
            if let northing = locationManager.northing {
                Text(String(format: "%.0f", northing))
                    .font(.system(size: settings.textSize, design: .monospaced))
                    .onTapGesture {
                        locationManager.setTargetNorthing(northing: northing)
                    }
            }
            
            
            else if (locationManager.easting == nil && locationManager.northing == nil){
                Text("Loading...")
                    .font(.system(size: 16, design: .monospaced))
            }
        }
        
        .onReceive(locationManager.$easting.combineLatest(locationManager.$northing)) { _, _ in
            checkDistance()
        }
        
    }
}

struct LocationView_Previews: PreviewProvider {
    static var previews: some View {
        LocationView()
    }
}


/**
 ===== TO DO =====
 Make Easting and Northing subtle buttons. Single click half select, use dial to change number. Second click 
 */
