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
import GoogleMaps


class LocationManager: NSObject, ObservableObject, CLLocationManagerDelegate {
    
    /**
     -------------------------------------------------
     ---------- VARIABLE DECLARATIONS AND INITIALIZATION -----------
     -------------------------------------------------
     */
    // ---------- LOCATION  ----------
    @Published var heading: CLHeading?
    @Published var utmCoordinatesString: String = "Loading..."
    @Published var zone: Int?
    @Published var easting: Double?
    @Published var targetEasting: Double?
    @Published var northing: Double?
    @Published var targetNorthing: Double?

    @Published var initialLocation: CLLocation?
    @Published var currentLocationCoordinate2D: CLLocationCoordinate2D?
    
    // ---------- TARGET LINES ----------
    @Published var targetLineStart: CLLocationCoordinate2D?
    @Published var targetLineUpperStart: CLLocationCoordinate2D?
    @Published var targetLineLowerStart: CLLocationCoordinate2D?

    @Published var targetLineEnd: CLLocationCoordinate2D?
    @Published var targetLineUpperEnd: CLLocationCoordinate2D?
    @Published var targetLineLowerEnd: CLLocationCoordinate2D?
    
    @Published var targetLinePolyline: GMSPolyline? // = nil (???)
    
    @Published var targetLineRangePolylineUpper: GMSPolyline?
    @Published var targetLineRangePolylineLower: GMSPolyline?
    @Published var targetLineRangePolygon: GMSPolygon?

    @Published var targetLineDisplay = false
    
    // ---------- MAP SETTINGS ----------
    @Published var shouldFollowUser: Bool = true 


    
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
            
            // Saves location as CLLocationCoordinate2D
            currentLocationCoordinate2D = location.coordinate
    
            
            // Convert the CLLocation object to UTM coordinates.
            let utmCoordinate = location.utmCoordinate()
            
            // Determine the hemisphere (N for Northern or S for Southern) based on the UTM coordinate.
            let hemisphere = utmCoordinate.hemisphere == .northern ? "N" : "S"
            
            // Store the easting and northing values from the UTM coordinate.
            easting = utmCoordinate.easting
            northing = utmCoordinate.northing
            
            
            // Format and store the UTM coordinates as a string.
            utmCoordinatesString = "Zone: \(utmCoordinate.zone)\(hemisphere) \nEasting: \(easting ?? 0) \nNorthing: \(northing ?? 0)"
            
            // Check if the device is within the target range by calling the checkDistance function.
            checkDistance(targetRange: targetRange)
            
            // If the device is out of range and vibration is enabled
            if isOutOfRange && vibrationEnabled {
                let appState = UIApplication.shared.applicationState
                
                // If the app is in the background and background vibration is enabled, vibrate.
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
    func setTargetEasting(location: CLLocationCoordinate2D ) {
        // Check if the target easting is not set.
        if (targetEasting == nil) {
            
            clearTargetLine()

            // Gets the UTM info of the location
            let utm = location.utmCoordinate()
            
            // Saves the updated UTM coordinate with the new northing value
            let startUTM = UTMCoordinate(northing: (utm.northing - 500), easting: utm.easting, zone: utm.zone, hemisphere: utm.hemisphere)
            let endUTM = UTMCoordinate(northing: (utm.northing + 500), easting: utm.easting, zone: utm.zone, hemisphere: utm.hemisphere)

            // Save the targetEasting and line start coordinates
            targetEasting = utm.easting
            targetLineStart = startUTM.coordinate()
            targetLineEnd = endUTM.coordinate()
            
            targetNorthing = nil
            
            drawTargetLine()

        } else {
            // If the target easting is already set, deselect it.
            clearTargetLine()
            targetEasting = nil
            targetLineStart = nil
            targetLineEnd = nil

        }
    }

    // This function assigns the current northing value as the target northing.
    // If the target northing is already set, tapping again deselects it.
    func setTargetNorthing(location: CLLocationCoordinate2D) {
        // Check if the target northing is not set.
        if (targetNorthing == nil) {
            clearTargetLine()

            // Gets the UTM info of the location
            let utm = location.utmCoordinate()
            
            // Saves the updated UTM coordinate with the new easting value
            let startUTM = UTMCoordinate(northing: utm.northing, easting: (utm.easting - 500), zone: utm.zone, hemisphere: utm.hemisphere)
            let endUTM = UTMCoordinate(northing: utm.northing, easting: (utm.easting + 500), zone: utm.zone, hemisphere: utm.hemisphere)
            
            // Save the targetNorthing and line start coordinates
            targetNorthing = utm.northing
            targetLineStart = startUTM.coordinate()
            targetLineEnd = endUTM.coordinate()
    
            // Make sure targetEasting set to nil
            targetEasting = nil
            
            drawTargetLine()

                
        } else {
            // If the target northing is already set, deselect it.
            clearTargetLine()
            targetNorthing = nil
            targetLineStart = nil
            targetLineEnd = nil

        }
    }
    
    
    // Draws the target line, the target range boundary lines, and colors the space in between
    func drawTargetLine() {
        // Ensure that both start and end locations are available
        guard let startLocation = targetLineStart, let endLocation = targetLineEnd else {
            // If either start or end location is missing, clear all related properties and return
            targetLinePolyline = nil
            targetLineRangePolylineUpper = nil
            targetLineRangePolylineLower = nil
            targetLineRangePolygon = nil
            targetLineDisplay = false
            return
        }

        // Create the main target line path and polyline
        let path = GMSMutablePath()
        path.addLatitude(startLocation.latitude, longitude: startLocation.longitude)
        path.addLatitude(endLocation.latitude, longitude: endLocation.longitude)
        let polyline = GMSPolyline(path: path)
        polyline.strokeWidth = 2.0
        polyline.strokeColor = .blue

        // Calculate the range lines based on the user-defined target range
        let targetRange: CLLocationDirection = self.targetRange
        let upperPath = GMSMutablePath()
        let lowerPath = GMSMutablePath()

        // Calculate the bearing (angle) between the start and end locations
        let startBearing = GMSGeometryHeading(startLocation, endLocation)
        let upperStartBearing = startBearing + 90
        let lowerStartBearing = startBearing - 90

        // Calculate the upper and lower range line start and end locations
        let upperStartLocation = GMSGeometryOffset(startLocation, targetRange, upperStartBearing)
        let lowerStartLocation = GMSGeometryOffset(startLocation, targetRange, lowerStartBearing)
        let upperEndLocation = GMSGeometryOffset(endLocation, targetRange, upperStartBearing)
        let lowerEndLocation = GMSGeometryOffset(endLocation, targetRange, lowerStartBearing)

        // Create the upper and lower range line paths and polylines
        upperPath.add(upperStartLocation)
        upperPath.add(upperEndLocation)
        lowerPath.add(lowerStartLocation)
        lowerPath.add(lowerEndLocation)
        let polylineUpper = GMSPolyline(path: upperPath)
        polylineUpper.strokeWidth = 1.5
        polylineUpper.strokeColor = .black
        let polylineLower = GMSPolyline(path: lowerPath)
        polylineLower.strokeWidth = 1.5
        polylineLower.strokeColor = .black

        // Create a filled polygon connecting the upper and lower range lines
        let polygonPath = GMSMutablePath()
        polygonPath.add(upperStartLocation)
        polygonPath.add(upperEndLocation)
        polygonPath.add(lowerEndLocation)
        polygonPath.add(lowerStartLocation)
        let polygon = GMSPolygon(path: polygonPath)
        polygon.fillColor = UIColor.green.withAlphaComponent(0.2)
        polygon.strokeWidth = 0

        // Update the target line and range line properties
        targetLinePolyline = polyline
        targetLineRangePolylineUpper = polylineUpper
        targetLineRangePolylineLower = polylineLower
        targetLineRangePolygon = polygon
        targetLineDisplay = true
    }
    
    func clearTargetLine() {
        // Remove target line polyline from the map if it exists
        if let polyline = targetLinePolyline {
            polyline.map = nil
            targetLinePolyline = nil
        }

        // Remove target line range polyline upper from the map if it exists
        if let polylineUpper = targetLineRangePolylineUpper {
            polylineUpper.map = nil
            targetLineRangePolylineUpper = nil
        }

        // Remove target line range polyline lower from the map if it exists
        if let polylineLower = targetLineRangePolylineLower {
            polylineLower.map = nil
            targetLineRangePolylineLower = nil
        }

        // Remove target line range polygon from the map if it exists
        if let polygon = targetLineRangePolygon {
            polygon.map = nil
            targetLineRangePolygon = nil
        }
    }
    
    


    
    func toggleTargetLineDisplay() {
        if targetLineDisplay {
            targetLineDisplay = false
        } else {
            targetLineDisplay = true
            drawTargetLine()
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
