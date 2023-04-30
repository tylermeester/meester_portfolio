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
    @State private var eastingLinePolyline: GMSPolyline? = nil

    
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
        mapView.settings.rotateGestures = false
        
        return mapView
    }
    
    func updateUIView(_ mapView: GMSMapView, context: Self.Context) {
        mapView.animate(toBearing: rotation)

        // Remove the existing polyline from the map if it exists
        locationManager.targetLinePolyline?.map = nil

        // Add or update the target line polyline on the map
        if let polyline = locationManager.targetLinePolyline {
            polyline.map = mapView
        }
    }
    
    func updateCompassImage(_ compassImageView: UIImageView, mapView: GMSMapView) {
        let rotationInRadians = CGFloat(rotation * .pi / 180)
        compassImageView.transform = CGAffineTransform(rotationAngle: rotationInRadians)
    }
    

    class Coordinator: NSObject, GMSMapViewDelegate {
        var parent: MapView
        var compassImageView: UIImageView?

        init(_ parent: MapView) {
            self.parent = parent
        }

        func mapView(_ mapView: GMSMapView, idleAt position: GMSCameraPosition) {
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
            
            parent.updateCompassImage(compassImageView!, mapView: mapView)
            
            // Add or update the easting line polyline on the map
            if parent.locationManager.targetLineDisplay {
                parent.locationManager.targetLinePolyline?.map = mapView
                parent.locationManager.resetTargetLineDisplay()
            } else {
                parent.locationManager.targetLinePolyline?.map = nil
            }
            
        }
    }
}

struct MapView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

