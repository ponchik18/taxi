INSERT INTO Ride (passenger_id, driver_id, pick_up_location, drop_location, start_time, end_time, cost, status, is_promo_code_applied)
VALUES
    (1, 101, 'LocationA', 'LocationB', '2024-01-05T10:00:00', '2024-01-05T11:30:00', 100, 'COMPLETED', FALSE),
    (2, 102, 'LocationC', 'LocationD', '2024-01-05T12:00:00', '2024-01-05T13:45:00', 30.75, 'IN_PROGRESS', FALSE),
    (3, 103, 'LocationE', 'LocationF', '2024-01-05T14:30:00', '2024-01-05T16:00:00', 20.00, 'DRIVER_EN_ROUTE', TRUE),
    (4, 104, 'LocationG', 'LocationH', '2024-01-05T17:15:00', '2024-01-05T18:45:00', 15.80, 'IN_PROGRESS', FALSE);