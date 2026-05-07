package edu.sdccd.cisc191;

import java.util.*;
import java.util.stream.Collectors;

public class GameServerAnalytics {

    /**
     * Returns the usernames of the top N rated players.
     *
     * @param players collection with all the players that will be checked
     * @param n number of players that should be returned
     * @return List<String> with only the top N players
     */

    public static List<String> findTopNUsernamesByRating(Collection<PlayerAccount> players, int n) {

        return players.stream()
                .sorted((a, b) -> Integer.compare(b.rating(), a.rating()))// highest first
                /* using
                 * .sorted(Comparator.comparingInt(PlayerAccount :: rating).reversed()).thenComparing(PlayerAccount::username))
                 * would make it more readable and ensure consistency
                 */
                .limit(n)
                .map(PlayerAccount::username)
                .toList();
    }

    /**
     * Computes the average rating by each region
     *
     * @param players collection with all the players that will be used
     * @return Map<String, Double> with the region as key and the average rating as value.
     */

    public static Map<String, Double> averageRatingByRegion(Collection<PlayerAccount> players) {

        return players.stream()
                .collect(Collectors.groupingBy(
                        PlayerAccount::region,
                        Collectors.averagingInt(PlayerAccount::rating)
                ));
    }

    /**
     * Find the repeating usernames in a collection.
     *
     * @param players collection with all the players that will be used
     * @return Set<String> with the usernames of every repeated user.
     */

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

    /**
     * Collects every username by their player tier.
     *
     * @param players collection with all the players that will be used
     * @return Map<String, List<String> with the tier as key and the list of player on that tier as value.
     */

    public static Map<String, List<String>> groupUsernamesByTier(Collection<PlayerAccount> players) {

        return players.stream()
                .collect(Collectors.groupingBy(
                        GameServerAnalytics::tierFor,
                        Collectors.mapping(PlayerAccount::username, Collectors.toList())
                ));
    }

    /**
     * Groups all the match summaries by the players in the match
     *
     * @param matches collection with all the played matches
     * @return Map<String, List<String> with the username as key and the list of match summaries as value.
     */

    public static Map<String, List<String>> buildRecentMatchSummariesByPlayer(Collection<MatchRecord> matches) {

        Map<String, List<String>> result = new HashMap<>();

        for (MatchRecord match : matches) {
            String summary = match.summary();

            result.computeIfAbsent(match.playerOne().username(), k -> new ArrayList<>()).add(summary);
            result.computeIfAbsent(match.playerTwo().username(), k -> new ArrayList<>()).add(summary);
        }
        return result;
    }

    /**
     * Returns the higher value according to a specified comparator
     *
     * @param first first value or object
     * @param second second value or object.
     * @param comparator comparator that specifies the parameters for the comparison.
     * @return highest value between the two according to comparator.
     */

    public static <T> T pickHigherRated(T first, T second, Comparator<T> comparator) {

        return comparator.compare(first, second) >= 0 ? first : second;
    }

    public static String tierFor(PlayerAccount player) {
        if (player.rating() < 1000) return "Bronze";
        if (player.rating() < 1400) return "Silver";
        return "Gold";
    }
}
