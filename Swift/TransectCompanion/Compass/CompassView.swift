//
//  CompassView.swift
//  TransectCompanion Watch App
//
//  Created by Tyler Meester on 4/19/23.
//

import SwiftUI
import AudioToolbox


// A view that displays a compass with cardinal directions (N, S, E, W) and heading information.
struct CompassView: View {
    // The LocationManager instance responsible for providing heading updates.
    @ObservedObject var locationManager: LocationManager
    
    // A user setting that determines whether vibrations are enabled.
    @AppStorage("vibrationEnabled") var vibrationEnabled: Bool = true
    
    // A state variable to track whether the view is currently visible on screen.
    @State var isViewVisible = false
    
    // This function checks if the user's heading aligns with a cardinal direction.
    // If it does and vibrations are enabled, it triggers a vibration.
    private func checkCardinalAlignment() {
        if isViewVisible, let heading = locationManager.heading?.trueHeading {
            if abs(heading - 0) < 1 || abs(heading - 90) < 1 || abs(heading - 180) < 1 || abs(heading - 270) < 1 {
                if vibrationEnabled {
                    AudioServicesPlayAlertSound(SystemSoundID(kSystemSoundID_Vibrate))
                }
            }
        }
    }

    // The main view that displays the compass and its elements.
    var body: some View {
        ZStack {
            // If heading information is available, display the compass.
            if let heading = locationManager.heading {
                CompassRoseView()
                    .rotationEffect(Angle(degrees: -heading.trueHeading))
                    // Whenever the heading is updated, check for cardinal alignment.
                    .onReceive(locationManager.$heading) { _ in
                        checkCardinalAlignment()
                    }
                
                // Display the north indicator.This is the user's current location arrow icon
                Image(systemName: "location.north.line")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 70, height: 70)
                    .foregroundColor(.green)
                    .offset(y: -10)

            // If no heading information is available, display a loading message.
            } else {
                Text("Loading compass...")
            }

        }
        // When the view appears, set isViewVisible to true.
        .onAppear {
            isViewVisible = true
        }
        // When the view disappears, set isViewVisible to false.
        .onDisappear {
            isViewVisible = false
        }
    }
}

struct CompassRoseView: View {
    var body: some View {
        ZStack {
            // Draw two concentric circles for the compass rose.
            Circle()
                .stroke(Color.gray, lineWidth: 1)
                .aspectRatio(contentMode: .fit)
                .frame(width: 190, height: 190)

            Circle()
                .stroke(Color.gray, lineWidth: 1)
                .aspectRatio(contentMode: .fit)
                .frame(width: 169, height: 169)

            // Draw tick marks for each degree of the compass rose.
            ForEach(0..<360) { index in
                TickView(angle: Double(index), long: index % 10 == 0)
            }

            // Display cardinal direction labels.
            // North
            VStack {
                Text("N")
                    .font(.system(size: 20, weight: .bold))
                    .offset(y: 20)
                Spacer()
            }

            // South
            VStack {
                Spacer()
                Text("S")
                    .font(.system(size: 20, weight: .bold))
                    .rotationEffect(.degrees(-180))
                    .offset(y: -20)
            }

            // East
            HStack {
                Spacer()
                Text("E")
                    .font(.system(size: 20, weight: .bold))
                    .rotationEffect(.degrees(90))
                    .offset(x: -25)
            }

            // West
            HStack {
                Text("W")
                    .font(.system(size: 20, weight: .bold))
                    .rotationEffect(.degrees(-90))
                    .offset(x: 20)
                Spacer()
            }
        }
        .frame(width: 200, height: 200)
    }
}

// A view representing a single tick mark on the compass rose.
struct TickView: View {
    let angle: Double  // The angle (in degrees) of the tick mark on the compass.
    let long: Bool  // A boolean indicating whether the tick mark is long or short.

    var body: some View {
        VStack {
            // Draw a rectangle as the tick mark.
            Rectangle()
                .frame(width: 2, height: (long || isCardinalDirection()) ? 20 : 8)
                .foregroundColor(isCardinalDirection() ? .white : (long ? .black : .gray))
            Spacer()
        }
        .rotationEffect(Angle(degrees: angle))  // Rotate the tick mark based on its angle.
    }

    // A helper function to determine if the tick mark represents a cardinal direction (N, S, E, W).
    private func isCardinalDirection() -> Bool {
        return angle == 0 || angle == 90 || angle == 180 || angle == 270
    }
}

struct CompassView_Previews: PreviewProvider {
    static var previews: some View {
        CompassView(locationManager: LocationManager())
    }
}
