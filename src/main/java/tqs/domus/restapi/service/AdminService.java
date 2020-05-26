package tqs.domus.restapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.domus.restapi.model.House;
import tqs.domus.restapi.model.HouseDTO;
import tqs.domus.restapi.model.Locador;
import tqs.domus.restapi.model.LocadorDTO;
import tqs.domus.restapi.model.Locatario;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static tqs.domus.restapi.Constants.HOUSE_1_PHOTO_1;
import static tqs.domus.restapi.Constants.HOUSE_2_PHOTO_1;
import static tqs.domus.restapi.Constants.HOUSE_2_PHOTO_2;
import static tqs.domus.restapi.Constants.LOCADOR_1_PHOTO;
import static tqs.domus.restapi.Constants.LOCADOR_2_PHOTO;
import static tqs.domus.restapi.Constants.LOCADOR_3_PHOTO;
import static tqs.domus.restapi.Constants.LOCADOR_4_PHOTO;
import static tqs.domus.restapi.Constants.LOCATARIO_1_PHOTO;
import static tqs.domus.restapi.Constants.LOCATARIO_2_PHOTO;
import static tqs.domus.restapi.Constants.LOCATARIO_3_PHOTO;

/**
 * @author Vasco Ramos
 * @date 26/mai/2020
 * @time 10:50
 */

@Service
public class AdminService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LocatarioService locatarioService;

	@Autowired
	private LocadorService locadorService;

	@Autowired
	private HouseService houseService;

	public boolean reloadDatabse() {
		return clearDatabase() && populateDatabase();
	}

	private boolean clearDatabase() {
		try {
			userRepository.deleteAll();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	private boolean populateDatabase() {
		try {
			/*
			 * CREATE LOCATARIOS
			 */
			UserDTO userDTO1 = new UserDTO("locatario1@mail.com", "Rafael", "Gomes", "locatario1", "934125484", "M",
					LOCATARIO_1_PHOTO);
			Locatario locatario1 = locatarioService.registerLocatario(userDTO1);

			UserDTO userDTO2 = new UserDTO("locatario2@mail.com", "José", "Amaral", "locatario2", "910294284", "M",
					LOCATARIO_2_PHOTO);
			Locatario locatario2 = locatarioService.registerLocatario(userDTO2);

			UserDTO userDTO3 = new UserDTO("locatario3@mail.com", "Diana", "Fonseca", "locatario3", "969230471", "F",
					LOCATARIO_3_PHOTO);
			Locatario locatario3 = locatarioService.registerLocatario(userDTO3);

			/*
			 * CREATE LOCADORES
			 */
			UserDTO userDTO4 = new UserDTO("locador1@mail.com", "Carlos", "Santos", "locador1", "926870150", "M",
					LOCADOR_1_PHOTO);
			Locador locador1 = locadorService.registerLocador(userDTO4);
			LocadorDTO locadorDTO1 = new LocadorDTO(locador1.getId());

			UserDTO userDTO5 = new UserDTO("locador2@mail.com", "Diogo", "Marques", "locador2", "964501288", "M",
					LOCADOR_2_PHOTO);
			Locador locador2 = locadorService.registerLocador(userDTO5);
			LocadorDTO locadorDTO2 = new LocadorDTO(locador2.getId());

			UserDTO userDTO6 = new UserDTO("locador3@mail.com", "Diana", "Fonseca", "locador3", "911030183", "F",
					LOCADOR_3_PHOTO);
			Locador locador3 = locadorService.registerLocador(userDTO6);
			LocadorDTO locadorDTO3 = new LocadorDTO(locador2.getId());

			UserDTO userDTO7 = new UserDTO("locador4@mail.com", "Joana", "Silva", "locador4", "966092333", "F",
					LOCADOR_4_PHOTO);
			Locador locador4 = locadorService.registerLocador(userDTO7);
			LocadorDTO locadorDTO4 = new LocadorDTO(locador2.getId());

			/*
			 * CREATE HOUSES
			 */
			List<String> housePhotos = new ArrayList<>() {{
				add(HOUSE_1_PHOTO_1);
			}};
			HouseDTO houseDTO1 = new HouseDTO("Rua de Norton Matos nº123", "Aveiro, Portugal", "3810-064", 2, 1, 0, 70, true, 250,
					"T2 em ótimo estado", "Totalmente remodelado em 2018, em tipologia de T2, com WI-FI.", "Internet;Secador;Máquina de Lavar Loiça", housePhotos, locadorDTO1);
			House house1 = houseService.registerHouse(houseDTO1);

			housePhotos = new ArrayList<>() {{
				add(HOUSE_2_PHOTO_1);
				add(HOUSE_2_PHOTO_2);
			}};
			HouseDTO houseDTO2 = new HouseDTO("Urbanização Chave 9-5", "Aveiro, Portugal", "3810-081", 3, 2, 1, 110, true, 375,
					"T3 c/ Garagem", "Mobília um pouco antiga, mas bem conservada, com acesso a garagem privada", "Internet;Garagem;TV;Máquina de Lavar Loiça", housePhotos, locadorDTO1);
			House house2 = houseService.registerHouse(houseDTO2);

			/*
			 * ADD HOUSES TO WISHLIST
			 */

			/*
			 * CREATE RENTS
			 */

			/*
			 * CREATE REVIEWS
			 */

			return true;
		} catch (Exception ex) {
			return false;
		}
	}
}
