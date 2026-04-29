#!/bin/bash

BASE="http://localhost:8080/api/listings"

echo "Adding test listings..."

curl -s -X POST $BASE \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Luxury Penthouse with Panoramic Views",
    "description": "Stunning penthouse on the top floor with breathtaking views of the Jerusalem hills. High ceilings, open plan living, marble finishes throughout. Perfect for those seeking the finest in city living.",
    "address": "Hovevei Zion 12",
    "neighborhood": "Talbiyeh",
    "price": 25000,
    "bedrooms": 4,
    "bathrooms": 3,
    "sizeInSqm": 220,
    "listingType": "RENT",
    "hasParking": true,
    "hasBalcony": true,
    "hasWasherDryer": true,
    "hasDishwasher": true,
    "hasAC": true,
    "hasElevator": true,
    "isFurnished": true,
    "isPetFriendly": false,
    "hasShabbatElevator": true,
    "hasSukkahBalcony": true,
    "hasMamad": true
  }' | python3 -c "import sys,json; d=json.load(sys.stdin); print('Added:', d.get('title','error'))"

curl -s -X POST $BASE \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Charming Studio Steps from Mahane Yehuda",
    "description": "Cozy furnished studio in the heart of the city, a two-minute walk from the shuk. Ideal for a single person or couple who want to be in the middle of everything. Lively street, great restaurants nearby.",
    "address": "Agrippas 45",
    "neighborhood": "Mahane Yehuda",
    "price": 4200,
    "bedrooms": 1,
    "bathrooms": 1,
    "sizeInSqm": 35,
    "listingType": "RENT",
    "hasParking": false,
    "hasBalcony": false,
    "hasWasherDryer": true,
    "hasDishwasher": false,
    "hasAC": true,
    "hasElevator": false,
    "isFurnished": true,
    "isPetFriendly": false,
    "hasShabbatElevator": false,
    "hasSukkahBalcony": false,
    "hasMamad": false
  }' | python3 -c "import sys,json; d=json.load(sys.stdin); print('Added:', d.get('title','error'))"

curl -s -X POST $BASE \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Sunny Family Apartment in Baka",
    "description": "Spacious and bright family apartment on a quiet tree-lined street. Large rooms, great for kids. Building has a shared garden. Dog-friendly building with easy street parking.",
    "address": "Yehuda 18",
    "neighborhood": "Baka",
    "price": 9500,
    "bedrooms": 4,
    "bathrooms": 2,
    "sizeInSqm": 130,
    "listingType": "RENT",
    "hasParking": false,
    "hasBalcony": true,
    "hasWasherDryer": true,
    "hasDishwasher": true,
    "hasAC": true,
    "hasElevator": false,
    "isFurnished": false,
    "isPetFriendly": true,
    "hasShabbatElevator": false,
    "hasSukkahBalcony": true,
    "hasMamad": true
  }' | python3 -c "import sys,json; d=json.load(sys.stdin); print('Added:', d.get('title','error'))"

curl -s -X POST $BASE \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Renovated 2BR in the Heart of Nachlaot",
    "description": "Beautifully renovated apartment in one of Jerusalems most charming historic neighborhoods. Stone walls, arched doorways, and modern kitchen. Quiet alley with old Jerusalem character.",
    "address": "Nachlaot 7",
    "neighborhood": "Nachlaot",
    "price": 7000,
    "bedrooms": 2,
    "bathrooms": 1,
    "sizeInSqm": 75,
    "listingType": "RENT",
    "hasParking": false,
    "hasBalcony": true,
    "hasWasherDryer": true,
    "hasDishwasher": false,
    "hasAC": true,
    "hasElevator": false,
    "isFurnished": false,
    "isPetFriendly": true,
    "hasShabbatElevator": false,
    "hasSukkahBalcony": false,
    "hasMamad": false
  }' | python3 -c "import sys,json; d=json.load(sys.stdin); print('Added:', d.get('title','error'))"

curl -s -X POST $BASE \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Private Villa with Garden in Ein Kerem",
    "description": "Rare standalone villa in the picturesque village of Ein Kerem. Large private garden, fruit trees, total privacy and quiet. A 10-minute drive from the city center. Perfect for families wanting space and nature.",
    "address": "Ein Kerem 3",
    "neighborhood": "Ein Kerem",
    "price": 4500000,
    "bedrooms": 5,
    "bathrooms": 3,
    "sizeInSqm": 280,
    "listingType": "SALE",
    "hasParking": true,
    "hasBalcony": true,
    "hasWasherDryer": true,
    "hasDishwasher": true,
    "hasAC": true,
    "hasElevator": false,
    "isFurnished": false,
    "isPetFriendly": true,
    "hasShabbatElevator": false,
    "hasSukkahBalcony": true,
    "hasMamad": true
  }' | python3 -c "import sys,json; d=json.load(sys.stdin); print('Added:', d.get('title','error'))"

curl -s -X POST $BASE \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Affordable Studio in Katamon",
    "description": "Simple and clean studio apartment, great for students or young professionals on a budget. Close to bus lines and supermarkets. No frills but everything you need.",
    "address": "Palmach 22",
    "neighborhood": "Katamon",
    "price": 3800,
    "bedrooms": 1,
    "bathrooms": 1,
    "sizeInSqm": 30,
    "listingType": "RENT",
    "hasParking": false,
    "hasBalcony": false,
    "hasWasherDryer": false,
    "hasDishwasher": false,
    "hasAC": false,
    "hasElevator": false,
    "isFurnished": false,
    "isPetFriendly": false,
    "hasShabbatElevator": false,
    "hasSukkahBalcony": false,
    "hasMamad": false
  }' | python3 -c "import sys,json; d=json.load(sys.stdin); print('Added:', d.get('title','error'))"

echo "Done! Run reindex next:"
echo "curl -X POST http://localhost:8080/api/listings/reindex"
