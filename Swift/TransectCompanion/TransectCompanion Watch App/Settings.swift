//
//  Settings.swift
//  TransectCompanion Watch App
//
//  Created by Tyler Meester on 4/19/23.
//

import SwiftUI
import Combine

class Settings: ObservableObject {
    @Published var textSize: CGFloat = 20
    @Published var distanceThreshold: Double = 5;
}
