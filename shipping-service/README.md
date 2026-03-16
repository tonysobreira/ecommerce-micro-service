# shipping-service

Service responsible for shipping methods, shipment creation and tracking timeline.

## Main endpoints
- `POST /shipping/methods`
- `GET /shipping/methods`
- `POST /shipping/shipments`
- `POST /shipping/shipments/{shipmentId}/tracking`
- `GET /shipping/shipments/{shipmentId}/tracking`
- `GET /shipping/shipments/order/{orderId}` (owner or admin only)
