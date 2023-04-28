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

    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }

    func makeUIView(context: Context) -> GMSMapView {
        let camera = GMSCameraPosition.camera(
            withLatitude: locationManager.initialLocation?.coordinate.latitude ?? 0,
            longitude: locationManager.initialLocation?.coordinate.longitude ?? 0,
            zoom: 15
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
    }

    class Coordinator: NSObject, GMSMapViewDelegate {
        var parent: MapView

        init(_ parent: MapView) {
            self.parent = parent
        }

        // You can implement GMSMapViewDelegate methods here if needed
    }
}

struct MapView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

