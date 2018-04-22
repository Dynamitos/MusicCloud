/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pojo.Album;
import pojo.ApplicationKeys;
import pojo.Song;

/**
 *
 * @author Dynamitos
 */
public class ModalServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String user = request.getParameter("user");
        String album = request.getParameter("album");

        EntityManager em = (EntityManager) getServletContext().getAttribute(ApplicationKeys.ENTITY_MANAGER);

        if (album != null) {
            TypedQuery<Album> albumQuery = em.createNamedQuery("Album.loadUserAlbum", Album.class);
            albumQuery.setParameter("userid", Integer.parseInt(user));
            albumQuery.setParameter("albumname", album);
            Album result = albumQuery.getSingleResult();
            List<Song> songs = result.getSongs();
            Collections.sort(songs);
            StringBuilder html = new StringBuilder();
            html.append("<div class=\"list-group\" id=\"list-tab\" role=\"tablist\">\n");
            for (Song song : songs) {
                html.append("<a class=\"list-group-item list-group-item-action\">");
                html.append(song.getTitle());
                html.append("</a>\n");
            }
            html.append("</div>");
            try (PrintWriter out = response.getWriter()) {
                out.print(html);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
