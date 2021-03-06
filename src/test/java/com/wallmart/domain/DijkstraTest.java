package com.wallmart.domain;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class DijkstraTest {

	private final Route ROUTE_SAOPAULO_LIMEIRA = new Route();

	private final Route ROUTE_LIMEIRA_AMERICANA = new Route();

	private final Route ROUTE_SAOPAULO_CAMPINAS = new Route();

	private final Route ROUTE_CAMPINAS_AMERICANA = new Route();

	private final Route ROUTE_LIMEIRA_PIRACICABA = new Route();

	private final Route ROUTE_PIRACICABA_SAOPAULO = new Route();

	private final Route ROUTE_AMERICANA_SUMARE = new Route();

	private final Set<Route> ROUTES_SAOPAULO_LIMEIRA_AMERICANA = new HashSet<Route>(
	    Arrays.asList(ROUTE_SAOPAULO_LIMEIRA, ROUTE_LIMEIRA_AMERICANA));

	private final Set<Route> ROUTES_SAOPAULO_LIMEIRA_AMERICANA_WITH_CYCLE = new HashSet<Route>(
	    Arrays
	        .asList(ROUTE_SAOPAULO_LIMEIRA, ROUTE_LIMEIRA_PIRACICABA, ROUTE_PIRACICABA_SAOPAULO, ROUTE_LIMEIRA_AMERICANA, ROUTE_SAOPAULO_CAMPINAS, ROUTE_CAMPINAS_AMERICANA));

	private final Set<Route> ROUTES_SAOPAULO_LIMEIRA_AMERICANA_SAOPAULO_CAMPINAS_CAMPINAS_AMERICANA = new HashSet<Route>(
	    Arrays
	        .asList(ROUTE_SAOPAULO_LIMEIRA, ROUTE_LIMEIRA_AMERICANA, ROUTE_SAOPAULO_CAMPINAS, ROUTE_CAMPINAS_AMERICANA));

	private final Set<Route> ROUTES_SAOPAULO_LIMEIRA_AMERICANA_SAOPAULO_CAMPINAS_CAMPINAS_AMERICANA_AMERICANA_SUMARE = new HashSet<Route>(
	    Arrays
	        .asList(ROUTE_SAOPAULO_LIMEIRA, ROUTE_LIMEIRA_AMERICANA, ROUTE_SAOPAULO_CAMPINAS, ROUTE_CAMPINAS_AMERICANA, ROUTE_AMERICANA_SUMARE));

	private final Route ROUTE_A_B = new Route();
	private final Route ROUTE_B_D = new Route();
	private final Route ROUTE_A_C = new Route();
	private final Route ROUTE_C_D = new Route();
	private final Route ROUTE_B_E = new Route();
	private final Route ROUTE_D_E = new Route();

	private final Set<Route> ROUTES_EXAMPLE = new HashSet<Route>(Arrays
	    .asList(ROUTE_A_B, ROUTE_B_D, ROUTE_A_C, ROUTE_C_D, ROUTE_B_E, ROUTE_D_E));

	private final Set<Route> ROUTES_WITH_DUPLICATES = new HashSet<Route>(Arrays
	    .asList(ROUTE_A_B, ROUTE_B_E, ROUTE_B_D, ROUTE_B_D, ROUTE_A_C, ROUTE_B_E, ROUTE_C_D, ROUTE_B_E, ROUTE_D_E));

	@Before
	public void tearUp() {

		ROUTE_SAOPAULO_LIMEIRA.setOrigin("São Paulo");
		ROUTE_SAOPAULO_LIMEIRA.setDestination("Limeira");
		ROUTE_SAOPAULO_LIMEIRA.setDistance(150f);

		ROUTE_LIMEIRA_AMERICANA.setOrigin("Limeira");
		ROUTE_LIMEIRA_AMERICANA.setDestination("Americana");
		ROUTE_LIMEIRA_AMERICANA.setDistance(30f);

		ROUTE_SAOPAULO_CAMPINAS.setOrigin("São Paulo");
		ROUTE_SAOPAULO_CAMPINAS.setDestination("Campinas");
		ROUTE_SAOPAULO_CAMPINAS.setDistance(90.5f);

		ROUTE_CAMPINAS_AMERICANA.setOrigin("Campinas");
		ROUTE_CAMPINAS_AMERICANA.setDestination("Americana");
		ROUTE_CAMPINAS_AMERICANA.setDistance(20f);

		ROUTE_LIMEIRA_PIRACICABA.setOrigin("Limeira");
		ROUTE_LIMEIRA_PIRACICABA.setDestination("Piracicaba");
		ROUTE_LIMEIRA_PIRACICABA.setDistance(20f);

		ROUTE_PIRACICABA_SAOPAULO.setOrigin("Piracicaba");
		ROUTE_PIRACICABA_SAOPAULO.setDestination("São Paulo");
		ROUTE_PIRACICABA_SAOPAULO.setDistance(120f);

		ROUTE_AMERICANA_SUMARE.setOrigin("Americana");
		ROUTE_AMERICANA_SUMARE.setDestination("Sumaré");
		ROUTE_AMERICANA_SUMARE.setDistance(10f);

		ROUTE_A_B.setOrigin("A");
		ROUTE_A_B.setDestination("B");
		ROUTE_A_B.setDistance(10f);

		ROUTE_B_D.setOrigin("B");
		ROUTE_B_D.setDestination("D");
		ROUTE_B_D.setDistance(15f);

		ROUTE_A_C.setOrigin("A");
		ROUTE_A_C.setDestination("C");
		ROUTE_A_C.setDistance(20f);

		ROUTE_C_D.setOrigin("C");
		ROUTE_C_D.setDestination("D");
		ROUTE_C_D.setDistance(30f);

		ROUTE_B_E.setOrigin("B");
		ROUTE_B_E.setDestination("E");
		ROUTE_B_E.setDistance(50f);

		ROUTE_D_E.setOrigin("D");
		ROUTE_D_E.setDestination("E");
		ROUTE_D_E.setDistance(30f);
	}

	@Test
	public void testDijkstraInitialize() {

		Dijkstra calculator = Dijkstra.initialize(ROUTES_SAOPAULO_LIMEIRA_AMERICANA);

		assertEquals(calculator.unburned.size(), 3);
		assertEquals(calculator.predecessorOf.size(), 3);
		assertEquals(calculator.distanceFromOrigin.size(), 3);
		assertEquals(calculator.neighbors.size(), 2);

		assertArrayEquals(calculator.unburned.toArray(), new String[] { "Americana", "Limeira", "São Paulo" });

		assertNull(calculator.predecessorOf.get("Americana"));
		assertNull(calculator.predecessorOf.get("Limeira"));
		assertNull(calculator.predecessorOf.get("São Paulo"));

		assertEquals(calculator.distanceFromOrigin.get("Americana"), Float.MAX_VALUE, 0.0001);
		assertEquals(calculator.distanceFromOrigin.get("Limeira"), Float.MAX_VALUE, 0.0001);
		assertEquals(calculator.distanceFromOrigin.get("São Paulo"), Float.MAX_VALUE, 0.0001);

		assertArrayEquals(calculator.neighbors.keySet().toArray(), new String[] { "Limeira", "São Paulo" });
		assertArrayEquals(calculator.neighbors.get("São Paulo").toArray(), new Route[] { ROUTE_SAOPAULO_LIMEIRA });
		assertArrayEquals(calculator.neighbors.get("Limeira").toArray(), new Route[] { ROUTE_LIMEIRA_AMERICANA });

	}

	@Test
	public void testGetUnburnedCityWithTheSmallestDistanceFromOriginOnStart() {

		Dijkstra calculator = Dijkstra.initialize(ROUTES_SAOPAULO_LIMEIRA_AMERICANA);

		calculator.distanceFromOrigin.put("São Paulo", 0f);

		String city = calculator.getUnburnedCityWithTheSmallestDistanceFromOrigin();

		assertEquals(city, "São Paulo");
	}

	@Test
	public void testGetUnburnedNeighborsOnStart() {

		Dijkstra calculator = Dijkstra.initialize(ROUTES_SAOPAULO_LIMEIRA_AMERICANA);

		Set<Route> unburnedNeighbors = calculator.getUnburnedNeighbors("São Paulo");

		assertArrayEquals(unburnedNeighbors.toArray(), new Route[] { ROUTE_SAOPAULO_LIMEIRA });
	}

	@Test
	public void testGetUnburnedCityWithTheSmallestDistanceFromOriginOnRunning() {

		Dijkstra calculator = Dijkstra.initialize(ROUTES_SAOPAULO_LIMEIRA_AMERICANA);

		calculator.unburned.remove("São Paulo");

		calculator.distanceFromOrigin.put("São Paulo", 0f);
		calculator.distanceFromOrigin.put("Americana", 180f);
		calculator.distanceFromOrigin.put("Limeira", 150f);

		String city = calculator.getUnburnedCityWithTheSmallestDistanceFromOrigin();

		assertEquals(city, "Limeira");
	}

	@Test(expected = NoSuchElementException.class)
	public void testCalculateShortestPathWithOriginNotExistent() {

		Dijkstra calculator = Dijkstra.initialize(ROUTES_SAOPAULO_LIMEIRA_AMERICANA);

		calculator.calculateShortestPath("Araraquara", "Americana");

	}

	@Test(expected = NoSuchElementException.class)
	public void testCalculateShortestPathWithDestinationNotExistent() {

		Dijkstra calculator = Dijkstra.initialize(ROUTES_SAOPAULO_LIMEIRA_AMERICANA);

		calculator.calculateShortestPath("São Paulo", "Araraquara");

	}

	@Test(expected = IllegalArgumentException.class)
	public void testCalculateShortestPathWithoutValidPath() {

		Dijkstra calculator = Dijkstra.initialize(ROUTES_SAOPAULO_LIMEIRA_AMERICANA);

		calculator.calculateShortestPath("Americana", "São Paulo");
	}

	@Test
	public void testCalculateShortestPathWithOneRouteMap() {

		HashSet<Route> route = new HashSet<Route>();
		route.add(ROUTE_LIMEIRA_AMERICANA);

		Dijkstra calculator = Dijkstra.initialize(route);

		DijkstraResult result = calculator.calculateShortestPath("Limeira", "Americana");

		assertEquals(result.getDistance(), 30f, 0.0001);
		assertArrayEquals(result.getPath(), new String[] { "Limeira", "Americana" });

	}

	@Test
	public void testCalculateShortestPathWithTwoRoutesMap() {

		Dijkstra calculator = Dijkstra.initialize(ROUTES_SAOPAULO_LIMEIRA_AMERICANA);

		DijkstraResult result = calculator.calculateShortestPath("São Paulo", "Americana");

		assertEquals(result.getDistance(), 180f, 0.0001);
		assertArrayEquals(result.getPath(), new String[] { "São Paulo", "Limeira", "Americana" });

	}

	@Test
	public void testCalculateShortestPathWithTwoPossibleRoutes() {

		Dijkstra calculator = Dijkstra
		    .initialize(ROUTES_SAOPAULO_LIMEIRA_AMERICANA_SAOPAULO_CAMPINAS_CAMPINAS_AMERICANA);

		DijkstraResult result = calculator.calculateShortestPath("São Paulo", "Americana");

		assertEquals(result.getDistance(), 110.5f, 0.0001);
		assertArrayEquals(result.getPath(), new String[] { "São Paulo", "Campinas", "Americana" });

	}

	@Test
	public void testCalculateShortestPathWithCycleRoutes() {

		Dijkstra calculator = Dijkstra
		    .initialize(ROUTES_SAOPAULO_LIMEIRA_AMERICANA_WITH_CYCLE);

		DijkstraResult result = calculator.calculateShortestPath("São Paulo", "Americana");

		assertEquals(result.getDistance(), 110.5f, 0.0001);
		assertArrayEquals(result.getPath(), new String[] { "São Paulo", "Campinas", "Americana" });

	}

	@Test
	public void testCalculateShortestPathWithTwoPossibleRoutesAndRoutesAfterDestination() {

		Dijkstra calculator = Dijkstra
		    .initialize(new HashSet<Route>(
		        ROUTES_SAOPAULO_LIMEIRA_AMERICANA_SAOPAULO_CAMPINAS_CAMPINAS_AMERICANA_AMERICANA_SUMARE));

		DijkstraResult result = calculator.calculateShortestPath("São Paulo", "Americana");

		assertEquals(result.getDistance(), 110.5f, 0.0001);
		assertArrayEquals(result.getPath(), new String[] { "São Paulo", "Campinas", "Americana" });

	}

	@Test
	public void testCalculateShortestPathExample() {

		Dijkstra calculator = Dijkstra.initialize(ROUTES_EXAMPLE);

		DijkstraResult result = calculator.calculateShortestPath("A", "D");

		assertEquals(result.getDistance(), 25f, 0.0001);
		assertArrayEquals(result.getPath(), new String[] { "A", "B", "D" });

	}

	@Test
	public void testCalculateShortestPathOfRoutesWithDuplicates() {

		Dijkstra calculator = Dijkstra.initialize(ROUTES_WITH_DUPLICATES);

		DijkstraResult result = calculator.calculateShortestPath("A", "D");

		assertEquals(result.getDistance(), 25f, 0.0001);
		assertArrayEquals(result.getPath(), new String[] { "A", "B", "D" });

	}
}
