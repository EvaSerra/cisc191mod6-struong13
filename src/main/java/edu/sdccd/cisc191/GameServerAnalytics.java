package edu.sdccd.cisc191;

import java.util.*;
import java.util.stream.Collectors;

public class GameServerAnalytics {

    public static List<String> findTopNUsernamesByRating(Collection<PlayerAccount> players, int n) {

        return players.stream()
                .sorted((a, b) -> Integer.compare(b.rating(), a.rating())) // highest first
                .limit(n)
                .map(PlayerAccount::username)
                .toList();
    }

    public static Map<String, Double> averageRatingByRegion(Collection<PlayerAccount> players) {

        return players.stream()
                .collect(Collectors.groupingBy(
                        PlayerAccount::region,
                        Collectors.averagingInt(PlayerAccount::rating)
                ));
    }

    public static Set<String> findDuplicateUsernames(Collection<PlayerAccount> players) {

        Set<String> seen = new HashSet<>();
        Set<String> duplicates = new HashSet<>();

        for (PlayerAccount player : players) {
            if (!seen.add(player.username())) {
                duplicates.add(player.username());
            }
        }
        return duplicates;
    }

    public static Map<String, List<String>> groupUsernamesByTier(Collection<PlayerAccount> players) {

        return players.stream()
                .collect(Collectors.groupingBy(
                        GameServerAnalytics::tierFor,
                        Collectors.mapping(PlayerAccount::username, Collectors.toList())
                ));
    }

    public static Map<String, List<String>> buildRecentMatchSummariesByPlayer(Collection<MatchRecord> matches) {

        Map<String, List<String>> result = new HashMap<>();

        for (MatchRecord match : matches) {
            String summary = match.summary();

            result.computeIfAbsent(match.playerOne().username(), k -> new ArrayList<>()).add(summary);
            result.computeIfAbsent(match.playerTwo().username(), k -> new ArrayList<>()).add(summary);
        }
        return result;
    }

    public static <T> T pickHigherRated(T first, T second, Comparator<T> comparator) {

        return comparator.compare(first, second) >= 0 ? first : second;
    }

    public static String tierFor(PlayerAccount player) {
        if (player.rating() < 1000) return "Bronze";
        if (player.rating() < 1400) return "Silver";
        return "Gold";
    }
}
