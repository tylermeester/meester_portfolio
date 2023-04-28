//
//  LocationManager.swift
//  TransectCompanion
//
//  Created by Tyler Meester on 4/24/23.
//

import Foundation
import CoreLocation
import UTMConversion
import AudioToolbox
import UserNotifications
import UIKit


class LocationManager: NSObject, ObservableObject, CLLocationManagerDelegate {
    
    /**
     -------------------------------------------------
     ---------- VARIABLE DECLARATIONS AND INITIALIZATION -----------
     -------------------------------------------------
     */
    // ---------- LOCATION  ----------
    @Published var heading: CLHeading?
    @Published var utmCoordinates: String = "Loading..."
    @Published var zone: Int?
    @Published var easting: Double?
    @Published var targetEasting: Double?
    @Published var northing: Double?
    @Published var targetNorthing: Double?
    @Published var latitude: CLLocationDegrees?
    @Published var longitude: CLLocationDegrees?
    @Published var initialLocation: CLLocation?
    
    
    // ---------- VIBRATION NOTIFICATION ----------
    @Published var targetRange: Double = 5 // range from target easting or northing to notify user
    @Published var isOutOfRange = false
    @Published var vibrationEnabled = true // used for program logic to determine whether watch should vibrate
    @Published var backgroundVibrationEnabled = true
    
    
    // ---------- INITIALIZING LOCATION MANAGER ----------
    private let locationManager = CLLocationManager()

    override init() {
        super.init()
        locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.requestAlwaysAuthorization()
        locationManager.distanceFilter = 0.5
        locationManager.startUpdatingLocation()
        locationManager.startUpdatingHeading()
        
        NotificationCenter.default.addObserver(forName: UIApplication.willResignActiveNotification, object: nil, queue: nil) { [weak self] _ in
            self?.locationManager.allowsBackgroundLocationUpdates = true
            self?.locationManager.pausesLocationUpdatesAutomatically = false
            self?.locationManager.startMonitoringSignificantLocationChanges()
        }
    }

    /**
     -------------------------------------------------
     ------------------------ DELEGATE METHODS ----------------------------------
     -------------------------------------------------
     */
    // ON LOCATION UPDATE
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        // Check if there is at least one CLLocation object in the locations array,
        // and if so, retrieve the last one (the most recent location update).
        if let location = locations.last {
            
            // Update latitude and longitude properties
            latitude = location.coordinate.latitude
            longitude = location.coordinate.longitude
            
            // Convert the CLLocation object to UTM coordinates.
            let utmCoordinate = location.utmCoordinate()
            
            // Determine the hemisphere (N for Northern or S for Southern) based on the UTM coordinate.
            let hemisphere = utmCoordinate.hemisphere == .northern ? "N" : "S"
            
            // Store the easting and northing values from the UTM coordinate.
            easting = utmCoordinate.easting
            northing = utmCoordinate.northing
            
            // Format and store the UTM coordinates as a string.
            utmCoordinates = "Zone: \(utmCoordinate.zone)\(hemisphere) \nEasting: \(easting ?? 0) \nNorthing: \(northing ?? 0)"
            
            // Check if the device is within the target range by calling the checkDistance function.
            checkDistance(targetRange: targetRange)
            
            // If the device is out of range and vibration is enabled
            if isOutOfRange && vibrationEnabled {
                let appState = UIApplication.shared.applicationState
                
                // If the app is in the background and background vibration is enabled
                if appState == .background && backgroundVibrationEnabled {
                    AudioServicesPlayAlertSound(SystemSoundID(kSystemSoundID_Vibrate))
                }
                // If the app is not in the background (i.e., active or inactive state)
                else if appState != .background {
                    AudioServicesPlayAlertSound(SystemSoundID(kSystemSoundID_Vibrate))
                }
            }
            
            // Update initialLocation only once, when it's nil, this loads the map with user's current location
            if initialLocation == nil {
                initialLocation = locations.last
            }
        }
    }

    
    // ON HEADING UPDATE
    func locationManager(_ manager: CLLocationManager, didUpdateHeading newHeading: CLHeading) {
        heading = newHeading
    }
    
    /**
     -------------------------------------------------
     -------------------- CUSTOM INSTANCE METHODS -------------------------
     -------------------------------------------------
     */
    // This function assigns the current easting value as the target easting.
    // If the target easting is already set, tapping again deselects it.
    func setTargetEasting(easting: Double) {
        // Check if the target easting is not set.
        if (targetEasting == nil) {
            // Set the target easting and reset the target northing.
            targetEasting = easting
            targetNorthing = nil
        } else {
            // If the target easting is already set, deselect it.
            targetEasting = nil
        }
    }

    // This function assigns the current northing value as the target northing.
    // If the target northing is already set, tapping again deselects it.
    func setTargetNorthing(northing: Double) {
        // Check if the target northing is not set.
        if (targetNorthing == nil) {
            // Set the target northing and reset the target easting.
            targetNorthing = northing
            targetEasting = nil
        } else {
            // If the target northing is already set, deselect it.
            targetNorthing = nil
        }
    }
    
    
    // Checks if the user is within the range of the target UTM, and returns a boolean value
    func checkDistance(targetRange: Double) {
        // Check if both easting and northing values are available.
        // If not, set `isOutOfRange` to false and return immediately.
        guard let currentEasting = easting, let currentNorthing = northing else {
            isOutOfRange = false
            return
        }

        // Compare the current easting value with the target easting value.
        // If the difference is greater than the allowed target range, set `isOutOfRange` to true.
        if let targetEasting = targetEasting,
           abs(targetEasting - currentEasting) > targetRange {
            isOutOfRange = true
        }
        // Compare the current northing value with the target northing value.
        // If the difference is greater than the allowed target range, set `isOutOfRange` to true.
        else if let targetNorthing = targetNorthing,
                abs(targetNorthing - currentNorthing) > targetRange {
            isOutOfRange = true
        }
        // If both comparisons are within the target range, set `isOutOfRange` to false.
        else {
            isOutOfRange = false
        }
    }

    
    

    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print("Error fetching location: \(error.localizedDescription)")
    }
}
