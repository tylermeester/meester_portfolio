//
//  TransectCompanionApp.swift
//  TransectCompanion
//
//  Created by Tyler Meester on 4/26/23.
//

import SwiftUI
import GoogleMaps

@main
struct TransectCompanionApp: App {
    init(){
        GMSServices.provideAPIKey("AIzaSyC0nRFfgMlgQtZspgV6IRFuNmCSts0C9NA")
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
