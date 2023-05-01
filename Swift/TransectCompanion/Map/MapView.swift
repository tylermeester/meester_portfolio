//
//  MapView.swift
//  TransectCompanion
//
//  Created by Tyler Meester on 4/26/23.
//

import SwiftUI
import GoogleMaps

struct MapView: UIViewRepresentable {
    @ObservedObject var locationManager: LocationManager
    @Binding var rotation: Double
    @State private var compassHeading: Double = 0

    
    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }

    func makeUIView(context: Context) -> GMSMapView {
        // Create a camera using the initial location's latitude and longitude.
        // If there's no initial location available, use 0 as the default latitude and longitude.
        let camera = GMSCameraPosition.camera(
            withLatitude: locationManager.initialLocation?.coordinate.latitude ?? 0,
            longitude: locationManager.initialLocation?.coordinate.longitude ?? 0,
            zoom: 20
        )
        
        // Create a GMSMapView with the camera defined above.
        // The frame is set to CGRect.zero, which means the map view will be sized by its parent view.
        let mapView = GMSMapView.map(withFrame: CGRect.zero, camera: camera)
        
        // Map Customizations
        mapView.isMyLocationEnabled = true
        mapView.settings.myLocationButton = true
        mapView.padding = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 0)
        mapView.mapType = .satellite
        
        // Assign the map view's delegate to the coordinator, which handles events such as map updates.
        mapView.delegate = context.coordinator

        return mapView
    }
    
    func updateUIView(_ mapView: GMSMapView, context: Self.Context) {
        // Follow the user's location if shouldFollowUser is true, keeps map bearing the same
        if locationManager.shouldFollowUser, !context.coordinator.isUserInteracting, let location = locationManager.currentLocationCoordinate2D {
            let currentZoom = mapView.camera.zoom
            let currentBearing = mapView.camera.bearing
            let camera = GMSCameraPosition.camera(withTarget: location, zoom: currentZoom, bearing: currentBearing, viewingAngle: 0)
            mapView.animate(to: camera)
        }

        // Remove the existing polyline from the map if it exists
        locationManager.targetLinePolyline?.map = nil
        locationManager.targetLineRangePolylineUpper?.map = nil
        locationManager.targetLineRangePolylineLower?.map = nil
        locationManager.targetLineRangePolygon?.map = nil

        // Add or update the target line polyline on the map
        if locationManager.targetLineDisplay {
            locationManager.targetLinePolyline?.map = mapView
            locationManager.targetLineRangePolylineUpper?.map = mapView
            locationManager.targetLineRangePolylineLower?.map = mapView
            locationManager.targetLineRangePolygon?.map = mapView
        }
    }
    
    func updateCompassImage(_ compassImageView: UIImageView) {
        let rotationInRadians = CGFloat(compassHeading * .pi / 180)
        compassImageView.transform = CGAffineTransform(rotationAngle: rotationInRadians)
    }
    
    

    class Coordinator: NSObject, GMSMapViewDelegate {
        // Store references to the parent MapView and the locationManager.
        var parent: MapView
        var locationManager: LocationManager
    
        var isUserInteracting = false
        var compassImageView: UIImageView?


        // Initialize the coordinator with a reference to the parent MapView.
        init(_ parent: MapView) {
            self.parent = parent
            self.locationManager = parent.locationManager
        }

        
        // This method is called when the map view's camera position changes.
        func mapView(_ mapView: GMSMapView, didChange position: GMSCameraPosition) {
            // If the user is not interacting with the map and shouldFollowUser is true, update the camera to follow the user's location.
            if locationManager.shouldFollowUser, !isUserInteracting, let location = locationManager.currentLocationCoordinate2D {
                let currentZoom = mapView.camera.zoom
                let currentBearing = mapView.camera.bearing
                let camera = GMSCameraPosition.camera(withTarget: location, zoom: currentZoom, bearing: currentBearing, viewingAngle: 0)
                mapView.animate(to: camera)
            }
        
        
            
            // Update the map view with target line data depending on whether targetLineDisplay is true or false.
            if parent.locationManager.targetLineDisplay {
                parent.locationManager.targetLinePolyline?.map = mapView
                parent.locationManager.targetLineRangePolylineUpper?.map = mapView
                parent.locationManager.targetLineRangePolylineLower?.map = mapView
                parent.locationManager.targetLineRangePolygon?.map = mapView
            } else {
                parent.locationManager.targetLinePolyline?.map = nil
                parent.locationManager.targetLineRangePolylineUpper?.map = nil
                parent.locationManager.targetLineRangePolylineLower?.map = nil
                parent.locationManager.targetLineRangePolygon?.map = nil
            }
            
            // If the compass image view hasn't been created yet, create and configure it.
            if compassImageView == nil {
                let compassImage = UIImage(named: "compass")
                let imageView = UIImageView(image: compassImage)
                imageView.contentMode = .scaleAspectFit
                imageView.frame = CGRect(x: 0, y: 0, width: 40, height: 40)
                mapView.addSubview(imageView)

                imageView.translatesAutoresizingMaskIntoConstraints = false
                NSLayoutConstraint.activate([
                    imageView.topAnchor.constraint(equalTo: mapView.safeAreaLayoutGuide.topAnchor, constant: 8),
                    imageView.trailingAnchor.constraint(equalTo: mapView.safeAreaLayoutGuide.trailingAnchor, constant: -8),
                    imageView.widthAnchor.constraint(equalToConstant: 40),
                    imageView.heightAnchor.constraint(equalToConstant: 40)
                ])
                
                compassImageView = imageView
            }
            
            // Update the compass heading and image based on the map view's camera bearing.
            parent.compassHeading = 360 - position.bearing
            parent.updateCompassImage(compassImageView!)
            
            
            
        }
        
        func mapView(_ mapView: GMSMapView, willMove gesture: Bool) {
            // Update the isUserInteracting flag based on whether the movement is caused by a user gesture.
            if gesture {
                isUserInteracting = true
            }
            else {
                isUserInteracting = false
            }
        }

        func mapView(_ mapView: GMSMapView, idleAt position: GMSCameraPosition) {
            // Reset the isUserInteracting flag when the map view becomes idle (i.e., stops moving).
            isUserInteracting = false
        }
        
        
    }
}

struct MapView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

