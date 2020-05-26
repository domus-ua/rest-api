from requests import *
from constants import *

BASE_URL = "http://0.0.0.0:8080/api"

#######################################
#          WIPE DATABASE              #
#######################################
get(url=f"{BASE_URL}/reset-db")


#######################################
#          CREATE LOCATARIOS          #
#######################################
data = {"email": "locatario1@mail.com", "firstName": "Rafael", "lastName": "Gomes", "password": "locatario1", "phoneNumber": "934125484", "sex": "M", "photo": LOCATARIO_1_PHOTO}
locatario_1_id = post(url=f"{BASE_URL}/locatarios", json=data).json()["id"]

data = {"email": "locatario2@mail.com", "firstName": "José", "lastName": "Amaral", "password": "locatario2", "phoneNumber": "910294284", "sex": "M", "photo": LOCATARIO_2_PHOTO}
locatario_2_id = post(url=f"{BASE_URL}/locatarios", json=data).json()["id"]

data = {"email": "locatario3@mail.com", "firstName": "Diana", "lastName": "Fonseca", "password": "locatario3", "phoneNumber": "969230471", "sex": "F", "photo": LOCATARIO_3_PHOTO}
locatario_3_id = post(url=f"{BASE_URL}/locatarios", json=data).json()["id"]


#######################################
#          CREATE LOCADORES           #
#######################################
data = {"email": "locador1@mail.com", "firstName": "Carlos", "lastName": "Santos", "password": "locador1", "phoneNumber": "926870150", "sex": "M", "photo": LOCADOR_1_PHOTO}
locador_1_id = post(url=f"{BASE_URL}/locadores", json=data).json()["id"]

data = {"email": "locador2@mail.com", "firstName": "Diogo", "lastName": "Marques", "password": "locador2", "phoneNumber": "964501288", "sex": "M", "photo": LOCADOR_2_PHOTO}
locador_2_id = post(url=f"{BASE_URL}/locadores", json=data).json()["id"]

data = {"email": "locador3@mail.com", "firstName": "Joana", "lastName": "Amaral", "password": "locador3", "phoneNumber": "931020653", "sex": "F", "photo": LOCADOR_3_PHOTO}
locador_3_id = post(url=f"{BASE_URL}/locadores", json=data).json()["id"]

data = {"email": "locador4@mail.com", "firstName": "Joana", "lastName": "Silva", "password": "locador4", "phoneNumber": "966092333", "sex": "F", "photo": LOCADOR_4_PHOTO}
locador_4_id = post(url=f"{BASE_URL}/locadores", json=data).json()["id"]


#######################################
#            CREATE HOUSES            #
#######################################
house_photos = [HOUSE_1_PHOTO_1]
data = {"street": "Rua de Norton Matos nº123", "city": "Aveiro, Portugal", "postalCode": "3810-064", "noRooms": 2, "noBathrooms": 1, "noGarages": 0, "habitableArea": 70, "available": True, "price": 250, "name": "T2 em ótimo estado", "description": "Totalmente remodelado em 2018, em tipologia de T2, com WI-FI.", "propertyFeatures": "Internet;Secador;Máquina de Lavar Loiça", "photos": house_photos, "locador": {"id": locador_1_id}}
house_1_id = post(url=f"{BASE_URL}/houses", json=data).json()["id"]

house_photos = [HOUSE_2_PHOTO_1, HOUSE_2_PHOTO_2]
data = {"street": "Urbanização Chave 9-5", "city": "Aveiro, Portugal", "postalCode": "3810-081", "noRooms": 3, "noBathrooms": 2, "noGarages": 1, "habitableArea": 110, "available": True, "price": 375, "name": "T3 c/ Garagem", "description": "Mobília um pouco antiga, mas bem conservada, com acesso a garagem privada", "propertyFeatures": "Internet;Garagem;TV;Máquina de Lavar Loiça", "photos": house_photos, "locador": {"id": locador_1_id}}
house_2_id = post(url=f"{BASE_URL}/houses", json=data).json()["id"]


#######################################
#       ADD HOUSES TO WISHLIST        #
#######################################
data = {"locatarioId": locatario_2_id, "houseId": house_1_id}
post(url=f"{BASE_URL}/locatarios/wishlist", json=data)


#######################################
#            CREATE RENTS             #
#######################################


#######################################
#           CREATE REVIEWS            #
#######################################
data = {"houseId": house_2_id, "locatarioId": locatario_2_id, "comment": "My beautiful comment", "rating": 3.5}
post(url=f"{BASE_URL}/houses/reviews", json=data)