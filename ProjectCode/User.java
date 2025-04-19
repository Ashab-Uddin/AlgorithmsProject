// Social Network Friend Recommendation System using BFS, DFS, and Dijkstra in Java

import java.util.*;

class User {
    String name;
    List<User> friends;

    User(String name) {
        this.name = name;
        this.friends = new ArrayList<>();
    }

    void addFriend(User user) {
        if (!friends.contains(user)) {
            friends.add(user);
            user.friends.add(this); // Bidirectional friendship
        }
    }
}

class FriendRecommendationSystem {

    // BFS: Recommend friends of friends
    public static Set<String> recommendByBFS(User user) {
        Set<String> recommended = new HashSet<>();
        Queue<User> queue = new LinkedList<>();
        Set<User> visited = new HashSet<>();

        queue.add(user);
        visited.add(user);

        while (!queue.isEmpty()) {
            User current = queue.poll();
            for (User friend : current.friends) {
                if (!visited.contains(friend)) {
                    visited.add(friend);
                    queue.add(friend);
                    for (User fof : friend.friends) {
                        if (!fof.equals(user) && !user.friends.contains(fof)) {
                            recommended.add(fof.name);
                        }
                    }
                }
            }
        }

        return recommended;
    }

    // DFS: Get connected cluster of friends
    public static void dfs(User user, Set<User> visited) {
        if (visited.contains(user)) return;
        visited.add(user);
        for (User friend : user.friends) {
            dfs(friend, visited);
        }
    }

    public static Set<String> getCluster(User user) {
        Set<User> visited = new HashSet<>();
        dfs(user, visited);
        Set<String> cluster = new HashSet<>();
        for (User u : visited) {
            cluster.add(u.name);
        }
        return cluster;
    }

    // Dijkstra: Find shortest connection paths
    public static Map<User, Integer> shortestPaths(User source, List<User> allUsers) {
        Map<User, Integer> distance = new HashMap<>();
        for (User user : allUsers) {
            distance.put(user, Integer.MAX_VALUE);
        }
        distance.put(source, 0);

        PriorityQueue<User> pq = new PriorityQueue<>(Comparator.comparingInt(distance::get));
        pq.add(source);

        while (!pq.isEmpty()) {
            User current = pq.poll();
            for (User neighbor : current.friends) {
                int alt = distance.get(current) + 1;
                if (alt < distance.get(neighbor)) {
                    distance.put(neighbor, alt);
                    pq.add(neighbor);
                }
            }
        }

        return distance;
    }
}

public class Main {
    public static void main(String[] args) {
        // Create users
        User A = new User("A");
        User B = new User("B");
        User C = new User("C");
        User D = new User("D");
        User E = new User("E");

        // Create friendships
        A.addFriend(B);
        B.addFriend(C);
        C.addFriend(D);
        D.addFriend(E);

        List<User> allUsers = Arrays.asList(A, B, C, D, E);

        // 1. BFS Recommendation
        System.out.println("\nBFS Recommendation for A:");
        System.out.println(FriendRecommendationSystem.recommendByBFS(A));

        // 2. DFS Cluster
        System.out.println("\nDFS Cluster for A:");
        System.out.println(FriendRecommendationSystem.getCluster(A));

        // 3. Dijkstra Shortest Path from A
        System.out.println("\nDijkstra Shortest Paths from A:");
        Map<User, Integer> distances = FriendRecommendationSystem.shortestPaths(A, allUsers);
        for (User u : distances.keySet()) {
            System.out.println(u.name + " : " + distances.get(u));
        }
    }
}
