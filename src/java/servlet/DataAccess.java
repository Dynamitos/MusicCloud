/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import pojo.ApplicationKeys;
import pojo.User;

/**
 *
 * @author Dynamitos
 */
public class DataAccess {

    private User cachedUser;
    private User nullUser;
    private static DataAccess instance;

    private DataAccess()
    {
        nullUser = new User();
        nullUser.setId(0);
        nullUser.setEmail("");
    }
    
    public static DataAccess getInstance() {
        if (instance == null) {
            instance = new DataAccess();
        }
        return instance;
    }
    public int getLoggedInUserId(HttpServletRequest request)
    {
        Cookie[] cookies = request.getCookies();
        int userId = 0;
        try {
            for (Cookie c : cookies) {
                if (c.getName().equals("user")) {
                    userId = Integer.parseInt(c.getValue());
                }
            }
        } catch (NullPointerException e) {
            //There were no cookies, so just use default user
        }
        return userId;
    }
    public User getUserFromId(HttpServletRequest request, int userId) {
        if (cachedUser == null) {
            EntityManager em = (EntityManager) request.getServletContext().getAttribute(ApplicationKeys.ENTITY_MANAGER);
            TypedQuery<User> userQuery = em.createNamedQuery("User.loadUserById", User.class);
            userQuery.setParameter("userId", userId);
            List<User> resultList = userQuery.getResultList();
            if(resultList.isEmpty())
            {
                cachedUser = nullUser;
            }
            else
            {
                cachedUser = resultList.get(0);
            }
        }
        return cachedUser;
    }
    public User getLoggedInUser(HttpServletRequest request)
    {
        return getUserFromId(request, getLoggedInUserId(request));
    }
}
