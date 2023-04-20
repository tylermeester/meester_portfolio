//
//  MainView.swift
//  TransectCompanion Watch App
//
//  Created by Tyler Meester on 4/19/23.
//

import SwiftUI

struct MainView: View {
    @StateObject private var locationManager = LocationManager()
    @StateObject private var settings = Settings()
    @State private var selection = 0

    var body: some View {
        TabView(selection: $selection) {
            LocationView()
                .tag(0)
            CompassView()
                .tag(1)
            SettingsView()
                .tag(2)
        }
        .tabViewStyle(PageTabViewStyle(indexDisplayMode: .never))
        .environmentObject(locationManager)
        .environmentObject(settings)
    }
}

struct MainView_Previews: PreviewProvider {
    static var previews: some View {
        MainView()
    }
}
