#!/usr/bin/env python3
"""
Seed script — posts ~500 realistic Jerusalem listings to the backend API.

Each POST /api/listings triggers real-time embedding generation via OpenAI,
so semantic search works on every listing immediately after it is created.

Usage:
    python3 seed-500.py <username> <password>

The backend must be running on http://localhost:8080.
"""

import sys
import json
import random
import urllib.request
import urllib.error

BASE_URL = "http://localhost:8080/api"

# ── Neighborhood data ─────────────────────────────────────────────────────────
# Each neighborhood has streets, a rent price range, a sale price range,
# and descriptive tags used to vary listing descriptions.

NEIGHBORHOODS = {
    "Rechavia": {
        "streets": ["Ramban", "Aza", "Ibn Ezra", "Keren Kayemet", "Alfasi", "Radak", "Marcus"],
        "rent": (7000, 20000),
        "sale": (2200000, 6500000),
        "tags": ["prestigious quiet streets", "near the President's residence",
                 "tree-lined boulevards", "central location", "upscale neighborhood"],
    },
    "Talbiyeh": {
        "streets": ["Hovevei Zion", "Pinsker", "Emile Botta", "Chopin", "Gaza"],
        "rent": (9000, 25000),
        "sale": (3000000, 8000000),
        "tags": ["most prestigious area", "embassies nearby", "stunning stone architecture",
                 "walking distance to downtown", "quiet luxury"],
    },
    "German Colony": {
        "streets": ["Emek Refaim", "Bethlehem Road", "Rachel Imenu", "Ben Zakai", "Dorot Rishonim"],
        "rent": (7500, 18000),
        "sale": (2000000, 6000000),
        "tags": ["vibrant cafe culture", "restaurants and boutiques on Emek Refaim",
                 "family-friendly", "beautiful stone buildings", "weekend market"],
    },
    "Baka": {
        "streets": ["Yehuda", "Beit Lechem", "Rabbi Akiva", "Yiftah", "Ha-Rav Frank"],
        "rent": (5500, 14000),
        "sale": (1500000, 4500000),
        "tags": ["popular family neighborhood", "quiet tree-lined streets",
                 "good schools", "community feel", "young families and professionals"],
    },
    "Katamon": {
        "streets": ["Palmach", "Ben Tabai", "Hativat Yerushalayim", "Uziel", "Alkalai"],
        "rent": (4800, 12000),
        "sale": (1200000, 3800000),
        "tags": ["lively neighborhood", "cafes and restaurants",
                 "good transport links", "central", "religious and secular mix"],
    },
    "Nachlaot": {
        "streets": ["Nachlaot", "Agrippas", "Bezalel", "Shvil HaMifal", "Mishkan"],
        "rent": (5000, 11000),
        "sale": (1300000, 3200000),
        "tags": ["historic charm", "stone arches and alleyways", "artistic community",
                 "steps from the shuk", "bohemian old Jerusalem feel"],
    },
    "Mahane Yehuda": {
        "streets": ["Agrippas", "HaTurim", "Beit Yaakov", "Jaffa Road", "Harav Kook"],
        "rent": (3800, 9000),
        "sale": (950000, 2500000),
        "tags": ["two-minute walk from the market", "vibrant food scene",
                 "central bus station nearby", "lively nightlife", "affordable central living"],
    },
    "Arnona": {
        "streets": ["Yehoshua Ben Nun", "Kovshei Katamon", "Shmuel Hanagid", "Uziel", "Rivka"],
        "rent": (5000, 12000),
        "sale": (1300000, 3500000),
        "tags": ["quiet residential", "southern Jerusalem", "parks and green areas",
                 "family-friendly", "near Pat Forest and nature trails"],
    },
    "Har Nof": {
        "streets": ["Givat Shaul", "Agassi", "HaRav Braun", "Shaulzon", "HaRav Shmuel"],
        "rent": (4200, 9000),
        "sale": (1000000, 2600000),
        "tags": ["established religious neighborhood", "overlooking Jerusalem hills",
                 "peaceful atmosphere", "many synagogues", "large community"],
    },
    "Ramot": {
        "streets": ["Golda Meir", "HaGdud HaIvri", "Mahal", "Nahal Soreq", "Nahal Haela"],
        "rent": (4500, 10000),
        "sale": (1100000, 2900000),
        "tags": ["large family apartments", "northern Jerusalem", "supermarkets and shops",
                 "parks and playgrounds", "good school district"],
    },
    "Gilo": {
        "streets": ["Gilo Road", "Yitzhak Navon", "Bar Lev", "Dagan", "Shmaryahu Levin"],
        "rent": (4200, 9000),
        "sale": (950000, 2500000),
        "tags": ["panoramic views of the Judean Hills", "affordable",
                 "spacious apartments", "good schools", "established community"],
    },
    "French Hill": {
        "streets": ["Hazan", "Moshe Sharett", "Churchill", "Bruria", "Yitzhak Epstein"],
        "rent": (4800, 10000),
        "sale": (1100000, 2800000),
        "tags": ["near Hebrew University", "international academic community",
                 "great views of the Old City", "multicultural neighborhood"],
    },
    "Bayit VeGan": {
        "streets": ["Bayit VeGan", "Golomb", "Weizmann", "Even Sapir", "Hameyasdim"],
        "rent": (4800, 10500),
        "sale": (1100000, 3000000),
        "tags": ["quiet and green", "western Jerusalem", "spacious family apartments",
                 "near Hadassah hospital", "shaded streets and parks"],
    },
    "Talpiot": {
        "streets": ["Pierre Koenig", "HaMasger", "Yad Harutzim", "Hebron Road", "HaBoneh"],
        "rent": (4200, 9500),
        "sale": (1000000, 2700000),
        "tags": ["convenient location", "close to Malha Mall", "good transport links",
                 "affordable", "improving neighborhood"],
    },
    "Pisgat Zeev": {
        "streets": ["Moshe Dayan", "Amnon VeTamar", "Moshe Sneh", "Mivtza Kadesh"],
        "rent": (3800, 8500),
        "sale": (900000, 2200000),
        "tags": ["large northern neighborhood", "affordable family housing",
                 "light rail access", "good schools", "family-oriented community"],
    },
    "Old City": {
        "streets": ["Jewish Quarter Road", "Batei Mahse", "Plugat HaKotel", "HaYehudim"],
        "rent": (7000, 20000),
        "sale": (2000000, 7000000),
        "tags": ["steps from the Western Wall", "historic Jewish Quarter",
                 "cobblestone alleyways", "rich history", "rare opportunity"],
    },
    "Pat": {
        "streets": ["Pat", "Mishmar HaGvul", "Sdeh Boker", "Ein Gedi"],
        "rent": (3500, 7500),
        "sale": (900000, 2000000),
        "tags": ["affordable", "southwestern Jerusalem", "quiet local feel",
                 "easy highway access", "good value for money"],
    },
}

TITLE_TEMPLATES = [
    "{beds}BR {adj} Apartment in {hood}",
    "Spacious {beds}-Bedroom Flat in {hood}",
    "Modern {beds}BR with {feature} in {hood}",
    "Charming Apartment in {hood}",
    "Sunny {beds}BR in the Heart of {hood}",
    "Renovated {beds}-Bedroom in {hood}",
    "Bright {beds}BR Apartment - {hood}",
    "Beautiful {beds}BR with Views in {hood}",
    "{beds}BR Family Home in {hood}",
    "Classic Jerusalem Stone {beds}BR in {hood}",
    "Quiet {beds}BR on Tree-Lined Street in {hood}",
    "High Floor {beds}BR with Panoramic Views in {hood}",
    "Penthouse {beds}BR in {hood}",
    "{beds}BR Corner Apartment in {hood}",
    "Well-Maintained {beds}BR in {hood}",
    "Ground Floor {beds}BR with Garden - {hood}",
    "{adj} {beds}BR in Desirable {hood}",
]

ADJECTIVES = [
    "luxurious", "cozy", "elegant", "renovated", "modern", "charming",
    "quiet", "sunny", "spacious", "stylish", "well-appointed", "lovely",
]

FEATURES = [
    "balcony", "parking", "mamad", "garden", "city views",
    "renovated kitchen", "shabbat elevator", "large terrace",
]

DESC_INTROS = [
    "A wonderful opportunity in one of Jerusalem's finest neighborhoods.",
    "This beautiful apartment offers everything you need for comfortable Jerusalem living.",
    "Rarely available property on a quiet, desirable street.",
    "Enjoy the best of Jerusalem from this well-located apartment.",
    "An excellent choice for families, couples, or professionals.",
    "Located on a calm residential street with easy access to everything.",
    "Light-filled apartment in a prime location.",
    "Meticulously maintained and move-in ready.",
    "A true gem — don't miss this opportunity.",
    "Excellent investment property or primary residence.",
    "A rare find in this highly desirable area.",
]

DESC_INTERIORS = [
    "Large living room with plenty of natural light throughout the day.",
    "Spacious rooms with generous closet space and storage.",
    "Renovated kitchen with modern appliances and plenty of counter space.",
    "Open-plan living and dining area, perfect for entertaining.",
    "Original Jerusalem stone walls add warmth and character.",
    "Beautifully updated bathrooms with quality fixtures.",
    "High ceilings and arched doorways throughout.",
    "New flooring and freshly painted throughout.",
    "Designer kitchen with top-of-the-line appliances.",
    "Well-proportioned rooms, each with natural light.",
]

DESC_LOCATIONS = [
    "Walking distance to parks, cafes, and supermarkets.",
    "Steps from public transport and main bus lines.",
    "Minutes to downtown Jerusalem and the main market.",
    "Close to top-rated schools and kindergartens.",
    "Easy access to the light rail and main roads.",
    "Quiet street yet central to everything.",
    "Near excellent restaurants, shops, and cultural venues.",
    "Convenient to highways for those commuting outside Jerusalem.",
]

DESC_BUILDINGS = [
    "Well-maintained building with attentive management.",
    "Newly renovated lobby and common areas.",
    "Small boutique building with only 8 units.",
    "Building with strong community of long-term residents.",
    "Recently refreshed building exterior.",
]

DESC_BALCONIES = [
    "Private balcony overlooking the quiet street.",
    "Large sunny balcony perfect for morning coffee.",
    "Wrap-around terrace with stunning city views.",
    "South-facing balcony with all-day sunlight.",
    "Spacious sukkah balcony, ideal for the holiday.",
    "Balcony with views of the Jerusalem hills.",
]


def build_description(neighborhood, beds, has_balcony, has_parking, has_mamad, listing_type, tags):
    tag = random.choice(tags)
    parts = [
        random.choice(DESC_INTROS),
        f"Situated in {neighborhood}, known for its {tag}.",
        random.choice(DESC_INTERIORS),
    ]
    if has_balcony:
        parts.append(random.choice(DESC_BALCONIES))
    parts.append(random.choice(DESC_LOCATIONS))
    parts.append(random.choice(DESC_BUILDINGS))

    extras = []
    if has_parking:
        extras.append("private parking included")
    if has_mamad:
        extras.append("mamad (safe room) for peace of mind")
    if extras:
        parts.append(f"Additional highlights: {' and '.join(extras)}.")
    if listing_type == "SALE":
        parts.append("A strong investment in the Jerusalem real estate market.")

    return " ".join(parts)


def generate_listing(neighborhood_name, rng):
    hood = NEIGHBORHOODS[neighborhood_name]
    listing_type = rng.choices(["RENT", "SALE"], weights=[65, 35])[0]

    beds = rng.choices([1, 2, 3, 4, 5], weights=[12, 28, 32, 20, 8])[0]
    baths = max(1, beds - 1)
    size_ranges = {1: (30, 58), 2: (55, 90), 3: (80, 130), 4: (110, 175), 5: (150, 280)}
    size_sqm = rng.randint(*size_ranges[beds])

    if listing_type == "RENT":
        lo, hi = hood["rent"]
        price = rng.randint(lo + (beds - 1) * 800, min(int(hi * 1.2), hi + (beds - 1) * 1500))
        price = round(price / 100) * 100
    else:
        lo, hi = hood["sale"]
        price = rng.randint(lo + (beds - 1) * 200000, min(int(hi * 1.3), hi + (beds - 1) * 350000))
        price = round(price / 10000) * 10000

    street = rng.choice(hood["streets"])
    address = f"{street} {rng.randint(1, 80)}"

    has_balcony       = rng.random() < 0.65
    has_parking       = rng.random() < 0.45
    has_washer_dryer  = rng.random() < 0.75
    has_dishwasher    = rng.random() < 0.55
    has_ac            = rng.random() < 0.80
    has_elevator      = rng.random() < 0.60
    is_furnished      = rng.random() < 0.28
    is_pet_friendly   = rng.random() < 0.35
    has_shabbat_elev  = has_elevator and rng.random() < 0.50
    has_sukkah        = has_balcony and rng.random() < 0.40
    has_mamad         = rng.random() < 0.45

    title = rng.choice(TITLE_TEMPLATES).format(
        beds=beds,
        adj=rng.choice(ADJECTIVES),
        feature=rng.choice(FEATURES),
        hood=neighborhood_name,
    )

    description = build_description(
        neighborhood_name, beds, has_balcony, has_parking, has_mamad, listing_type, hood["tags"]
    )

    return {
        "title": title,
        "description": description,
        "address": address,
        "neighborhood": neighborhood_name,
        "price": price,
        "bedrooms": beds,
        "bathrooms": baths,
        "sizeInSqm": size_sqm,
        "listingType": listing_type,
        "hasParking": has_parking,
        "hasBalcony": has_balcony,
        "hasWasherDryer": has_washer_dryer,
        "hasDishwasher": has_dishwasher,
        "hasAC": has_ac,
        "hasElevator": has_elevator,
        "isFurnished": is_furnished,
        "isPetFriendly": is_pet_friendly,
        "hasShabbatElevator": has_shabbat_elev,
        "hasSukkahBalcony": has_sukkah,
        "hasMamad": has_mamad,
    }


def post_json(url, data, token):
    body = json.dumps(data).encode("utf-8")
    req = urllib.request.Request(
        url,
        data=body,
        headers={
            "Content-Type": "application/json",
            "Authorization": f"Bearer {token}",
        },
        method="POST",
    )
    with urllib.request.urlopen(req, timeout=30) as resp:
        return json.loads(resp.read())


def get_token(username, password):
    data = json.dumps({"username": username, "password": password}).encode("utf-8")
    req = urllib.request.Request(
        f"{BASE_URL}/auth/login",
        data=data,
        headers={"Content-Type": "application/json"},
        method="POST",
    )
    with urllib.request.urlopen(req, timeout=10) as resp:
        return json.loads(resp.read())["token"]


# ── Main ──────────────────────────────────────────────────────────────────────

if len(sys.argv) != 3:
    print("Usage: python3 seed-500.py <username> <password>")
    sys.exit(1)

username, password = sys.argv[1], sys.argv[2]

print("Logging in...")
try:
    token = get_token(username, password)
    print("Logged in successfully.\n")
except Exception as e:
    print(f"Login failed: {e}")
    sys.exit(1)

# Generate listings: spread evenly across all neighborhoods
rng = random.Random(42)
neighborhood_names = list(NEIGHBORHOODS.keys())
target = 500

listings = []
base = target // len(neighborhood_names)
remainder = target % len(neighborhood_names)
for i, hood in enumerate(neighborhood_names):
    count = base + (1 if i < remainder else 0)
    for _ in range(count):
        listings.append(generate_listing(hood, rng))

rng.shuffle(listings)

print(f"Seeding {len(listings)} listings across {len(neighborhood_names)} neighborhoods.")
print("Each POST triggers an OpenAI embedding — this will take a few minutes.\n")

ok = 0
failed = 0

for i, listing in enumerate(listings, start=1):
    try:
        post_json(f"{BASE_URL}/listings", listing, token)
        ok += 1
        if i % 25 == 0 or i == len(listings):
            print(f"  [{i}/{len(listings)}] {ok} created, {failed} failed")
    except urllib.error.HTTPError as e:
        body = e.read().decode()
        print(f"  [{i}] HTTP {e.code} — {listing['title'][:50]}: {body[:80]}")
        failed += 1
    except Exception as e:
        print(f"  [{i}] Error — {listing['title'][:50]}: {e}")
        failed += 1

print(f"\nDone. {ok} listings created, {failed} failed.")
if failed > 0:
    print("Some listings failed. Check the backend logs for details.")
