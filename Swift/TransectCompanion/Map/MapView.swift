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
        let camera = GMSCameraPosition.camera(
            withLatitude: locationManager.initialLocation?.coordinate.latitude ?? 0,
            longitude: locationManager.initialLocation?.coordinate.longitude ?? 0,
            zoom: 20
        )
        let mapView = GMSMapView.map(withFrame: CGRect.zero, camera: camera)
        
        mapView.isMyLocationEnabled = true
        mapView.settings.myLocationButton = true
        
        mapView.padding = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 0)
        mapView.mapType = .satellite
        mapView.delegate = context.coordinator
//        mapView.settings.rotateGestures = false
        
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
        var parent: MapView
        var locationManager: LocationManager
        var lastKnownLocation: CLLocationCoordinate2D?
        var isUserInteracting = false
        var compassImageView: UIImageView?



        init(_ parent: MapView) {
            self.parent = parent
            self.locationManager = parent.locationManager
        }

        
        func mapView(_ mapView: GMSMapView, didChange position: GMSCameraPosition) {
            // Follow the user's location if shouldFollowUser is true, the user is not interacting with the map, and the current location is available
            if locationManager.shouldFollowUser, !isUserInteracting, let location = locationManager.currentLocationCoordinate2D {
                let currentZoom = mapView.camera.zoom
                let currentBearing = mapView.camera.bearing
                let camera = GMSCameraPosition.camera(withTarget: location, zoom: currentZoom, bearing: currentBearing, viewingAngle: 0)
                mapView.animate(to: camera)
            }
        
        
            
            // Add or update the easting line polyline on the map
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
            
            
            parent.compassHeading = 360 - position.bearing
            parent.updateCompassImage(compassImageView!)
            
            
            
        }
        
        func mapView(_ mapView: GMSMapView, willMove gesture: Bool) {
            if gesture {
                isUserInteracting = true
            }
            else {
                isUserInteracting = false
            }
        }

        func mapView(_ mapView: GMSMapView, idleAt position: GMSCameraPosition) {
            isUserInteracting = false
            
        }
        
        
    }
}

struct MapView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

