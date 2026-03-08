# Book-My-Stay-App

### Use Case 4: Reservation Confirmation & Room Allocation

Adds confirmation + unique room allocation:
- FIFO: process next/all from queue
- Unique room IDs per type (e.g., SINGLE-001)
- Immediate inventory decrement
- No double-booking (IDs stored in a set)
- View assigned rooms