package org.mastercard.city.routes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class CityRoutes {

	private static List<String> verifiedCities = new ArrayList<>();

	public static final String YES = "yes";
	public static final String NO = "no";

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter origin city:");

		String originCity = scanner.nextLine();
		System.out.println("Enter destination city");
		String destinationCity = scanner.nextLine();

		System.out.println("Cities connected or not:" + connectedOrNot(originCity, destinationCity));
	}

	public static Map<String, Set<String>> buildCityTOCityRoutesMap() {
		Map<String, Set<String>> routes = new HashMap<>();
		BufferedReader br = new BufferedReader(new InputStreamReader(CityRoutes.class.getResourceAsStream("city.txt")));
		try {
			if (br.ready()) {
				String line;
				while ((line = br.readLine()) != null) {
					if (!line.trim().equals("")) {
						String cities[] = line.split(",");
						if (cities.length == 2) {
							String city1 = cities[0].trim();
							String city2 = cities[1].trim();
							addCityToMap(routes, city1, city2);
							addCityToMap(routes, city2, city1);
						}
					}
				}
			}
		} catch (Exception e) {

		} finally {
			try {
				br.close();
			} catch (IOException e) {
				
			}
		}
		return routes;
	}

	private static void addCityToMap(Map<String, Set<String>> routes, String cityKey, String cityRoute) {
		if (!routes.containsKey(cityKey)) {
			Set<String> cityRouteList = new HashSet<>();
			cityRouteList.add(cityRoute);
			routes.put(cityKey, cityRouteList);
		} else {
			routes.get(cityKey).add(cityRoute);
		}
	}

	public static String connectedOrNot(String origin, String destination) {
		String result;
		if (origin.equals(destination)) {
			result = YES;
		} else {
			Map<String, Set<String>> routes = null;
			try {
				routes = buildCityTOCityRoutesMap();
			} catch (Exception e) {
				result = NO; 
			}
			if (isRoutePresent(origin, destination, routes)) {
				result = YES;
			} else {
				result = NO;
			}
		}
		return result;
	}

	private static boolean isRoutePresent(String city1, String city2, Map<String, Set<String>> routes) {
		if (!routes.containsKey(city1) || !routes.containsKey(city2)) {
			return false;
		}
		if (routes.get(city1).contains(city2))
			return true;
		else {
			verifiedCities.add(city1);
			for (String cityConnected : routes.get(city1)) {
				if (!verifiedCities.contains(cityConnected) && isRoutePresent(cityConnected, city2, routes))
					return true;
			}
		}
		return false;
	}
}
