package com.example.toysocialnetwork.utils;

import com.example.toysocialnetwork.domain.User;

import java.util.*;

public class Graph {
    public static void dfs(User user, Map<Long, Boolean> visited, List<User> comp) {
        visited.put(user.getId(), true);
        comp.add(user);
        for (User friend : user.getFriends()) {
            if (visited.get(friend.getId()) == null) {
                dfs(friend, visited, comp);
            }
        }
    }


    public static int bfs(List<User> users, User start) {
        Map<Long, Integer> distances = new HashMap<>();

        distances.put(start.getId(), 0);
        Queue<User> userQueue = new LinkedList<>();
        userQueue.add(start);

        while (!userQueue.isEmpty()) {
            User user = userQueue.remove();
            Integer dist = distances.get(user.getId());
            for (User friend : user.getFriends()) {
                if (distances.get(friend.getId()) == null) {
                    distances.put(friend.getId(), 1 + dist);
                    userQueue.add(friend);
                } else if (distances.get(friend.getId()) > 1 + dist) {
                    distances.replace(friend.getId(), 1 + dist);
                    userQueue.add(friend);
                }
            }
        }

        int ans = 0;
        for (Map.Entry<Long, Integer> entries : distances.entrySet()) {
            ans = Integer.max(ans, entries.getValue());
        }

        return ans;
    }
}
