import java.util.*;

public class WorldMap {
    private static HashMap<Location, List<Location>> MAP;

    static {
        MAP = new HashMap<>();
        MAP.put(Location.ALGIERS, Arrays.asList(Location.CAIRO, Location.ISTANBUL, Location.MADRID, Location.PARIS));
        MAP.put(Location.ATLANTA, Arrays.asList(Location.CHICAGO, Location.MIAMI, Location.WASHINGTON));
        MAP.put(Location.BAGHDAD, Arrays.asList(Location.CAIRO, Location.ISTANBUL, Location.KARACHI, Location.RIYADH, Location.TEHRAN));
        MAP.put(Location.BANGKOK, Arrays.asList(Location.CHENNAI, Location.HO_CHI_MINH_CITY, Location.HONG_KONG, Location.JAKARTA, Location.KOLKATA));
        MAP.put(Location.BEIJING, Arrays.asList(Location.SEOUL, Location.SHANGHAI));
        MAP.put(Location.BOGOTA, Arrays.asList(Location.BUENOS_AIRES, Location.LIMA, Location.MEXICO_CITY, Location.MIAMI, Location.SAO_PAULO));
        MAP.put(Location.BUENOS_AIRES, Arrays.asList(Location.BOGOTA, Location.SAO_PAULO));
        MAP.put(Location.CAIRO, Arrays.asList(Location.ALGIERS, Location.BAGHDAD, Location.ISTANBUL, Location.KHARTOUM, Location.RIYADH));
        MAP.put(Location.CHENNAI, Arrays.asList(Location.BANGKOK, Location.DELHI, Location.JAKARTA, Location.KOLKATA, Location.MUMBAI));
        MAP.put(Location.CHICAGO, Arrays.asList(Location.ATLANTA, Location.LOS_ANGELES, Location.MEXICO_CITY, Location.MONTREAL, Location.SAN_FRANCISCO));
        MAP.put(Location.DELHI, Arrays.asList(Location.CHENNAI, Location.KARACHI, Location.KOLKATA, Location.MUMBAI, Location.TEHRAN));
        MAP.put(Location.ESSEN, Arrays.asList(Location.LONDON, Location.MILAN, Location.PARIS, Location.ST_PETERSBURG));
        MAP.put(Location.HO_CHI_MINH_CITY, Arrays.asList(Location.BANGKOK, Location.HONG_KONG, Location.JAKARTA, Location.MANILA));
        MAP.put(Location.HONG_KONG, Arrays.asList(Location.BANGKOK, Location.HO_CHI_MINH_CITY, Location.KOLKATA, Location.MANILA, Location.SHANGHAI, Location.TAIPEI));
        MAP.put(Location.ISTANBUL, Arrays.asList(Location.ALGIERS, Location.BAGHDAD, Location.CAIRO, Location.MILAN, Location.MOSCOW, Location.ST_PETERSBURG));
        MAP.put(Location.JAKARTA, Arrays.asList(Location.BANGKOK, Location.CHENNAI, Location.HO_CHI_MINH_CITY, Location.SYDNEY));
        MAP.put(Location.JOHANNESBURG, Arrays.asList(Location.KHARTOUM, Location.KINSHASA));
        MAP.put(Location.KARACHI, Arrays.asList(Location.BAGHDAD, Location.DELHI, Location.MUMBAI, Location.RIYADH, Location.TEHRAN));
        MAP.put(Location.KHARTOUM, Arrays.asList(Location.CAIRO, Location.JOHANNESBURG, Location.KINSHASA, Location.LAGOS));
        MAP.put(Location.KINSHASA, Arrays.asList(Location.JOHANNESBURG, Location.KHARTOUM, Location.LAGOS));
        MAP.put(Location.KOLKATA, Arrays.asList(Location.BANGKOK, Location.CHENNAI, Location.DELHI, Location.HONG_KONG));
        MAP.put(Location.LAGOS, Arrays.asList(Location.KHARTOUM, Location.KINSHASA, Location.SAO_PAULO));
        MAP.put(Location.LIMA, Arrays.asList(Location.BOGOTA, Location.MEXICO_CITY, Location.SANTIAGO));
        MAP.put(Location.LONDON, Arrays.asList(Location.ESSEN, Location.MADRID, Location.NEW_YORK, Location.PARIS));
        MAP.put(Location.LOS_ANGELES, Arrays.asList(Location.CHICAGO, Location.MEXICO_CITY, Location.SAN_FRANCISCO, Location.SYDNEY));
        MAP.put(Location.MADRID, Arrays.asList(Location.ALGIERS, Location.LONDON, Location.NEW_YORK, Location.PARIS, Location.SAO_PAULO));
        MAP.put(Location.MANILA, Arrays.asList(Location.HONG_KONG, Location.HO_CHI_MINH_CITY, Location.SAN_FRANCISCO, Location.SYDNEY, Location.TAIPEI));
        MAP.put(Location.MEXICO_CITY, Arrays.asList(Location.BOGOTA, Location.CHICAGO, Location.LIMA, Location.LOS_ANGELES, Location.MIAMI));
        MAP.put(Location.MIAMI, Arrays.asList(Location.ATLANTA, Location.BOGOTA, Location.MEXICO_CITY, Location.WASHINGTON));
        MAP.put(Location.MILAN, Arrays.asList(Location.ESSEN, Location.ISTANBUL, Location.PARIS));
        MAP.put(Location.MONTREAL, Arrays.asList(Location.CHICAGO, Location.NEW_YORK, Location.WASHINGTON));
        MAP.put(Location.MOSCOW, Arrays.asList(Location.ISTANBUL, Location.ST_PETERSBURG, Location.TEHRAN));
        MAP.put(Location.MUMBAI, Arrays.asList(Location.CHENNAI, Location.DELHI, Location.KARACHI));
        MAP.put(Location.NEW_YORK, Arrays.asList(Location.LONDON, Location.MADRID, Location.MONTREAL, Location.WASHINGTON));
        MAP.put(Location.OSAKA, Arrays.asList(Location.TAIPEI, Location.TOKYO));
        MAP.put(Location.PARIS, Arrays.asList(Location.ALGIERS, Location.ESSEN, Location.LONDON, Location.MADRID, Location.MILAN));
        MAP.put(Location.RIYADH, Arrays.asList(Location.BAGHDAD, Location.CAIRO, Location.KARACHI));
        MAP.put(Location.SAN_FRANCISCO, Arrays.asList(Location.CHICAGO, Location.LOS_ANGELES, Location.MANILA, Location.TOKYO));
        MAP.put(Location.SANTIAGO, Arrays.asList(Location.LIMA));
        MAP.put(Location.SAO_PAULO, Arrays.asList(Location.BOGOTA, Location.BUENOS_AIRES, Location.LAGOS, Location.MADRID));
        MAP.put(Location.SEOUL, Arrays.asList(Location.BEIJING, Location.SHANGHAI, Location.TOKYO));
        MAP.put(Location.SHANGHAI, Arrays.asList(Location.BEIJING, Location.HONG_KONG, Location.SEOUL, Location.TAIPEI, Location.TOKYO));
        MAP.put(Location.ST_PETERSBURG, Arrays.asList(Location.ESSEN, Location.ISTANBUL, Location.MOSCOW));
        MAP.put(Location.SYDNEY, Arrays.asList(Location.JAKARTA, Location.LOS_ANGELES, Location.MANILA));
        MAP.put(Location.TAIPEI, Arrays.asList(Location.HONG_KONG, Location.MANILA, Location.OSAKA, Location.SHANGHAI));
        MAP.put(Location.TEHRAN, Arrays.asList(Location.BAGHDAD, Location.DELHI, Location.KARACHI, Location.MOSCOW));
        MAP.put(Location.TOKYO, Arrays.asList(Location.OSAKA, Location.SAN_FRANCISCO, Location.SEOUL, Location.SHANGHAI));
        MAP.put(Location.WASHINGTON, Arrays.asList(Location.ATLANTA, Location.MIAMI, Location.MONTREAL, Location.NEW_YORK));
    }

    private static boolean undirectedGraphCheck() {
        for (Map.Entry<Location, List<Location>> entry : MAP.entrySet()) {
            // Get city and list of neighbors of that city
            Location city = entry.getKey();
            List<Location> neighbors = entry.getValue();

            // For each neighbor, make sure original city is in neighbor's list of neighbors
            for (Location neighbor : neighbors) {
                if (!MAP.get(neighbor).contains(city)) {
                    System.err.println(city.getNameOfCity() + " " + neighbor.getNameOfCity());
                    return false;
                }
            }
        }

        // If all edges are symmetric, return true
        return true;
    }

    public static Set<Location> getCitySet() {
        return MAP.keySet();
    }

    public static List<Location> getNeighbors(Location city) {
        return MAP.get(city);
    }
}
