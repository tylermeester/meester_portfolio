//
//  LocationManager.swift
//  TransectCompanion Watch App
//
//  Created by Tyler Meester on 4/17/23.
//

import Foundation
import CoreLocation
import Combine
import UTMConversion

class LocationManager: NSObject, ObservableObject, CLLocationManagerDelegate {
    @Published var heading: CLHeading?

    @Published var utmCoordinates: String = "Loading..."
    @Published var zone: Int?
    
    @Published var easting: Double?
    @Published var targetEasting: Double?
    
    @Published var northing: Double?
    @Published var targetNorthing: Double?
    

    private let locationManager = CLLocationManager()

    enum TargetType {
        case none, easting, northing
    }

    @Published var targetType: TargetType = .none
    @Published var targetValue: Double = 0


    override init() {
        super.init()
        locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.requestWhenInUseAuthorization()
        locationManager.distanceFilter = 0.5
        locationManager.startUpdatingLocation()
        locationManager.startUpdatingHeading()
    }

    // Updates the location
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        if let location = locations.last {
            let utmCoordinate = location.utmCoordinate()
            let hemisphere = utmCoordinate.hemisphere == .northern ? "N" : "S"
            easting = utmCoordinate.easting
            northing = utmCoordinate.northing
            utmCoordinates = "Zone: \(utmCoordinate.zone)\(hemisphere) \nEasting: \(easting ?? 0) \nNorthing: \(northing ?? 0)"
        }
    }
    
    // Updates the heading
    func locationManager(_ manager: CLLocationManager, didUpdateHeading newHeading: CLHeading) {
        heading = newHeading
    }
    
    
    //Assigns the current UTM value to the target, tapping again deselects
    func setTargetEasting(easting: Double) {
        if (targetEasting == nil){
            targetEasting = easting
            targetNorthing = nil
        }
        else {
            targetEasting = nil
        }
    }
    
    //Assigns the current UTM value to the target northing, tapping again deselects
    func setTargetNorthing(northing: Double) {
        if (targetNorthing == nil){
            targetNorthing = northing
            targetEasting = nil
        }
        else {
            targetNorthing = nil
        }
    }
    

    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print("Error fetching location: \(error.localizedDescription)")
    }
}

