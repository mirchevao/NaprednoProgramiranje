import com.sun.xml.internal.ws.api.ha.StickyFeature;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}
class Team {
    String name;
     int wins;
     int loses;
     int draws;
     int scoredGoals;
     int acceptedGoals;
     int matchesPlayed;
    public Team(String name) {
        this.name = name;
        wins=0;
        loses=0;
        draws=0;
        scoredGoals=0;
        acceptedGoals=0;
        matchesPlayed=0;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name=name;
    }
    public int getWins() {
        return wins;
    }
    public void setWins(int wins) {
        this.wins=wins;
    }
    public int getLoses() {
        return loses;
    }
    public void setLoses(int loses) {
        this.loses=loses;
    }
    public int getDraws() {
        return draws;
    }
    public void setDraws(int draws) {
        this.draws = draws;
    }
    public int getScoredGoals()  {
        return scoredGoals;
    }
    public void setScoredGoals(int scoredGoals) {
        this.scoredGoals=scoredGoals;
    }
    public int getAcceptedGoals()  {
        return acceptedGoals;
    }
    public void setAcceptedGoals(int acceptedGoals) {
        this.acceptedGoals=acceptedGoals;
    }
    public int getMatchesPlayed() {
        return matchesPlayed;
    }
    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed=matchesPlayed;
    }
    public int addPoints() {
        return wins*3 + draws *1;
    }
    public void addGoals(int homeGoals) {
        scoredGoals+=homeGoals;
    }
    public void addAcceptedGoals(int goals) {
        acceptedGoals+=goals;
    }
    public int goalDifference() {
        return scoredGoals-acceptedGoals;
    }
    @Override
    public String toString() {
        return String.format("%-15s%5d%5d%5d%5d%5d", name, getMatchesPlayed(), wins,draws,loses,addPoints());
    }



}
class FootballTable {
    private Map<String, Team> teamMap;
    public FootballTable(Map<String, Team> teamMap) {
        teamMap = new HashMap<>();
    }

    public FootballTable() {

    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
        Team home = null;
        for(String team : teamMap.keySet()) {
            if(team.equals(homeTeam)) {
                home = teamMap.get(team);
                break;
            }
        }
        if(home == null) {
            home = new Team(homeTeam);
            teamMap.put(homeTeam,home);
        }
        Team away = teamMap.computeIfAbsent(awayTeam, key-> new Team(awayTeam));
        home.scoredGoals+=homeGoals;
        home.acceptedGoals+=awayGoals;
        away.scoredGoals+=awayGoals;
        away.acceptedGoals+=homeGoals;
        home.matchesPlayed++;
        away.matchesPlayed++;

        if(homeGoals > awayGoals) {
            home.wins++;
            away.loses++;
        } else if(homeGoals < awayGoals) {
            home.loses++;
            away.wins++;
        } else {
            home.draws++;
            away.draws++;
        }
    }
    public void printTable() {
        List<Team> teamList = teamMap.values().stream().sorted(Comparator.comparing(Team::addPoints).thenComparing(Team::goalDifference).reversed().thenComparing(Team::getName)).collect(Collectors.toList());
        IntStream.range(0,teamList.size()).forEach(i-> System.out.printf("%2d. %s\n", i+1, teamList.get(i)));
    }
}