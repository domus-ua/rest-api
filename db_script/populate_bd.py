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

    house_photos = [HOUSE_5_PHOTO_1, HOUSE_5_PHOTO_2]
    data = {"street": "Campo 24 de Agosto", "city": "Porto", "postalCode": "4300-503", "noRooms": 4, "noBathrooms": 2, "noGarages": 0, "habitableArea": 130, "available": True, "price": 280, "name": "T4 c/ quarto para rapariga",
            "description": "Quarto extremamente confortável, localizado na Rua Santos Pousada, próximo da Rua Santa Catarina. Nas imediações poderá encontrar todo o tipo de comércio, transportes e serviços. Dista 500 metros estação de Metro do Campo 24 de Agosto e 20 metros de paragem de autocarro dos STCP.",
            "propertyFeatures": "Wi-Fi;Washing machine;Vacuum cleaner;Warm Water", "photos": house_photos, "locador": {"id": locador_3_id}}
    house_5_id = post(url=f"{base_url}/houses", json=data).json()["id"]

    house_photos = [HOUSE_6_PHOTO_1, HOUSE_6_PHOTO_2]
    data = {"street": "Campo 24 de Agosto", "city": "Porto", "postalCode": "4300-503", "noRooms": 3, "noBathrooms": 1, "noGarages": 0, "habitableArea": 120, "available": True, "price": 220, "name": "Quartos no centro do Porto",
            "description": "Quarto extremamente confortável.Equipado com microondas, placa, forno, cafeteira, frigorífico e torradeira.Não possui armário nos quartos, podendo o arrendatário providenciá-lo e levantar quando da saída.",
            "propertyFeatures": "Wi-Fi;Washing machine;Vacuum cleaner;Fire extinguisher", "photos": house_photos, "locador": {"id": locador_4_id}}
    house_6_id = post(url=f"{base_url}/houses", json=data).json()["id"]

    house_photos = [HOUSE_7_PHOTO_1, HOUSE_7_PHOTO_2]
    data = {"street": "Rua Heliodoro Salgado", "city": "Lisboa", "postalCode": "1170-174", "noRooms": 3, "noBathrooms": 2, "noGarages": 0, "habitableArea": 120, "available": True, "price": 300, "name": "Quartos em T3 Lisboa",
            "description": "Despesas incluídas. A 10 minutos a pé do metro dos Anjos, encontrará este apartamento totalmente remodelado e que conta com um ambiente internacional bastante descontraído.",
            "propertyFeatures": "Wi-Fi;Vacuum cleaner;Fire extinguisher", "photos": house_photos, "locador": {"id": locador_2_id}}
    house_7_id = post(url=f"{base_url}/houses", json=data).json()["id"]

    house_photos = [HOUSE_8_PHOTO_1, HOUSE_8_PHOTO_2]
    data = {"street": "Rua Heliodoro Salgado", "city": "Lisboa", "postalCode": "1170-174", "noRooms": 4, "noBathrooms": 2, "noGarages": 1, "habitableArea": 150, "available": True, "price": 320, "name": "Quartos em T4 Lisboa",
            "description": "Desejavelmente estudantes ou quadros superiores.Um mês de renda e um mês de caução.Não aceitamos animaisNão fumadoresPrivilegiamos contratos de média ou longa duração.",
            "propertyFeatures": "Wi-Fi;Vacuum cleaner;Washing machine;Balcony", "photos": house_photos, "locador": {"id": locador_2_id}}
    house_8_id = post(url=f"{base_url}/houses", json=data).json()["id"]

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

    data = {"locatarioEmail": "locatario3@mail.com", "houseId": house_8_id,
            "locadorId": locador_2_id, "startDate": "2018-01-01", "endDate": "2018-12-01", "price": 320}
    print(post(url=f"{base_url}/locadores/rent", json=data))

    data = {"locatarioEmail": "locatario3@mail.com", "houseId": house_7_id,
            "locadorId": locador_2_id, "startDate": "2017-01-01", "endDate": "2017-10-01", "price": 300}
    print(post(url=f"{base_url}/locadores/rent", json=data))

    data = {"locatarioEmail": "locatario3@mail.com", "houseId": house_5_id,
            "locadorId": locador_3_id, "startDate": "2016-01-01", "endDate": "2016-12-01", "price": 280}
    print(post(url=f"{base_url}/locadores/rent", json=data))

    data = {"locatarioEmail": "locatario2@mail.com", "houseId": house_1_id,
            "locadorId": locador_1_id, "startDate": "2018-06-01", "endDate": "2018-12-01", "price": 250}
    print(post(url=f"{base_url}/locadores/rent", json=data))

    data = {"locatarioEmail": "locatario2@mail.com", "houseId": house_2_id,
            "locadorId": locador_1_id, "startDate": "2017-06-01", "endDate": "2017-12-01", "price": 375}
    print(post(url=f"{base_url}/locadores/rent", json=data))

    data = {"locatarioEmail": "locatario2@mail.com", "houseId": house_7_id,
            "locadorId": locador_2_id, "startDate": "2019-06-01", "endDate": "2019-12-01", "price": 300}
    print(post(url=f"{base_url}/locadores/rent", json=data))

    data = {"locatarioEmail": "locatario2@mail.com", "houseId": house_8_id,
            "locadorId": locador_2_id, "startDate": "2020-01-01", "endDate": "2020-12-01", "price": 320}
    print(post(url=f"{base_url}/locadores/rent", json=data))

    data = {"locatarioEmail": "locatario1@mail.com", "houseId": house_2_id,
            "locadorId": locador_1_id, "startDate": "2018-08-01", "endDate": "2018-12-22", "price": 375}
    print(post(url=f"{base_url}/locadores/rent", json=data))

    data = {"locatarioEmail": "locatario1@mail.com", "houseId": house_5_id,
            "locadorId": locador_3_id, "startDate": "2019-03-01", "endDate": "2019-12-01", "price": 280}
    print(post(url=f"{base_url}/locadores/rent", json=data))

    data = {"locatarioEmail": "locatario1@mail.com", "houseId": house_6_id,
            "locadorId": locador_4_id, "startDate": "2020-01-01", "endDate": "2020-12-01", "price": 220}
    print(post(url=f"{base_url}/locadores/rent", json=data))

    

    #######################################
    #           CREATE REVIEWS            #
    #######################################
    data = {"houseId": house_2_id, "locatarioId": locatario_2_id,
            "comment": "Very nice!", "rating": 3.5}
    post(url=f"{base_url}/houses/reviews", json=data)

    data = {"houseId": house_2_id, "locatarioId": locatario_1_id,
            "comment": "I had an amazing stay!", "rating": 4.5}
    post(url=f"{base_url}/houses/reviews", json=data)

    data = {"houseId": house_5_id, "locatarioId": locatario_1_id,
            "comment": "The house is nice, but the street is very noisy!", "rating": 3.5}
    post(url=f"{base_url}/houses/reviews", json=data)

    data = {"houseId": house_8_id, "locatarioId": locatario_2_id,
            "comment": "Excellent house with an amazing owner.", "rating": 5}
    post(url=f"{base_url}/houses/reviews", json=data)

    data = {"houseId": house_7_id, "locatarioId": locatario_2_id,
            "comment": "The bathroom was always dirty and the windows didn't close.", "rating": 2.5}
    post(url=f"{base_url}/houses/reviews", json=data)

    data = {"houseId": house_3_id, "locatarioId": locatario_3_id,
            "comment": "The house is nice, but some walls need to be repainted!", "rating": 4}
    post(url=f"{base_url}/houses/reviews", json=data)

    data = {"houseId": house_8_id, "locatarioId": locatario_3_id,
            "comment": "The bedroom was nice and the location of the house is very good!", "rating": 4.5}
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
