//
//  CompassView.swift
//  TransectCompanion Watch App
//
//  Created by Tyler Meester on 4/19/23.
//

import SwiftUI

struct CompassView: View {
    @EnvironmentObject var locationManager: LocationManager
    @AppStorage("vibrationEnabled") var vibrationEnabled: Bool = true
    @State var isViewVisible = false
    
    
    private func checkCardinalAlignment() {
        if isViewVisible, let heading = locationManager.heading?.trueHeading {
            if abs(heading - 0) < 1 || abs(heading - 90) < 1 || abs(heading - 180) < 1 || abs(heading - 270) < 1 {
                if vibrationEnabled{
                    WKInterfaceDevice.current().play(.success)
                }
            }
        }
    }

    var body: some View {
        ZStack {
            if let heading = locationManager.heading {
                CompassRoseView()
                    .rotationEffect(Angle(degrees: -heading.trueHeading))
                    .onReceive(locationManager.$heading) { _ in
                        checkCardinalAlignment()
                    }
            } else {
                Text("Loading compass...")
            }

                Image(systemName: "location.north.line")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 70, height: 70)
                    .foregroundColor(.green)
                    .offset(y: -10)
        }
        .onAppear {
            isViewVisible = true
        }
        .onDisappear {
            isViewVisible = false
        }
    }
}

struct CompassRoseView: View {
    var body: some View {
        ZStack {
            Circle()
                .stroke(Color.gray, lineWidth: 1)
                .aspectRatio(contentMode: .fit)
                .frame(width: 190, height: 190)

            Circle()
                .stroke(Color.gray, lineWidth: 1)
                .aspectRatio(contentMode: .fit)
                .frame(width: 169, height: 169)
            
            ForEach(0..<360) { index in
                TickView(angle: Double(index), long: index % 10 == 0)
            }

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

struct TickView: View {
    let angle: Double
    let long: Bool
    
    var body: some View {
        VStack {
            Rectangle()
                .frame(width: 2, height: (long || isCardinalDirection()) ? 20 : 8)
                .foregroundColor(isCardinalDirection() ? .white : (long ? .black : .gray))
            Spacer()
        }
        .rotationEffect(Angle(degrees: angle))
    }
    
    private func isCardinalDirection() -> Bool {
        return angle == 0 || angle == 90 || angle == 180 || angle == 270
    }
}




struct CompassView_Previews: PreviewProvider {
    static var previews: some View {
        CompassView().environmentObject(LocationManager())
    }
}
