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
