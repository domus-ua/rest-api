from requests import *
from constants import *
import argparse
import sys


def main(base_url):
    #######################################
    #          WIPE DATABASE              #
    #######################################
    get(url=f"{base_url}/reset-db")

    #######################################
    #          CREATE LOCATARIOS          #
    #######################################
    data = {"email": "locatario1@mail.com", "firstName": "Rafael", "lastName": "Gomes",
            "password": "locatario1", "phoneNumber": "934125484", "sex": "M", "photo": LOCATARIO_1_PHOTO}
    locatario_1_id = post(url=f"{base_url}/locatarios", json=data).json()["id"]

    data = {"email": "locatario2@mail.com", "firstName": "José", "lastName": "Amaral",
            "password": "locatario2", "phoneNumber": "910294284", "sex": "M", "photo": LOCATARIO_2_PHOTO}
    locatario_2_id = post(url=f"{base_url}/locatarios", json=data).json()["id"]

    data = {"email": "locatario3@mail.com", "firstName": "Diana", "lastName": "Fonseca",
            "password": "locatario3", "phoneNumber": "969230471", "sex": "F", "photo": LOCATARIO_3_PHOTO}
    locatario_3_id = post(url=f"{base_url}/locatarios", json=data).json()["id"]

    #######################################
    #          CREATE LOCADORES           #
    #######################################
    data = {"email": "locador1@mail.com", "firstName": "Carlos", "lastName": "Santos",
            "password": "locador1", "phoneNumber": "926870150", "sex": "M", "photo": LOCADOR_1_PHOTO}
    locador_1_id = post(url=f"{base_url}/locadores", json=data).json()["id"]

    data = {"email": "locador2@mail.com", "firstName": "Diogo", "lastName": "Marques",
            "password": "locador2", "phoneNumber": "964501288", "sex": "M", "photo": LOCADOR_2_PHOTO}
    locador_2_id = post(url=f"{base_url}/locadores", json=data).json()["id"]

    data = {"email": "locador3@mail.com", "firstName": "Joana", "lastName": "Amaral",
            "password": "locador3", "phoneNumber": "931020653", "sex": "F", "photo": LOCADOR_3_PHOTO}
    locador_3_id = post(url=f"{base_url}/locadores", json=data).json()["id"]

    data = {"email": "locador4@mail.com", "firstName": "Joana", "lastName": "Silva",
            "password": "locador4", "phoneNumber": "966092333", "sex": "F", "photo": LOCADOR_4_PHOTO}
    locador_4_id = post(url=f"{base_url}/locadores", json=data).json()["id"]

    #######################################
    #            CREATE HOUSES            #
    #######################################
    house_photos = [HOUSE_1_PHOTO_1]
    data = {"street": "Rua de Norton Matos nº123", "city": "Aveiro", "postalCode": "3810-064", "noRooms": 2, "noBathrooms": 1, "noGarages": 0, "habitableArea": 70, "available": True, "price": 250,
            "name": "T2 em ótimo estado", "description": "Totalmente remodelado em 2018, em tipologia de T2, com Wi-Fi.", "propertyFeatures": "Wi-Fi;Washing machine;Phone;Warm water", "photos": house_photos, "locador": {"id": locador_1_id}}
    house_1_id = post(url=f"{base_url}/houses", json=data).json()["id"]

    house_photos = [HOUSE_2_PHOTO_1, HOUSE_2_PHOTO_2]
    data = {"street": "Urbanização Chave 9-5", "city": "Aveiro", "postalCode": "3810-081", "noRooms": 3, "noBathrooms": 2, "noGarages": 1, "habitableArea": 110, "available": True, "price": 375, "name": "T3 c/ Garagem",
            "description": "Mobília um pouco antiga, mas bem conservada, com acesso a garagem privada", "propertyFeatures": "Wi-Fi;Parking;Television;Washing machine;Alarm", "photos": house_photos, "locador": {"id": locador_1_id}}
    house_2_id = post(url=f"{base_url}/houses", json=data).json()["id"]

    house_photos = [HOUSE_3_PHOTO_1, HOUSE_3_PHOTO_2, HOUSE_3_PHOTO_3]
    data = {"street": "Urbanização Chave 9-5", "city": "Aveiro", "postalCode": "3810-081", "noRooms": 1, "noBathrooms": 1, "noGarages": 0, "habitableArea": 65, "available": True, "price": 195, "name": "T1 bem localizado",
            "description": "Mobília um pouco antiga, mas bem conservada. Muito bem localizado", "propertyFeatures": "Wi-Fi;Washing machine;Fire extinguisher;Balcony", "photos": house_photos, "locador": {"id": locador_3_id}}
    house_3_id = post(url=f"{base_url}/houses", json=data).json()["id"]

    house_photos = [HOUSE_4_PHOTO_1, HOUSE_4_PHOTO_2]
    data = {"street": "Rua Formosa", "city": "Viseu", "postalCode": "3500-135", "noRooms": 1, "noBathrooms": 1, "noGarages": 0, "habitableArea": 65, "available": True, "price": 180, "name": "T1 para rapariga",
            "description": "Apenas são permitidas raparigas", "propertyFeatures": "Wi-Fi;Washing machine;Vacuum cleaner;Air conditioning", "photos": house_photos, "locador": {"id": locador_4_id}}
    house_4_id = post(url=f"{base_url}/houses", json=data).json()["id"]

    #######################################
    #       ADD HOUSES TO WISHLIST        #
    #######################################
    data = {"locatarioId": locatario_2_id, "houseId": house_1_id}
    post(url=f"{base_url}/locatarios/wishlist", json=data)

    data = {"locatarioId": locatario_3_id, "houseId": house_4_id}
    post(url=f"{base_url}/locatarios/wishlist", json=data)

    data = {"locatarioId": locatario_3_id, "houseId": house_2_id}
    post(url=f"{base_url}/locatarios/wishlist", json=data)

    data = {"locatarioId": locatario_3_id, "houseId": house_3_id}
    post(url=f"{base_url}/locatarios/wishlist", json=data)

    #######################################
    #            CREATE RENTS             #
    #######################################
    data = {"locatarioEmail": "locatario3@mail.com", "houseId": house_4_id,
            "locadorId": locador_4_id, "startDate": "2020-04-01", "endDate": "2020-06-01", "price": 175}
    print(post(url=f"{base_url}/locadores/rent", json=data))

    data = {"locatarioEmail": "locatario3@mail.com", "houseId": house_3_id,
            "locadorId": locador_3_id, "startDate": "2019-01-01", "endDate": "2020-01-01", "price": 195}
    print(post(url=f"{base_url}/locadores/rent", json=data))

    data = {"locatarioEmail": "locatario2@mail.com", "houseId": house_1_id,
            "locadorId": locador_1_id, "startDate": "2018-06-01", "endDate": "2018-12-01", "price": 250}
    print(post(url=f"{base_url}/locadores/rent", json=data))

    #######################################
    #           CREATE REVIEWS            #
    #######################################
    data = {"houseId": house_2_id, "locatarioId": locatario_2_id,
            "comment": "Very nice!", "rating": 3.5}
    post(url=f"{base_url}/houses/reviews", json=data)


if __name__ == "__main__":
    parser = argparse.ArgumentParser(
        description="Reset and reload DOMUS database")

    parser.add_argument("-d", "--dev", dest="dev", action="store_true",
                        help="use local REST API to assist development (default=False)")

    args = parser.parse_args()

    dev = args.dev

    base_url = "http://0.0.0.0:8080/api" if dev else "http://192.168.160.60:8080/api"

    try:
        main(base_url)
    except ConnectionError:
        host, port = base_url.split("/")[2].split(":")
        sys.exit(
            f"Failed to establish a new connection with (host='{host}', port={port})")
