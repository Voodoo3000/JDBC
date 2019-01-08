package com.epam.jpm;

import com.epam.jpm.dao.*;
import com.epam.jpm.entity.Friendship;

import com.epam.jpm.entity.Post;
import com.epam.jpm.util.RandomGenerator;
import org.apache.log4j.Logger;


import java.sql.Date;
import java.util.*;

public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class);

    public static void main(String[] args) {
        RandomGenerator generator = new RandomGenerator();
        Dao userDao = new UserDao();
        Dao friendshipDao = new FriendshipDao();
        Dao postDao = new PostDao();
        Dao likeDao = new LikeDao();

        /*for (int i = 0; i < 500; i++) {
            User user = new User();
            user.setFirstName("Name" + i);
            user.setLastName("Surname" + i);
            user.setBirthday(generator.getRandomDate(1970, 2007));
            userDao.add(user);
        }*/

       /* for (int i = 0; i < 7000; i++) {
            Friendship friendship = new Friendship();
            friendship.setUserId1(userDao.getById(generator.randBetween(2776, 3025)).getId());
            friendship.setUserId2(userDao.getById(generator.randBetween(3026, 3275)).getId());

            Friendship friendship1 = ((FriendshipDao) friendshipDao).getFriendshipByUserIds(friendship.getUserId1(), friendship.getUserId2());
            if (friendship1 != null) {
                if (friendship.getUserId1() == friendship1.getUserId1() && friendship.getUserId2() == friendship1.getUserId2()) {
                    LOGGER.info("Friendship between userId1: " + friendship.getUserId1() + " and userId2: "
                            + friendship.getUserId2() + " already exist");
                    LOGGER.info("Changing randomly generated user ID-1");
                    friendship.setUserId1(userDao.getById(generator.randBetween(2776, 3025)).getId());
                }
            }
            friendship.setTimestamp(generator.getRandomDate(2018, 2022));
            friendshipDao.add(friendship);
        }*/

        /*for (int i = 0; i < 1001; i++) {
            Post post = new Post();
            post.setUserId(userDao.getById(generator.randBetween(2776, 3275)).getId());
            post.setText("Post text" + i);
            post.setTimestamp(generator.getRandomDate(2018, 2025));
            postDao.add(post);
        }*/

        /*for (int i = 0; i < 20000; i++) {
            Like like = new Like();
            Post post = (Post) postDao.getById(generator.randBetween(1, 1001));
            like.setUserId(userDao.getById(generator.randBetween(2776, 3275)).getId());
            like.setPostId(post.getId());
            if (post.getUserId() == like.getUserId()) {
                LOGGER.info("Users who posted and liked this post have the same id: " + like.getUserId() +
                        "\n Liking your own posts is prohibited");
                LOGGER.info("Taking random user from DB again");
                like.setUserId(userDao.getById(generator.randBetween(2776, 3275)).getId());
            }
            Like like1 = ((LikeDao) likeDao).getLikeByPostAndUserId(like.getPostId(), like.getUserId());
            if(like1 != null) {
                if (like.getPostId() == like1.getPostId() && like.getUserId() == like1.getUserId()) {
                    LOGGER.info("Post: " + like.getPostId() + " and with like from user: "
                            + like.getUserId() + " is already in DB");
                    LOGGER.info("Taking random post from DB again");
                    like.setPostId(postDao.getById(generator.randBetween(1, 1001)).getId());
                }
            }
            like.setTimestamp(generator.getRandomDate(2018, 2025));
            likeDao.add(like);
        }*/

        Date timestamp = new Date(2020 - 1900, 5 - 1, 1);
        List<Friendship> friendships = friendshipDao.getAll();
        Map<Integer, List<Integer>> friendshipMap = new HashMap<>();
        List<Integer> friendIds;

        for (Friendship friendship : friendships) {
            friendIds = ((FriendshipDao) friendshipDao).getAllFriendshipsMultipleParams( "USER_ID2","USER_ID1", friendship.getUserId1(), timestamp);
            friendshipMap.put(friendship.getUserId1(), friendIds);
            friendIds = ((FriendshipDao) friendshipDao).getAllFriendshipsMultipleParams( "USER_ID1","USER_ID2", friendship.getUserId2(), timestamp);
            friendshipMap.put(friendship.getUserId2(), friendIds);
        }

        for(Map.Entry<Integer, List<Integer>> integerListEntry : friendshipMap.entrySet()) {
            if(integerListEntry.getValue().size() >= 20) {
                LOGGER.info("User id: " + integerListEntry.getKey() + " has " + integerListEntry.getValue().size() + " friends");
                LOGGER.info("His friend ID's: " + integerListEntry.getValue());
            }
        }

        //Set<Integer> usersWhoHasLikes = new TreeSet<>();
        for(int i : ((LikeDao) likeDao).getLikedPostIdsByLikesQuantity(20)) {
            Post post = (Post) postDao.getById(i);
            if(friendshipMap.containsKey(post.getUserId()) && friendshipMap.get(post.getUserId()).size() >= 15) {
                LOGGER.info("User id: " + post.getUserId() + " has " + friendshipMap.get(post.getUserId()).size()
                        + " friends and more than 25 likes in post id: " + post.getId());
            }
        }
    }
}
