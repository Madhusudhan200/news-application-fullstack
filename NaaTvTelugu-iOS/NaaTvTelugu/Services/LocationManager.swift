import Foundation
import CoreLocation

public class LocationManager: NSObject, ObservableObject, CLLocationManagerDelegate {
    private let manager = CLLocationManager()
    @Published public var userLocation: String = "పొన్నమండ, కోనసీమ"
    @Published public var isGpsActive: Bool = true
    
    public override init() {
        super.init()
        manager.delegate = self
        manager.desiredAccuracy = kCLLocationAccuracyBest
        manager.requestWhenInUseAuthorization()
        manager.startUpdatingLocation()
    }
    
    public func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        guard let location = locations.last else { return }
        
        let geocoder = CLGeocoder()
        geocoder.reverseGeocodeLocation(location) { [weak self] placemarks, error in
            guard let self = self, let mark = placemarks?.first, error == nil else { return }
            
            let locality = mark.locality ?? mark.subLocality ?? "పొన్నమండ"
            let subAdmin = mark.subAdministrativeArea ?? mark.administrativeArea ?? "కోనసీమ"
            DispatchQueue.main.async {
                self.userLocation = "\(locality), \(subAdmin)"
                self.isGpsActive = true
            }
        }
    }
    
    public func setManualLocation(_ location: String) {
        self.userLocation = location
        self.isGpsActive = false
    }
}
